package ru.tagilov.avitotrainee.city.di

import dagger.Component
import ru.tagilov.avitotrainee.city.ui.viewmodel.CityViewModel
import ru.tagilov.avitotrainee.core.di.ScreenScope
import ru.tagilov.avitotrainee.root.di.CityDependencies

@ScreenScope
@Component(
        modules = [CityModule::class],
        dependencies = [CityDependencies::class]
)
interface CityComponent {

    @ScreenScope
    fun getViewModel(): CityViewModel

    @Component.Factory
    interface Factory {
        fun create(dependencies: CityDependencies): CityComponent
    }
}