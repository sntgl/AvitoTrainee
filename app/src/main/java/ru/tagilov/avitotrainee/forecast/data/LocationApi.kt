package ru.tagilov.avitotrainee.forecast.data

import retrofit2.http.GET
import ru.tagilov.avitotrainee.forecast.data.entity.ResponseLocation

interface LocationApi {
    @GET("ipgeo")
    suspend fun location(): ResponseLocation
}