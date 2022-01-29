package ru.tagilov.avitotrainee.forecast.di

import dagger.Binds
import dagger.Module
import ru.tagilov.avitotrainee.forecast.data.ForecastRepository
import ru.tagilov.avitotrainee.forecast.data.ForecastRepositoryImpl
import ru.tagilov.avitotrainee.forecast.data.LocationRepository
import ru.tagilov.avitotrainee.forecast.data.LocationRepositoryImpl

@Module(includes = [ForecastBinds::class])
class ForecastModule

@Module
interface ForecastBinds {
    @Binds
    fun bindForecastRepository(repository: ForecastRepositoryImpl): ForecastRepository

    @Binds
    fun bindLocationRepository(repository: LocationRepositoryImpl): LocationRepository
}
