package ru.tagilov.avitotrainee.data

import retrofit2.http.GET
import retrofit2.http.Query
import ru.tagilov.avitotrainee.data.entity.ResponseCityName

interface ForecastApi {
    @GET("geo/1.0/reverse")
    suspend fun getCityName(
        @Query("lon") longitude: String,
        @Query("lat") latitude: String,
    ): List<ResponseCityName>
}