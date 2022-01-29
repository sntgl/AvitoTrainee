package ru.tagilov.avitotrainee.root.di

import ru.tagilov.avitotrainee.city.data.CityApi
import ru.tagilov.avitotrainee.core.db.AppDatabase
import ru.tagilov.avitotrainee.forecast.data.ForecastApi
import ru.tagilov.avitotrainee.forecast.data.LocationApi

interface BaseDependencies {
    val database: AppDatabase
}


interface CityDependencies: BaseDependencies {
    val cityApi: CityApi
}

interface ForecastDependencies: BaseDependencies {
    val forecastApi: ForecastApi
    val locationApi: LocationApi
}