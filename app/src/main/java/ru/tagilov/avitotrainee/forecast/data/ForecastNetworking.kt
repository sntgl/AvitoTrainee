package ru.tagilov.avitotrainee.forecast.data

import retrofit2.create
import ru.tagilov.avitotrainee.Networking.forecastRetrofit
import ru.tagilov.avitotrainee.Networking.locationRetrofit

object ForecastNetworking {

    val forecastApi: ForecastApi
        get() = forecastRetrofit.create()

    val locationApi: LocationApi
        get() = locationRetrofit.create()
}

