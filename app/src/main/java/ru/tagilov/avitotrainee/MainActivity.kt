package ru.tagilov.avitotrainee

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.annotation.ExperimentalCoilApi
import ru.tagilov.avitotrainee.screen.Destination
import ru.tagilov.avitotrainee.screen.Forecast
import ru.tagilov.avitotrainee.screen.LocationPermission
import ru.tagilov.avitotrainee.ui.theme.AvitoTheme

@ExperimentalAnimationApi
@ExperimentalCoilApi
class MainActivity : ComponentActivity() {
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
                        arguments = listOf(
                            navArgument(Destination.Forecast.granted) { type = NavType.BoolType }
                        ),
                    ) {
//                            locationGranted = it.arguments?.getBoolean(Destination.Forecast.granted),
                        Forecast(
                            navController = navController,
                            city = null
                        )
                    }
                    composable(Destination.Permission.route) {
                        LocationPermission(navController)
                    }
//                    composable("profile") { Profile(/*...*/) }
//                    composable("friendslist") { FriendsList(/*...*/) }
                    /*...*/
                }
//                Column {
//                    CityTitle(city = "Москва")
//                    LazyColumn(
//                        modifier = Modifier
//                            .background(color = MaterialTheme.colors.background)
//                    ) {
//                        item { CurrentPreview() }
//                        item { HourlyPreview() }
//                        item { DailyPreview() }
//                    }
//                }
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