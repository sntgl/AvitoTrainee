package ru.tagilov.avitotrainee.root.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.tagilov.avitotrainee.core.di.AppScope
import ru.tagilov.avitotrainee.root.App
import ru.tagilov.avitotrainee.root.MainActivity

@Component(modules = [AppModule::class])
@AppScope
interface AppComponent :
        BaseDependencies,
        CityDependencies,
        ForecastDependencies
{

    fun inject(activity: MainActivity)
    fun inject(app: App)

    @Component.Factory
    interface Factory {
        fun create(
                @BindsInstance context: Context
        ): AppComponent
    }
}
