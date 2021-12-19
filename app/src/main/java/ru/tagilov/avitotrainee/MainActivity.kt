package ru.tagilov.avitotrainee

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import kotlinx.coroutines.FlowPreview
import ru.tagilov.avitotrainee.city.ui.screen.City
import ru.tagilov.avitotrainee.forecast.ui.screen.Destination
import ru.tagilov.avitotrainee.forecast.ui.screen.Forecast
import ru.tagilov.avitotrainee.theme.AvitoTheme
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
                        val city: CityModel? = it.arguments?.getParcelable(Destination.Forecast.key_city)
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

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AvitoTheme {
        Greeting("Android")
    }
}