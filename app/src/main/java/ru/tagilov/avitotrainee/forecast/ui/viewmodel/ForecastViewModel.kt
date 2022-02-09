package ru.tagilov.avitotrainee.forecast.ui.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import ru.tagilov.avitotrainee.core.SnackBarMessage
import ru.tagilov.avitotrainee.core.SnackbarEvent
import ru.tagilov.avitotrainee.core.routing.CityParcelable
import ru.tagilov.avitotrainee.core.util.TypedResult
import ru.tagilov.avitotrainee.core.util.plusAssign
import ru.tagilov.avitotrainee.forecast.data.ForecastRepository
import ru.tagilov.avitotrainee.forecast.data.LocationRepository
import ru.tagilov.avitotrainee.forecast.data.entity.SetLocationData
import ru.tagilov.avitotrainee.forecast.data.entity.toCity
import ru.tagilov.avitotrainee.forecast.ui.entity.City
import ru.tagilov.avitotrainee.forecast.ui.entity.Forecast
import ru.tagilov.avitotrainee.forecast.ui.entity.GeoOrigin
import ru.tagilov.avitotrainee.forecast.ui.entity.PermissionState
import ru.tagilov.avitotrainee.forecast.ui.entity.complete
import ru.tagilov.avitotrainee.forecast.ui.entity.toCity
import ru.tagilov.avitotrainee.forecast.ui.entity.toSaved
import ru.tagilov.avitotrainee.forecast.ui.screen.ForecastState
import ru.tagilov.avitotrainee.forecast.ui.screen.SavedState
import timber.log.Timber
import javax.inject.Inject

class ForecastViewModel @Inject constructor(
    private val locationRepo: LocationRepository,
    private val forecastRepo: ForecastRepository,
) : ViewModel() {
    private val disposables = CompositeDisposable()

    private val screenStateSubject: BehaviorSubject<ForecastState> = BehaviorSubject.create()
    val screenStateObservable: Observable<ForecastState>
        get() = screenStateSubject.hide()

    private val forecastSubject: BehaviorSubject<Forecast> = BehaviorSubject.create()
    val forecastObservable: Observable<Forecast>
        get() = forecastSubject.hide()

    private val savedStateSubject: BehaviorSubject<SavedState> = BehaviorSubject.create()
    val savedStateObservable: Observable<SavedState>
        get() = savedStateSubject.hide()

    private val showSnackBarSubject: BehaviorSubject<SnackbarEvent> = BehaviorSubject.create()
    val showSnackBarObservable: Observable<SnackbarEvent>
        get() = showSnackBarSubject.hide()

    private val permissionStateSubject: BehaviorSubject<PermissionState> = BehaviorSubject.create()
    val permissionStateObservable: Observable<PermissionState>
        get() = permissionStateSubject.hide()

    private val citySubject: BehaviorSubject<City> = BehaviorSubject.create()
    val cityObservable: Observable<City>
        get() = citySubject.hide()

    private val setLocationSubject = BehaviorSubject.create<SetLocationData>()

    init {
        subscribeToLocationPermission()
        subscribeToCity()
        subscribeToNewLocation()
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    fun setCity(city: CityParcelable) {
        citySubject.onNext(city.toCity())
        city.id?.let { subscribeToSavedState(it) }
    }

    fun setEmptyCity() {
        citySubject.onNext(City.Empty)
        screenStateSubject.onNext(ForecastState.Loading)
    }

    fun newPermissionState(state: PermissionState) {
        permissionStateSubject.onNext(state)
    }

    fun save() {
        val cityValue = citySubject.value
        if (cityValue is City.Full) {
            disposables += forecastRepo
                .saveCityRx(cityValue.toSaved()).subscribe({ }, { unableSave() })
        } else {
            unableSave()
        }
    }

    fun refresh() {
        val cityValue = citySubject.value
        citySubject.onNext(cityValue ?: City.Empty)
    }

    fun setGPSLocation(long: Double, lat: Double) {
        setLocationSubject.onNext(SetLocationData(lat = lat, long = long, origin = GeoOrigin.GPS))
    }

    private fun unableSave() {
        showSnackBarSubject.onNext(SnackbarEvent.Show(SnackBarMessage.UNABLE_SAVE))
    }

    private fun setApiLocation(long: Double, lat: Double) {
        setLocationSubject.onNext(SetLocationData(long = long, lat = lat, origin = GeoOrigin.API))
    }

    private fun getLocation() {
        disposables += locationRepo.getLocationRx()
            .subscribe({ loc ->
                when (loc) {
                    is TypedResult.Err -> {
                        screenStateSubject.onNext(ForecastState.ErrorState.Location)
                    }
                    is TypedResult.Ok -> {
                        loc.result.let { setApiLocation(lat = it.latitude, long = it.longitude) }
                    }
                }
            }, ::handleError)
    }

    private fun getName(city: City.WithGeo) {
        disposables += forecastRepo
            .getCityNameRx(longitude = city.longitude, latitude = city.latitude)
            .subscribe({
                when (it) {
                    is TypedResult.Err ->
                        screenStateSubject.onNext(ForecastState.ErrorState.Connection)
                    is TypedResult.Ok ->
                        citySubject.onNext(city.complete(it.result.name, it.result.countryCode))
                }
            }, ::handleError)
    }

    private fun getForecast(city: City.Full) {
        screenStateSubject.onNext(ForecastState.Loading)
        disposables += forecastRepo
            .getWeatherRx(longitude = city.longitude, latitude = city.latitude)
            .subscribe({ forecast ->
                screenStateSubject.onNext(ForecastState.Content)
                when (forecast) {
                    is TypedResult.Ok -> forecastSubject.onNext(forecast.result)
                    is TypedResult.Err -> {
                        if (forecastSubject.value is Forecast.Data) {
                            showSnackBarSubject.onNext(
                                SnackbarEvent.Show(SnackBarMessage.UNABLE_LOAD)
                            )
                        } else {
                            screenStateSubject.onNext(ForecastState.ErrorState.Connection)
                        }
                    }
                }
            }, ::handleError)
    }

    private fun handleError(throwable: Throwable) {
        Timber.e(throwable, "RxJava error!")
        screenStateSubject.onNext(ForecastState.ErrorState.Connection)
    }

    private fun subscribeToNewLocation() {
        disposables += setLocationSubject
            .zipWith(citySubject, { newLocation, currentCity -> newLocation to currentCity })
            .subscribe({ (newLocation, currentCity) ->
                if (currentCity is City.Empty) {
                    citySubject.onNext(newLocation.toCity())
                }
            }, ::handleError)
    }

    private fun subscribeToLocationPermission() {
        disposables += permissionStateSubject
            .filter { it is PermissionState.Denied }
            .subscribe({
                getLocation()
            }, ::handleError)
    }

    private fun requireLocation() {
        permissionStateSubject.onNext(PermissionState.Required)
    }

    private fun subscribeToSavedState(cityId: String) {
        disposables += forecastRepo.checkSavedRx(cityId)
            .subscribe({ savedStateSubject.onNext(it) }, ::handleError)
    }

    private fun subscribeToCity() {
        disposables += citySubject
            .subscribe({ city ->
                when (city) {
                    is City.Empty -> requireLocation()
                    is City.WithGeo -> getName(city = city)
                    is City.Full -> getForecast(city = city)
                }
            }, ::handleError)
    }
}