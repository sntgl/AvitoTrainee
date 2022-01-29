package ru.tagilov.avitotrainee.root.di

import retrofit2.Retrofit
import ru.tagilov.avitotrainee.city.data.CityApi
import ru.tagilov.avitotrainee.core.db.AppDatabase
import ru.tagilov.avitotrainee.forecast.data.ForecastApi
import ru.tagilov.avitotrainee.forecast.data.LocationApi
import javax.inject.Named

interface BaseDependencies {
    @Forecast
    val database: AppDatabase

    @Forecast
    val forecastRetrofit: Retrofit
}


interface CityDependencies: BaseDependencies