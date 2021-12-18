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
                Networking.forecastApi.getCityName(
                    longitude = longitude.toString(),
                    latitude = latitude.toString()
                )
            )
        } catch (e: IOException) {
            emit(null)
        }
    }.map{
        if (it != null)
            it[0].local_names.ru
        else
            null
    }.flowOn(Dispatchers.IO)


    suspend fun getWeather(
        longitude: Double,
        latitude: Double,
    ): Flow<Forecast?> = flow {
        try {
            emit(
                Networking.forecastApi.getFullForecast(
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