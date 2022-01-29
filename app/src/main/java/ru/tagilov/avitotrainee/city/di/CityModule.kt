package ru.tagilov.avitotrainee.city.di

import dagger.Binds
import dagger.Component
import dagger.Module
import kotlinx.coroutines.FlowPreview
import ru.tagilov.avitotrainee.city.data.CityRepository
import ru.tagilov.avitotrainee.city.data.CityRepositoryImpl
import ru.tagilov.avitotrainee.city.ui.viewmodel.CityViewModel
import ru.tagilov.avitotrainee.root.di.CityDependencies
import javax.inject.Scope

@Module(includes = [CityBinds::class])
class CityModule

@Module
interface CityBinds {
    @Binds
    fun bindCityRepository(repository: CityRepositoryImpl): CityRepository
}
