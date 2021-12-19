package ru.tagilov.avitotrainee.city.data

import retrofit2.create
import ru.tagilov.avitotrainee.Networking.forecastRetrofit

object CityNetworking {
    val cityApi: CityApi
        get() = forecastRetrofit.create()
}