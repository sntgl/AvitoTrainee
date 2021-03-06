package ru.tagilov.avitotrainee.forecast.data

import retrofit2.http.GET
import retrofit2.http.Query
import ru.tagilov.avitotrainee.forecast.data.entity.OneCallResponse
import ru.tagilov.avitotrainee.forecast.data.entity.ResponseCityName

interface ForecastApi {
    @GET("geo/1.0/reverse")
    suspend fun getCityName(
        @Query("lon") longitude: String,
        @Query("lat") latitude: String,
    ): List<ResponseCityName>

    @GET("data/2.5/onecall?exclude=minutely&lang=ru&units=metric")
    suspend fun getFullForecast(
        @Query("lon") longitude: String,
        @Query("lat") latitude: String,
    ): OneCallResponse
}