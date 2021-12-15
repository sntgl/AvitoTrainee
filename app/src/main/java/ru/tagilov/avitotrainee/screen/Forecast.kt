package ru.tagilov.avitotrainee.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import ru.tagilov.avitotrainee.ui.component.CityTitle
import timber.log.Timber

@Composable
fun Forecast(
    navController: NavController,
//    modifier: Modifier = Modifier,
    locationGranted: Boolean?
) {
    CityTitle(city = "Moscow")
    SideEffect {
        Timber.d("granted = $locationGranted")
    }
}

@Preview
@Composable
fun ForecastPreview() {

}