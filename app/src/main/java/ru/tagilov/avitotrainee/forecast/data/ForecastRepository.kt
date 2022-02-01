package ru.tagilov.avitotrainee.forecast.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.tagilov.avitotrainee.core.db.AppDatabase
import ru.tagilov.avitotrainee.core.db.SavedCity
import ru.tagilov.avitotrainee.core.util.TypedResult
import ru.tagilov.avitotrainee.forecast.data.entity.toForecast
import ru.tagilov.avitotrainee.forecast.ui.entity.Forecast
import ru.tagilov.avitotrainee.forecast.ui.screen.SavedState
import java.io.IOException
import javax.inject.Inject

interface ForecastRepository {
    fun getCityName(longitude: Double, latitude: Double): Flow<TypedResult<String>>
    fun getWeather(longitude: Double, latitude: Double): Flow<TypedResult<Forecast>>
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
    ) = flow {
        try {
            val result = forecastApi.getCityName(
                longitude = longitude.toString(),
                latitude = latitude.toString()
            )
            if (result.isNotEmpty())
                emit(TypedResult.Ok(result[0].localNames?.ru ?: result[0].name))
            else
                emit(TypedResult.Err())
        } catch (e: IOException) {
            emit(TypedResult.Err())
        }
    }.flowOn(Dispatchers.IO)


    override fun getWeather(
        longitude: Double,
        latitude: Double,
    ) = flow {
        try {
            val result = forecastApi.getFullForecast(
                longitude = longitude.toString(),
                latitude = latitude.toString()
            )
            emit(TypedResult.Ok(result.toForecast()))
        } catch (e: IOException) {
            emit(TypedResult.Err())
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun saveCity(city: SavedCity) {
        cityDao.save(city)
    }

    override fun checkSaved(id: String): Flow<SavedState> = cityDao.get(id).map {
        if (it != null) SavedState.SAVED else SavedState.NOT_SAVED
    }

}

