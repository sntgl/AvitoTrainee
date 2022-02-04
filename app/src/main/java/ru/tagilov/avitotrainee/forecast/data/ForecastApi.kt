package ru.tagilov.avitotrainee.forecast.data

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query
import ru.tagilov.avitotrainee.forecast.data.entity.OneCallResponse
import ru.tagilov.avitotrainee.forecast.data.entity.ResponseCityName

interface ForecastApi {
    @GET("geo/1.0/reverse")
    fun getCityNameRx(
        @Query("lon") longitude: String,
        @Query("lat") latitude: String,
    ): Single<List<ResponseCityName>>

    @GET("data/2.5/onecall?exclude=minutely&lang=ru&units=metric")
    fun getFullForecastRx(
        @Query("lon") longitude: String,
        @Query("lat") latitude: String,
    ): Single<OneCallResponse>
}