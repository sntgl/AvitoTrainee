package ru.tagilov.avitotrainee.forecast.di

import dagger.Component
import ru.tagilov.avitotrainee.city.di.CityComponent
import ru.tagilov.avitotrainee.forecast.ui.viewmodel.ForecastViewModel
import ru.tagilov.avitotrainee.root.di.CityDependencies
import ru.tagilov.avitotrainee.root.di.ForecastDependencies
import javax.inject.Scope

@ForecastScreenScope
@Component(
        modules = [ForecastModule::class],
        dependencies = [ForecastDependencies::class]
)
interface ForecastComponent {

    @ForecastScreenScope
    fun getViewModel(): ForecastViewModel

    @Component.Factory
    interface Factory {
        fun create(dependencies: ForecastDependencies): ForecastComponent
    }
}

@Scope
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class ForecastScreenScope

