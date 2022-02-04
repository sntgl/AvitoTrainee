package ru.tagilov.avitotrainee.forecast.ui.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.ReplaySubject
import ru.tagilov.avitotrainee.core.SnackBarMessage
import ru.tagilov.avitotrainee.core.SnackbarEvent
import ru.tagilov.avitotrainee.core.routing.CityParcelable
import ru.tagilov.avitotrainee.core.routing.toSavedCity
import ru.tagilov.avitotrainee.core.util.TypedResult
import ru.tagilov.avitotrainee.core.util.plusAssign
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
    private val disposables = CompositeDisposable()

    private var isApiLocation = true
    private var apiLocationFailed = false
    private var delayForecast = false

    private val isGPSLocationSubject: ReplaySubject<Boolean> = ReplaySubject.create()
    val isGPSLocationObservable: Observable<Boolean>
        get() = isGPSLocationSubject.hide()

    private val isRefreshingSubject: BehaviorSubject<Boolean> = BehaviorSubject.create()
    val isRefreshingObservable: Observable<Boolean>
        get() = isRefreshingSubject.hide()

    private val savedCitySubject: BehaviorSubject<SavedState> = BehaviorSubject.create()
    val savedCityObservable: Observable<SavedState>
        get() = savedCitySubject.hide()

    private val forecastSubject: BehaviorSubject<Forecast> = BehaviorSubject.create()
    val forecastObservable: Observable<Forecast>
        get() = forecastSubject.hide()

    private val mShowSnackBar: BehaviorSubject<SnackbarEvent> = BehaviorSubject.create()
    val showSnackBar: Observable<SnackbarEvent>
        get() = mShowSnackBar.hide()

    private val permissionStateSubject: BehaviorSubject<PermissionState> = BehaviorSubject.create()
    val permissionStateObservable: Observable<PermissionState>
        get() = permissionStateSubject.hide()

    private val screenStateSubject: BehaviorSubject<ForecastState> = BehaviorSubject.create()
    val screenStateObservable: Observable<ForecastState>
        get() = screenStateSubject.hide()

    private val citySubject: BehaviorSubject<TypedResult<CityParcelable>> = BehaviorSubject.create()
    val cityObservable: Observable<TypedResult<CityParcelable>>
        get() = citySubject.hide()

    fun configure(city: CityParcelable) {
        Timber.d("City configured: $city")
        setCity(city)
        getForecast(city)
    }

    fun configure() {
        permissionStateSubject.onNext(PermissionState.Required)
        getForecast()
    }

    fun newPermissionState(state: PermissionState) {
        Timber.d("new permission state - $state")
        permissionStateSubject.onNext(state)
    }

    fun save() {
        val city = citySubject.value
        if (city is TypedResult.Ok) {
            disposables += forecastRepo.saveCityRx(city.result.toSavedCity()).subscribe({}, {
                mShowSnackBar.onNext(SnackbarEvent.Show(SnackBarMessage.UNABLE_SAVE))
            })
        }
    }

    fun refresh() {
        isRefreshingSubject.onNext(true)
        citySubject.value?.let { city ->
            getForecast(city)
        }
    }

    @Synchronized //тк может придти от разных источников - и от системы, и от апи, апи менее приоритетный источник
    fun setLocation(long: Double, lat: Double, fromApi: Boolean) {
        Timber.d("got loc $long, $lat (from api - $fromApi)")
        if (isApiLocation || !fromApi) {
            val city = citySubject.value
            citySubject.onNext(
                TypedResult.Ok(
                    when (city) {
                        is TypedResult.Err -> CityParcelable(
                            id = null,
                            name = null,
                            countryCode = null,
                            longitude = long,
                            latitude = lat
                        )
                        is TypedResult.Ok -> city.result.copy(longitude = long, latitude = lat)
                    }
                )
            )
            isApiLocation = fromApi
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    private fun setCity(city: CityParcelable) {
        if (citySubject.value is TypedResult.Err) {
            isGPSLocationSubject.onNext(false)
            citySubject.onNext(TypedResult.Ok(city))
        }
    }

    private fun getLocation() {
        if (permissionStateSubject.value == PermissionState.None)
            permissionStateSubject.onNext(PermissionState.Required)
        locationRepo.getLocationRx()
            .subscribe({ loc ->
                when (loc) {
                    is TypedResult.Err -> {
                        apiLocationFailed = true
                        if (permissionStateSubject.value == PermissionState.Denied)
                            screenStateSubject.onNext(ForecastState.ErrorState.Location)
                    }
                    is TypedResult.Ok -> {
                        setLocation(
                            lat = loc.result.latitude,
                            long = loc.result.longitude,
                            fromApi = true
                        )
                    }
                }
            }, ::handleError)
    }

    private fun getForecastAndName(city: CityParcelable) {
        delayForecast = false
        val nameSource = forecastRepo.getCityNameRx(
            longitude = city.longitude,
            latitude = city.latitude
        )
        val weatherSource = forecastRepo.getWeatherRx(
            longitude = city.longitude,
            latitude = city.latitude
        )

        disposables += Single.zip(
            nameSource,
            weatherSource,
            { name, forecast -> name to forecast })
            .subscribe({ (trName, trForecast) ->
                isRefreshingSubject.onNext(false)
                if (trName is TypedResult.Ok && trForecast is TypedResult.Ok) {
                    citySubject.onNext(TypedResult.Ok(city.copy(name = trName.result)))
                    forecastSubject.onNext(trForecast.result)
                } else if (forecastSubject.value is Forecast.Empty)
                    screenStateSubject.onNext(ForecastState.ErrorState.Connection)
                else
                    mShowSnackBar.onNext(SnackbarEvent.Show(SnackBarMessage.UNABLE_LOAD))
            }, ::handleError)
    }

    private fun getOnlyForecast(city: CityParcelable) {
        delayForecast = false

        disposables += forecastRepo
            .getWeatherRx(longitude = city.longitude, latitude = city.latitude)
            .subscribe({ forecast ->
                isRefreshingSubject.onNext(false)
                when {
                    forecast is TypedResult.Ok ->
                        forecastSubject.onNext(forecast.result)
                    forecastSubject.value is Forecast.Empty ->
                        screenStateSubject.onNext(ForecastState.ErrorState.Connection)
                    else ->
                        mShowSnackBar.onNext(SnackbarEvent.Show(SnackBarMessage.UNABLE_LOAD))
                }
            }, ::handleError)
    }

    private fun getForecast(city: CityParcelable? = null) = getForecast(
        when (city) {
            null -> TypedResult.Err()
            else -> TypedResult.Ok(city)
        }
    )

    private fun getForecast(city: TypedResult<CityParcelable>) {
        screenStateSubject.onNext(ForecastState.Loading)
        when (city) {
            is TypedResult.Err -> {
                getLocation()
                delayForecast = true
            }
            is TypedResult.Ok -> {
                if (city.result.name == null)
                    getForecastAndName(city.result)
                else
                    getOnlyForecast(city.result)
            }
        }
    }

    private fun handleError(throwable: Throwable? = null) {
        Timber.d("Rxjava err: $throwable")
        screenStateSubject.onNext(ForecastState.ErrorState.Connection)
    }

    init {
        permissionStateSubject.onNext(PermissionState.None)
        forecastSubject.onNext(Forecast.Empty)
        savedCitySubject.onNext(SavedState.NONE)
        isGPSLocationSubject.onNext(true)
        screenStateSubject.onNext(ForecastState.None)
        citySubject.onNext(TypedResult.Err())
        screenStateSubject.onNext(ForecastState.Loading)

        disposables += citySubject.subscribe({
            Timber.d("new city: $it")
            if (it is TypedResult.Ok && delayForecast) {
                if (delayForecast)
                    getForecast(it.result)
            }
        }, ::handleError)

        disposables += citySubject.filter { it is TypedResult.Ok && it.result.id != null }
            .map { (it as TypedResult.Ok).result }
            .subscribe { city ->
                city.id?.let { id ->
                    forecastRepo.checkSavedRx(id).subscribe({ ss: SavedState ->
                        savedCitySubject.onNext(ss)
                    }, ::handleError)
                }
            }

        disposables += permissionStateSubject.subscribe {
            if (it == PermissionState.Denied && apiLocationFailed)
                screenStateSubject.onNext(ForecastState.ErrorState.Location)
        }
    }
}