package ru.tagilov.avitotrainee.root.di

import retrofit2.Retrofit
import ru.tagilov.avitotrainee.core.db.AppDatabase
import ru.tagilov.avitotrainee.core.di.Forecast
import ru.tagilov.avitotrainee.core.di.Location

interface BaseDependencies {
    val database: AppDatabase
}

interface CityDependencies: BaseDependencies {
    @get:Forecast
    val forecastRetrofit: Retrofit
}

interface ForecastDependencies: BaseDependencies {
    @get:Forecast val forecastRetrofit: Retrofit
    @get:Location val locationRetrofit: Retrofit
}