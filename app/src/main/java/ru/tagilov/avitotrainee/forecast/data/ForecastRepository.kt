package ru.tagilov.avitotrainee.forecast.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.tagilov.avitotrainee.forecast.data.entity.toForecast
import ru.tagilov.avitotrainee.forecast.ui.entity.Forecast
import java.io.IOException
import javax.inject.Inject

interface ForecastRepository {
    suspend fun getCityName(longitude: Double, latitude: Double, ): Flow<String?>
    suspend fun getWeather(longitude: Double, latitude: Double, ): Flow<Forecast?>
}

class ForecastRepositoryImpl @Inject constructor(
        private val forecastApi: ForecastApi
): ForecastRepository {
    override suspend fun getCityName(
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
    }.map{
        it?.get(0)?.localNames?.ru ?: it?.get(0)?.name
    }.flowOn(Dispatchers.IO)


    override suspend fun getWeather(
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
    }.map{
        if (it != null)
            it.toForecast()
        else
            null
    }.flowOn(Dispatchers.IO)

}