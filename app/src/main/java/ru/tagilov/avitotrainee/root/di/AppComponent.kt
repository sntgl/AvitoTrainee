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
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
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

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }
}
