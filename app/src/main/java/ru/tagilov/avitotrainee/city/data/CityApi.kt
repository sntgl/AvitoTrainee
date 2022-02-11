package ru.tagilov.avitotrainee.city.data

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query
import ru.tagilov.avitotrainee.city.data.entity.ResponseCity

interface CityApi {
    @GET("geo/1.0/direct")
    fun getCitiesRx(
        @Query("q") q: String,
    ): Single<List<ResponseCity>>
}
