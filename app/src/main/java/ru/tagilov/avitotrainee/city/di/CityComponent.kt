package ru.tagilov.avitotrainee.city.di

import dagger.Component
import kotlinx.coroutines.FlowPreview
import ru.tagilov.avitotrainee.city.ui.viewmodel.CityViewModel
import ru.tagilov.avitotrainee.root.di.CityDependencies
import javax.inject.Scope

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

@Scope
annotation class CityScreenScope