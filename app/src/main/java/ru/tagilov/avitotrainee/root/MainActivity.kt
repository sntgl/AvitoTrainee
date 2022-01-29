package ru.tagilov.avitotrainee.root

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import kotlinx.coroutines.FlowPreview
import ru.tagilov.avitotrainee.city.di.DaggerCityComponent
import ru.tagilov.avitotrainee.city.ui.screen.City
import ru.tagilov.avitotrainee.core.db.AppDatabase
import ru.tagilov.avitotrainee.core.routing.CityParcelable
import ru.tagilov.avitotrainee.core.routing.Destination
import ru.tagilov.avitotrainee.forecast.ui.screen.Forecast
import ru.tagilov.avitotrainee.core.theme.AvitoTheme
import ru.tagilov.avitotrainee.core.util.daggerViewModel
import timber.log.Timber
import javax.inject.Inject

@ExperimentalAnimationApi
@ExperimentalCoilApi
@ExperimentalMaterialApi
@FlowPreview
class MainActivity: ComponentActivity() {

    @Inject
    lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        Timber.d("citydao - ${database.cityDao()}")
        setContent {
            AvitoTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Destination.Forecast.route
                ) {
                    composable(
                        route = Destination.Forecast.route,
                    ) {
                        val city: CityParcelable? =
                            it.arguments?.getParcelable(Destination.Forecast.KEY_CITY)
                        Timber.d("City = $city")
                        Forecast(
                            navController = navController,
                            city = city
                        )
                    }
                    composable(
                        route = Destination.City.route,
                    ) {

                        val component = DaggerCityComponent.builder().deps(appComponent).build()
                        City(
                            navController = navController,
                            vm = daggerViewModel {
                                component.getViewModel()
                            }
                        )
                    }
                }
            }
        }
    }
}