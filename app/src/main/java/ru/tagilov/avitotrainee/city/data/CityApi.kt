package ru.tagilov.avitotrainee.city.data

import retrofit2.http.GET
import retrofit2.http.Query
import ru.tagilov.avitotrainee.city.data.entity.ResponseCity

interface CityApi {
    @GET("geo/1.0/direct")
    suspend fun getCities(
        @Query("q") q: String,
    ): List<ResponseCity>
}
