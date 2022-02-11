package ru.tagilov.avitotrainee.forecast.data

import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET
import ru.tagilov.avitotrainee.forecast.data.entity.ResponseLocation

interface LocationApi {
    @GET("ipgeo")
    fun locationRx(): Single<Response<ResponseLocation>>
}