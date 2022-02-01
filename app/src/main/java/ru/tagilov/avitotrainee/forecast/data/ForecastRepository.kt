package ru.tagilov.avitotrainee.forecast.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.tagilov.avitotrainee.city.ui.entity.CityModel
import ru.tagilov.avitotrainee.city.ui.entity.toSaved
import ru.tagilov.avitotrainee.core.db.AppDatabase
import ru.tagilov.avitotrainee.core.db.SavedCity
import ru.tagilov.avitotrainee.core.routing.toSavedCity
import ru.tagilov.avitotrainee.forecast.data.entity.toForecast
import ru.tagilov.avitotrainee.forecast.ui.entity.Forecast
import ru.tagilov.avitotrainee.forecast.ui.screen.SavedState
import java.io.IOException
import javax.inject.Inject

interface ForecastRepository {
    fun getCityName(longitude: Double, latitude: Double): Flow<String?>
    fun getWeather(longitude: Double, latitude: Double): Flow<Forecast?>
    suspend fun saveCity(city: SavedCity)
    fun checkSaved(id: String): Flow<SavedState>
}

class ForecastRepositoryImpl @Inject constructor(
    private val forecastApi: ForecastApi,
    db: AppDatabase
) : ForecastRepository {
    private val cityDao = db.cityDao()

    override fun getCityName(
        longitude: Double,
        latitude: Double,
    ): Flow<String?> = flow {
        try {
            emit(
                forecastApi.getCityName(
                    longitude = longitude.toString(),
                    latitude = latitude.toString()
                )
            )
        } catch (e: IOException) {
            emit(null)
        }
    }.map {
        it?.get(0)?.localNames?.ru ?: it?.get(0)?.name
    }.flowOn(Dispatchers.IO)


    override fun getWeather(
        longitude: Double,
        latitude: Double,
    ): Flow<Forecast?> = flow {
        try {
            emit(
                forecastApi.getFullForecast(
                    longitude = longitude.toString(),
                    latitude = latitude.toString()
                )
            )
        } catch (e: IOException) {
            emit(null)
        }
    }.map {
        if (it != null)
            it.toForecast()
        else
            null
    }.flowOn(Dispatchers.IO)

    override suspend fun saveCity(city: SavedCity) {
        cityDao.save(city)
    }

    override fun checkSaved(id: String): Flow<SavedState> = cityDao.get(id).map {
        if (it != null) SavedState.SAVED else SavedState.NOT_SAVED
    }

}