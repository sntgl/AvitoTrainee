package ru.tagilov.avitotrainee.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import ru.tagilov.avitotrainee.data.entity.ResponseLocation

interface LocationApi {
    @GET("ipgeo")
    suspend fun location(): ResponseLocation
}