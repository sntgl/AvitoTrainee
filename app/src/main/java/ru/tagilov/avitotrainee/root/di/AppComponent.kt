package ru.tagilov.avitotrainee.root.di

import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import coil.annotation.ExperimentalCoilApi
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.FlowPreview
import ru.tagilov.avitotrainee.root.App
import ru.tagilov.avitotrainee.root.MainActivity

@Component(modules = [AppModule::class])
@AppScope
interface AppComponent :
        BaseDependencies,
        CityDependencies,
        ForecastDependencies
{

    @ExperimentalCoilApi
    @FlowPreview
    @ExperimentalMaterialApi
    @ExperimentalAnimationApi
    fun inject(activity: MainActivity)

    fun inject(app: App)

    @Component.Factory
    interface Factory {
        fun create(
                @BindsInstance context: Context
        ): AppComponent
    }
}
