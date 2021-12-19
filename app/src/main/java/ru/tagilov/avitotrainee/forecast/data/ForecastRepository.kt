package ru.tagilov.avitotrainee.forecast.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.tagilov.avitotrainee.forecast.ui.entity.fromResponse
import ru.tagilov.avitotrainee.forecast.ui.entity.Forecast
import java.io.IOException

class ForecastRepository {
    suspend fun getCityName(
        longitude: Double,
        latitude: Double,
    ): Flow<String?> = flow {
        try {
            emit(
                ForecastNetworking.forecastApi.getCityName(
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


    suspend fun getWeather(
        longitude: Double,
        latitude: Double,
    ): Flow<Forecast?> = flow {
        try {
            emit(
                ForecastNetworking.forecastApi.getFullForecast(
                    longitude = longitude.toString(),
                    latitude = latitude.toString()
                )
            )
        } catch (e: IOException) {
            emit(null)
        }
    }.map{
        if (it != null)
            Forecast.fromResponse(it)
        else
            null
    }.flowOn(Dispatchers.IO)

}