package ru.tagilov.avitotrainee.forecast.ui.viewmodel

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import ru.tagilov.avitotrainee.core.util.TypedResult
import ru.tagilov.avitotrainee.forecast.data.ForecastRepository
import ru.tagilov.avitotrainee.forecast.ui.entity.Forecast
import javax.inject.Inject

interface ForecastInteractor {
    fun getForecastAndName(lon: Double, lat: Double):
            Single<Pair<TypedResult<String>, TypedResult<Forecast>>>
    fun onCleared()
}

class ForecastInteractorImpl @Inject constructor(
    private val forecastRepo: ForecastRepository
): ForecastInteractor {
    val compositeDisposable = CompositeDisposable()
    override fun getForecastAndName(
        lon: Double,
        lat: Double
    ): Single<Pair<TypedResult<String>, TypedResult<Forecast>>> {
        TODO("Not yet implemented")
    }

    override fun onCleared() {
        TODO("Not yet implemented")
    }

//    override fun getForecastAndName(lon: Double, lat: Double):
//            Single<Pair<TypedResult<String>, TypedResult<Forecast>>> {
//        val nameSource = forecastRepo.getCityNameRx(longitude = lon, latitude = lat)
//        val weatherSource = forecastRepo.getWeatherRx(longitude = lon, latitude = lat)
//        return Single.zip(nameSource, weatherSource, { name, forecast -> name to forecast })
//    }
//
//    override fun onCleared() {
//        compositeDisposable.clear()
//    }

}