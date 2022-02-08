package ru.tagilov.avitotrainee.forecast.data

import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import ru.tagilov.avitotrainee.core.db.AppDatabase
import ru.tagilov.avitotrainee.core.db.SavedCity
import ru.tagilov.avitotrainee.core.util.TypedResult
import ru.tagilov.avitotrainee.forecast.data.entity.ResponseCityName
import ru.tagilov.avitotrainee.forecast.data.entity.toCityNameData
import ru.tagilov.avitotrainee.forecast.data.entity.toForecast
import ru.tagilov.avitotrainee.forecast.di.SchedulersFactory
import ru.tagilov.avitotrainee.forecast.ui.entity.CityNameData
import ru.tagilov.avitotrainee.forecast.ui.entity.Forecast
import ru.tagilov.avitotrainee.forecast.ui.screen.SavedState
import javax.inject.Inject

interface ForecastRepository {
    fun getCityNameRx(longitude: Double, latitude: Double): Single<TypedResult<CityNameData>>
    fun getWeatherRx(longitude: Double, latitude: Double): Single<TypedResult<Forecast>>
    fun saveCityRx(city: SavedCity): Single<TypedResult<Unit>>
    fun checkSavedRx(id: String): Flowable<SavedState>
}

class ForecastRepositoryImpl @Inject constructor(
    private val forecastApi: ForecastApi,
    private val schedulers: SchedulersFactory,
    db: AppDatabase
) : ForecastRepository {
    private val cityDao = db.cityDao()

    override fun getCityNameRx(longitude: Double, latitude: Double): Single<TypedResult<CityNameData>> =
        forecastApi.getCityNameRx(longitude = longitude.toString(), latitude = latitude.toString())
            .map { result: List<ResponseCityName> ->
                if (result.isNotEmpty())
                    TypedResult.Ok(result[0].toCityNameData())
                else
                    TypedResult.Err()
            }.onErrorResumeNext { Single.just(TypedResult.Err()) }
            .subscribeOn(schedulers.io())

    override fun getWeatherRx(longitude: Double, latitude: Double): Single<TypedResult<Forecast>> =
        forecastApi.getFullForecastRx(
            longitude = longitude.toString(),
            latitude = latitude.toString()
        )
            .map { TypedResult.Ok(it.toForecast()) as TypedResult<Forecast> }
            .onErrorResumeNext { Single.just(TypedResult.Err()) }
            .subscribeOn(schedulers.io())

    override fun saveCityRx(city: SavedCity): Single<TypedResult<Unit>> =
        cityDao.saveRx(city)
            .toSingleDefault(TypedResult.Ok(Unit) as TypedResult<Unit>)
            .onErrorResumeNext {
                Single.just(TypedResult.Err())
            }.subscribeOn(schedulers.io())

    override fun checkSavedRx(id: String): Flowable<SavedState> =
        cityDao.checkSavedRx(id)
            .map {
                if (it == 0) SavedState.NOT_SAVED else SavedState.SAVED
            }
            .onErrorResumeNext {
                Flowable.just(SavedState.NOT_SAVED)
            }
            .subscribeOn(schedulers.io())

}

