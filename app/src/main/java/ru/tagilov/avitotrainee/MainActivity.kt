package ru.tagilov.avitotrainee

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import kotlinx.coroutines.FlowPreview
import ru.tagilov.avitotrainee.city.ui.screen.City
import ru.tagilov.avitotrainee.core.routing.CityParcelable
import ru.tagilov.avitotrainee.forecast.ui.screen.Destination
import ru.tagilov.avitotrainee.forecast.ui.screen.Forecast
import ru.tagilov.avitotrainee.core.theme.AvitoTheme
import timber.log.Timber

@ExperimentalAnimationApi
@ExperimentalCoilApi
class MainActivity : ComponentActivity() {
    @FlowPreview
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                        City(navController = navController)
                    }
                }
            }
        }
    }
}