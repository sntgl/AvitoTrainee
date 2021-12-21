package ru.tagilov.avitotrainee.forecast.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.tagilov.avitotrainee.core.ShowSnackbarEvent
import ru.tagilov.avitotrainee.core.db.Database
import ru.tagilov.avitotrainee.core.db.SavedCity
import ru.tagilov.avitotrainee.core.db.wrap
import ru.tagilov.avitotrainee.core.routing.CityParcelable
import ru.tagilov.avitotrainee.forecast.data.ForecastRepository
import ru.tagilov.avitotrainee.forecast.data.LocationRepository
import ru.tagilov.avitotrainee.forecast.ui.entity.Forecast
import ru.tagilov.avitotrainee.forecast.ui.entity.PermissionState
import timber.log.Timber

class ForecastViewModel : ViewModel() {

    private val locationRepo = LocationRepository()
    private val forecastRepo = ForecastRepository()
    private var isApiLocation = true
    private var apiLocationFailed = false
    private var delayForecast = false
    private val db = Database.instance.cityDao()

    fun configure(city: CityParcelable?) {
        Timber.d("City configured: $city")
        setCity(city = city)
        getForecast()
    }

    private val permissionStateMutableFlow = MutableStateFlow<PermissionState>(PermissionState.None)
    val permissionStateFlow: StateFlow<PermissionState>
        get() = permissionStateMutableFlow

    private val isLocationMutableFlow = MutableStateFlow<Boolean>(true)
    val isLocationFlow: StateFlow<Boolean>
        get() = isLocationMutableFlow

    private val screenStateMutableFlow = MutableStateFlow<ForecastState>(ForecastState.None)
    val stateFlow: StateFlow<ForecastState>
        get() = screenStateMutableFlow

    private val isRefreshingMutableFlow = MutableStateFlow<Boolean>(false)
    val isRefreshingFlow: StateFlow<Boolean>
        get() = isRefreshingMutableFlow

    fun newPermissionState(state: PermissionState) {
        Timber.d("new permission state - $state")
        viewModelScope.launch {
            permissionStateMutableFlow.emit(state)
        }
    }

    private val cityMutableFlow = MutableStateFlow<CityParcelable?>(null)
    val cityFlow: StateFlow<CityParcelable?>
        get() = cityMutableFlow

    private fun setCity(city: CityParcelable?) {
        viewModelScope.launch {
            if (city == null) {
                permissionStateMutableFlow.emit(PermissionState.Required)
            } else if (cityMutableFlow.value == null) {
                isLocationMutableFlow.emit(false)
                cityMutableFlow.emit(city)
            }
        }
    }


    @Synchronized //тк может придти от разных источников - и от системы, и от апи, апи менее приоритетный источник
    fun setLocation(long: Double, lat: Double, fromApi: Boolean) {
        Timber.d("got loc $long, $lat (from api - $fromApi)")
        viewModelScope.launch {
            if (isApiLocation || !fromApi) {
                val city = cityMutableFlow.value
                cityMutableFlow.emit(
                    city?.copy(longitude = long, latitude = lat)
                        ?: CityParcelable(name = null, longitude = long, latitude = lat)
                )
                isApiLocation = fromApi
            }
        }
    }

    private var currentLocationJob: Job? = null
    private fun getLocation() { //TODO неразумно запрашивать каждый раз локацию(
        currentLocationJob?.cancel()
        currentLocationJob = viewModelScope.launch {
            if (permissionStateFlow.value == PermissionState.None)
                permissionStateMutableFlow.emit(PermissionState.Required)
            locationRepo.getLocation().collect { loc ->
                if (loc != null) {
                    setLocation(lat = loc.latitude, long = loc.longitude, fromApi = true)
                } else {
                    apiLocationFailed = true
                    if (permissionStateMutableFlow.value == PermissionState.Denied)
                        screenStateMutableFlow.emit(ForecastState.ErrorState.Location)
                }
            }
            currentLocationJob = null
        }
    }

    private val forecastMutableFlow = MutableStateFlow<Forecast?>(null)
    val forecastFlow: StateFlow<Forecast?>
        get() = forecastMutableFlow
    private var currentForecastJob: Job? = null

    private val showSnackBarMutableEvent = MutableStateFlow<ShowSnackbarEvent?>(null)
    val showSnackBarEvent: StateFlow<ShowSnackbarEvent?>
        get() = showSnackBarMutableEvent


    private val savedCityMutableFlow = MutableStateFlow(SavedState.NONE)
    val savedCityFlow: StateFlow<SavedState>
        get() = savedCityMutableFlow


    private fun checkSaved() {
        val cityName = cityMutableFlow.value?.name
        if (
            savedCityMutableFlow.value == SavedState.NONE &&
            permissionStateMutableFlow.value == PermissionState.None &&
            cityName != null
        ) {
            db.get(cityName).onEach {
                Timber.d("GET SAVED = $it")
                savedCityMutableFlow.emit(
                    if (it != null) SavedState.SAVED else SavedState.NOT_SAVED
                )
            }.launchIn(viewModelScope)
        }
    }

    fun save() {
        cityMutableFlow.value?.let {
            viewModelScope.launch {
                db.save(SavedCity.wrap(it))
                Timber.d("SET SAVED!")
            }
        }
    }

    private fun getForecast() { //какой-то жирный получился, мб раскидать стоит
        currentForecastJob?.cancel()
        currentForecastJob = viewModelScope.launch {
            screenStateMutableFlow.emit(ForecastState.Loading)
            val oldCity = cityMutableFlow.value
            //какой-то continuation получился))
            if (oldCity == null) {
                getLocation()
                delayForecast = true
            } else if (cityMutableFlow.value?.name == null) {
                delayForecast = false
                val cityFlow = forecastRepo
                    .getCityName(longitude = oldCity.longitude, latitude = oldCity.latitude)
                val forecastFlow = forecastRepo
                    .getWeather(longitude = oldCity.longitude, latitude = oldCity.latitude)
                cityFlow
                    .zip(forecastFlow) { t1, t2 -> t1 to t2 }
                    .onEach { (city, forecast) ->
                        isRefreshingMutableFlow.emit(false)
                        if (city != null && forecast != null) {
                            cityMutableFlow.emit(oldCity.copy(name = city))
                            forecastMutableFlow.emit(forecast)
                        } else if (forecastMutableFlow.value == null)
                            screenStateMutableFlow.emit(ForecastState.ErrorState.Connection)
                        else
                            showSnackBarMutableEvent.emit(ShowSnackbarEvent())
                    }.launchIn(viewModelScope)
            } else {
                delayForecast = false
                forecastRepo
                    .getWeather(longitude = oldCity.longitude, latitude = oldCity.latitude)
                    .onEach { forecast ->
                        isRefreshingMutableFlow.emit(false)
                        when {
                            forecast != null ->
                                forecastMutableFlow.emit(forecast)
                            forecastMutableFlow.value == null ->
                                screenStateMutableFlow.emit(ForecastState.ErrorState.Connection)
                            else ->
                                showSnackBarMutableEvent.emit(ShowSnackbarEvent())
                        }
                    }.launchIn(viewModelScope)
            }
        }
        currentForecastJob = null
    }

    fun refresh() {
        viewModelScope.launch {
            isRefreshingMutableFlow.emit(true)
        }
        getForecast()
    }

    init {
        cityMutableFlow.onEach {
            Timber.d("new city: ${it?.name} at " + it?.latitude.toString() + " " + it?.longitude.toString())
            if (it != null && delayForecast) {
                getForecast()
            }
        }.launchIn(viewModelScope)

        forecastMutableFlow.onEach {
            Timber.d("new forecast: $it")
        }.launchIn(viewModelScope)

        viewModelScope.launch {
            screenStateMutableFlow.emit(ForecastState.Loading)
        }
        screenStateMutableFlow.onEach {
            Timber.d("New screen state - $it")
        }.launchIn(viewModelScope)

        permissionStateMutableFlow.onEach {
            if (it == PermissionState.Denied && apiLocationFailed)
                screenStateMutableFlow.emit(ForecastState.ErrorState.Location)
        }.launchIn(viewModelScope)

        cityMutableFlow.onEach {
            checkSaved()
        }.launchIn(viewModelScope)

        savedCityMutableFlow.onEach {
            Timber.d("Saved = $it")
        }.launchIn(viewModelScope)
    }
}