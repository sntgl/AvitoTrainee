package ru.tagilov.avitotrainee.root.di

import retrofit2.Retrofit
import ru.tagilov.avitotrainee.core.db.AppDatabase
import ru.tagilov.avitotrainee.core.di.Forecast
import ru.tagilov.avitotrainee.core.di.Location
import ru.tagilov.avitotrainee.forecast.di.SchedulersFactory

interface BaseDependencies {
    val database: AppDatabase
    val schedulers: SchedulersFactory
}

interface CityDependencies: BaseDependencies {
    @get:Forecast
    val forecastRetrofit: Retrofit
}

interface ForecastDependencies: BaseDependencies {
    @get:Forecast val forecastRetrofit: Retrofit
    @get:Location val locationRetrofit: Retrofit
}