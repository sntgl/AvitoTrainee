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
