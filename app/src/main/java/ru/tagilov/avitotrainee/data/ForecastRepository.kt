package ru.tagilov.avitotrainee.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import retrofit2.http.Query
import ru.tagilov.avitotrainee.BuildConfig
import ru.tagilov.avitotrainee.DomainLocation
import ru.tagilov.avitotrainee.fromResponse
import ru.tagilov.avitotrainee.ui.entity.Forecast
import ru.tagilov.avitotrainee.ui.entity.fromResponse
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