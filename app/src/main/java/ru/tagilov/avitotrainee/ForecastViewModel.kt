package ru.tagilov.avitotrainee

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import ru.tagilov.avitotrainee.data.ForecastRepository
import ru.tagilov.avitotrainee.data.LocationRepository
import ru.tagilov.avitotrainee.ui.entity.Forecast
import ru.tagilov.avitotrainee.ui.entity.PermissionState
import timber.log.Timber

class ForecastViewModel : ViewModel() {

    private val locationRepo = LocationRepository()
    private val forecastRepo = ForecastRepository()
    private var isApiLocation = true
    private var delayForecast = false

    fun configure(city: City?): Boolean {
        Timber.d("ViewModel configured!")
        setCity(city = city)
        return true
    }

    private val permissionStateMutableFlow = MutableStateFlow<PermissionState>(PermissionState.None)
    val permissionStateFlow: StateFlow<PermissionState>
        get() = permissionStateMutableFlow

    fun newPermissionState(state: PermissionState) {
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
                    if (city != null)
                        cityMutableFlow.value?.copy(longitude = long, latitude = lat)
                    else
                        City(name = null, longitude = long, latitude = lat)
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
                    Timber.d("Something wrong with location")
                    //TODO show err?
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
                delayForecast = false
                forecastRepo
                    .getCityName(longitude = city.longitude, latitude = city.latitude)
                    .collect {
                        cityMutableFlow.emit(city.copy(name = it))
                    }
                forecastRepo
                    .getWeather(longitude = city.longitude, latitude = city.latitude)
                    .collect {
                        forecastMutableFlow.emit(it)
                    }

            }
        }
        currentForecastJob = null
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
        getForecast()
    }
}