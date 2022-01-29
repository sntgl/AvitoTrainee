package ru.tagilov.avitotrainee.city.di

import dagger.Binds
import dagger.Module
import ru.tagilov.avitotrainee.city.data.CityRepository
import ru.tagilov.avitotrainee.city.data.CityRepositoryImpl

@Module(includes = [CityBinds::class])
class CityModule

@Module
interface CityBinds {
    @Binds
    fun bindCityRepository(repository: CityRepositoryImpl): CityRepository
}
