package ru.tagilov.avitotrainee.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import ru.tagilov.avitotrainee.data.ForecastRepository
import ru.tagilov.avitotrainee.data.LocationRepository
import ru.tagilov.avitotrainee.ui.entity.City
import ru.tagilov.avitotrainee.ui.screen.ForecastScreenState
import ru.tagilov.avitotrainee.ui.entity.Forecast
import ru.tagilov.avitotrainee.ui.entity.PermissionState
import timber.log.Timber

class ForecastViewModel : ViewModel() {

    private val locationRepo = LocationRepository()
    private val forecastRepo = ForecastRepository()
    private var isApiLocation = true
    private var apiLocationFailed = false
    private var delayForecast = false

    fun configure(city: City?): Boolean {
        Timber.d("ViewModel configured!")
        setCity(city = city)
        return true
    }

    private val permissionStateMutableFlow = MutableStateFlow<PermissionState>(PermissionState.None)
    val permissionStateFlow: StateFlow<PermissionState>
        get() = permissionStateMutableFlow

    private val screenStateMutableFlow = MutableStateFlow<ForecastScreenState>(ForecastScreenState.None)
    val screenStateFlow: StateFlow<ForecastScreenState>
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

    private val cityMutableFlow = MutableStateFlow<City?>(null)
    val cityFlow: StateFlow<City?>
        get() = cityMutableFlow

    private fun setCity(city: City?) {
        viewModelScope.launch {
            if (city == null) {
                permissionStateMutableFlow.emit(PermissionState.Required)
            } else if (cityMutableFlow.value == null) {
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
                        ?: City(name = null, longitude = long, latitude = lat)
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
                if (loc != null) {
                    setLocation(lat = loc.latitude, long = loc.longitude, fromApi = true)
                } else {
                    apiLocationFailed = true
                    Timber.d("Something wrong with location")
                    if (permissionStateMutableFlow.value == PermissionState.Denied)
                        screenStateMutableFlow.emit(ForecastScreenState.ErrorState.Location)
                }
            }
            currentLocationJob = null
        }
    }

    private val forecastMutableFlow = MutableStateFlow<Forecast?>(null)
    val forecastFlow: StateFlow<Forecast?>
        get() = forecastMutableFlow
    private var currentForecastJob: Job? = null

    fun getForecast() {
        currentForecastJob?.cancel()
        currentForecastJob = viewModelScope.launch {
            val city = cityMutableFlow.value
            if (city == null) {
                getLocation()
                delayForecast = true
            } else {
                screenStateMutableFlow.emit(ForecastScreenState.Loading)
                delayForecast = false
                forecastRepo
                    .getCityName(longitude = city.longitude, latitude = city.latitude)
                    .onEach {
                        Timber.d("new city $it")
                        if (it != null)
                            cityMutableFlow.emit(city.copy(name = it))
//                        else if (cityMutableFlow.value == null)
//
                    }.launchIn(viewModelScope)
                forecastRepo
                    .getWeather(longitude = city.longitude, latitude = city.latitude)
                    .onEach {
                        Timber.d("new weather $it")
                        if (it != null)
                            forecastMutableFlow.emit(it)
                        else if (forecastMutableFlow.value == null)
                            screenStateMutableFlow.emit(ForecastScreenState.ErrorState.Connection)
                    }.launchIn(viewModelScope)
                isRefreshingMutableFlow.emit(false)
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
            screenStateMutableFlow.emit(ForecastScreenState.Loading)
        }
//        forecastMutableFlow.onEach {
//            if (apiLocationFailed)
//                screenStateMutableFlow.emit(ForecastScreenState.ErrorState.Location)
//        }.launchIn(viewModelScope)
        getForecast()
        screenStateMutableFlow.onEach {
            Timber.d("New screen state - $it")
        }.launchIn(viewModelScope)
        permissionStateMutableFlow.onEach {
            if (it == PermissionState.Denied && apiLocationFailed)
                screenStateMutableFlow.emit(ForecastScreenState.ErrorState.Location)
        }.launchIn(viewModelScope)
    }
}