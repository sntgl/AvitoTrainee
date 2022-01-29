package ru.tagilov.avitotrainee.city.di

import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.FlowPreview
import retrofit2.Retrofit
import retrofit2.create
import ru.tagilov.avitotrainee.city.data.CityApi
import ru.tagilov.avitotrainee.city.data.CityRepository
import ru.tagilov.avitotrainee.city.data.CityRepositoryImpl
import ru.tagilov.avitotrainee.city.ui.viewmodel.CityViewModel
import ru.tagilov.avitotrainee.core.db.AppDatabase
import ru.tagilov.avitotrainee.root.di.BaseDependencies
import ru.tagilov.avitotrainee.root.di.CityDependencies
import ru.tagilov.avitotrainee.root.di.Forecast
import javax.inject.Scope

@Scope
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class CityScreenScope

@CityScreenScope
@Component(
        modules = [CityModule::class],
        dependencies = [CityDependencies::class]
)
interface CityComponent {

    @CityScreenScope
    @FlowPreview
    fun getViewModel(): CityViewModel

    @Component.Builder
    interface Builder {
        fun deps(dependencies: CityDependencies): Builder
        fun build(): CityComponent
    }
}


@Module(includes = [CityBinds::class])
class CityModule

@Module
interface CityBinds {
    @Binds
    fun bindCityRepository(repository: CityRepositoryImpl): CityRepository
}
