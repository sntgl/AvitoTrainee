package ru.tagilov.avitotrainee.forecast.di

import dagger.Component
import ru.tagilov.avitotrainee.core.di.ScreenScope
import ru.tagilov.avitotrainee.forecast.ui.viewmodel.ForecastViewModel
import ru.tagilov.avitotrainee.root.di.ForecastDependencies

@ScreenScope
@Component(
        modules = [ForecastModule::class],
        dependencies = [ForecastDependencies::class]
)
interface ForecastComponent {

    @ScreenScope
    fun getViewModel(): ForecastViewModel

    @Component.Factory
    interface Factory {
        fun create(dependencies: ForecastDependencies): ForecastComponent
    }
}