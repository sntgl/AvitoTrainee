package ru.tagilov.avitotrainee.forecast.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.tagilov.avitotrainee.core.ShowSnackbarEvent
import ru.tagilov.avitotrainee.core.SnackBarMessage
import ru.tagilov.avitotrainee.core.routing.CityParcelable
import ru.tagilov.avitotrainee.core.routing.toSavedCity
import ru.tagilov.avitotrainee.core.util.TypedResult
import ru.tagilov.avitotrainee.forecast.data.ForecastRepository
import ru.tagilov.avitotrainee.forecast.data.LocationRepository
import ru.tagilov.avitotrainee.forecast.ui.entity.Forecast
import ru.tagilov.avitotrainee.forecast.ui.entity.PermissionState
import ru.tagilov.avitotrainee.forecast.ui.screen.ForecastState
import ru.tagilov.avitotrainee.forecast.ui.screen.SavedState
import timber.log.Timber
import javax.inject.Inject

class ForecastViewModel @Inject constructor(
        private val locationRepo: LocationRepository,
        private val forecastRepo: ForecastRepository,
) : ViewModel() {
    private var isApiLocation = true
    private var apiLocationFailed = false
    private var delayForecast = false

    fun configure(city: CityParcelable?) {
        Timber.d("City configured: $city")
        setCity(city = city)
        getForecast()
    }

    private val permissionStateMutableFlow = MutableStateFlow<PermissionState>(PermissionState.None)
    val permissionStateFlow: StateFlow<PermissionState>
        get() = permissionStateMutableFlow

    private val isLocationMutableFlow = MutableStateFlow(true)
    val isLocationFlow: StateFlow<Boolean>
        get() = isLocationMutableFlow

    private val screenStateMutableFlow = MutableStateFlow<ForecastState>(ForecastState.None)
    val stateFlow: StateFlow<ForecastState>
        get() = screenStateMutableFlow

    private val isRefreshingMutableFlow = MutableStateFlow(false)
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


    @Synchronized //???? ?????????? ???????????? ???? ???????????? ???????????????????? - ?? ???? ??????????????, ?? ???? ??????, ?????? ?????????? ???????????????????????? ????????????????
    fun setLocation(long: Double, lat: Double, fromApi: Boolean) {
        Timber.d("got loc $long, $lat (from api - $fromApi)")
        viewModelScope.launch {
            if (isApiLocation || !fromApi) {
                val city = cityMutableFlow.value
                cityMutableFlow.emit(
                    city?.copy(longitude = long, latitude = lat)
                        ?: CityParcelable(
                            id = null,
                            name = null,
                            countryCode = null,
                            longitude = long,
                            latitude = lat
                        )
                )
                isApiLocation = fromApi
            }
        }
    }

    private var currentLocationJob: Job? = null
    private fun getLocation() {
        currentLocationJob?.cancel()
        currentLocationJob = viewModelScope.launch {
            if (permissionStateFlow.value == PermissionState.None)
                permissionStateMutableFlow.emit(PermissionState.Required)
            locationRepo.getLocation().collect { loc ->
                when (loc) {
                    is TypedResult.Err -> {
                        apiLocationFailed = true
                        if (permissionStateMutableFlow.value == PermissionState.Denied)
                            screenStateMutableFlow.emit(ForecastState.ErrorState.Location)
                    }
                    is TypedResult.Ok -> {
                        setLocation(
                            lat = loc.result.latitude,
                            long = loc.result.longitude,
                            fromApi = true
                        )
                    }
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
        val cityId = cityMutableFlow.value?.id
        if (
            savedCityMutableFlow.value == SavedState.NONE &&
            permissionStateMutableFlow.value == PermissionState.None &&
            cityId != null
        ) {
            forecastRepo.checkSaved(cityId).onEach {
                savedCityMutableFlow.emit(it)
            }.launchIn(viewModelScope)
        }
    }

    fun save() {
        cityMutableFlow.value?.let {
            viewModelScope.launch {
                try {
                    forecastRepo.saveCity(it.toSavedCity())
                } catch (e: IllegalArgumentException) {
                    showSnackBarMutableEvent.emit(ShowSnackbarEvent(SnackBarMessage.UNABLE_SAVE))
                }
            }
        }
    }

    private fun getForecast() { //??????????-???? ???????????? ??????????????????, ???? ?????????????????? ??????????
        currentForecastJob?.cancel()
        currentForecastJob = viewModelScope.launch {
            screenStateMutableFlow.emit(ForecastState.Loading)
            val oldCity = cityMutableFlow.value
            //??????????-???? continuation ??????????????????))
            when {
                oldCity == null -> {
                    getLocation()
                    delayForecast = true
                }
                cityMutableFlow.value?.name == null -> {
                    delayForecast = false
                    val cityFlow = forecastRepo
                        .getCityName(longitude = oldCity.longitude, latitude = oldCity.latitude)
                    val forecastFlow = forecastRepo
                        .getWeather(longitude = oldCity.longitude, latitude = oldCity.latitude)
                    cityFlow
                        .zip(forecastFlow) { t1, t2 -> t1 to t2 }
                        .onEach { (city, forecast) ->
                            isRefreshingMutableFlow.emit(false)
                            if (city is TypedResult.Ok && forecast is TypedResult.Ok) {
                                cityMutableFlow.emit(oldCity.copy(name = city.result))
                                forecastMutableFlow.emit(forecast.result)
                            } else if (forecastMutableFlow.value == null)
                                screenStateMutableFlow.emit(ForecastState.ErrorState.Connection)
                            else
                                showSnackBarMutableEvent.emit(
                                    ShowSnackbarEvent(SnackBarMessage.UNABLE_LOAD)
                                )
                        }.launchIn(viewModelScope)
                }
                else -> {
                    delayForecast = false
                    forecastRepo
                        .getWeather(longitude = oldCity.longitude, latitude = oldCity.latitude)
                        .onEach { forecast ->
                            isRefreshingMutableFlow.emit(false)
                            when {
                                forecast is TypedResult.Err ->
                                    forecastMutableFlow.emit(null)
                                forecastMutableFlow.value == null ->
                                    screenStateMutableFlow.emit(ForecastState.ErrorState.Connection)
                                else ->
                                    showSnackBarMutableEvent.emit(
                                        ShowSnackbarEvent(SnackBarMessage.UNABLE_LOAD)
                                    )
                            }
                        }.launchIn(viewModelScope)
                }
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