package ru.tagilov.avitotrainee.forecast.di

import dagger.Component
import ru.tagilov.avitotrainee.forecast.ui.viewmodel.ForecastViewModel
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

    @Component.Builder
    interface Builder {
        fun deps(dependencies: ForecastDependencies): Builder
        fun build(): ForecastComponent
    }
}

@Scope
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class ForecastScreenScope

