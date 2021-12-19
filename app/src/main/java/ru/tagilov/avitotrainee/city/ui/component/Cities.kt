package ru.tagilov.avitotrainee.city.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.tagilov.avitotrainee.CityModel
import ru.tagilov.avitotrainee.city.ui.entity.City
import ru.tagilov.avitotrainee.forecast.ui.screen.Destination
import ru.tagilov.avitotrainee.theme.AvitoTheme
import ru.tagilov.avitotrainee.util.navigateWithParcelable
import ru.tagilov.avitotrainee.util.shimmerContent

@Composable
private fun CityItem(
    modifier: Modifier = Modifier,
    city: City,
    navController: NavController,
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 2.dp)
            .clickable {
                navController.navigateWithParcelable(
                    route = Destination.Forecast.route,
                    key = Destination.Forecast.key_city,
                    parcelable = CityModel(city.name, city.lat, city.lon)
                ) {
                    launchSingleTop = true
                    popUpTo(Destination.Forecast.route) { inclusive = true }
                }
            }
    ) {
        Row(
            modifier = modifier
                .background(
                    color = MaterialTheme.colors.background,
                    shape = MaterialTheme.shapes.medium
                )
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = city.name,
                style = MaterialTheme.typography.h2,
                color = MaterialTheme.colors.secondary,
                modifier = Modifier
                    .weight(1f)
            )
            Box(
                modifier = Modifier
                    .size(10.dp)
            )
            Text(
                text = city.countryCode,
                style = MaterialTheme.typography.h2,
                color = MaterialTheme.colors.onBackground
            )
        }
    }
}

@Composable
private fun CityShimmer(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 2.dp)
            .background(
                color = MaterialTheme.colors.background,
                shape = MaterialTheme.shapes.medium
            )
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "a".repeat(SKELETON_CITY_NAME_SIZE),
            style = MaterialTheme.typography.h2,
            color = MaterialTheme.colors.secondary,
            modifier = Modifier
                .shimmerContent(true)
        )
        Box(
            modifier = Modifier
                .size(10.dp)
        )
        Text(
            text = "a".repeat(SKELETON_COUNTRY_CODE_SIZE),
            style = MaterialTheme.typography.h2,
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier
                .shimmerContent(true)
        )
    }
}


@Composable
fun Cities(
    modifier: Modifier = Modifier,
    cities: List<City>?,
    navController: NavController,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxHeight()
    ) {
        if (cities != null) {
            items(cities.size) {
                CityItem(city = cities[it], navController = navController)
            }
        } else {
            items(3) {
                CityShimmer()
            }
        }
    }
}

//@Preview
//@Composable
//fun CitiesPreview() {
//    AvitoTheme {
//        Cities(
//            cities = listOf(
//                City(
//                    name = "Ну вообще ну очень какой-то длинный город",
//                    lat = 10.0,
//                    lon = 10.0,
//                    countryCode = "RU"
//                ),
//                City(name = "Москва", lat = 10.0, lon = 10.0, countryCode = "RU"),
//                City(name = "Москва", lat = 10.0, lon = 10.0, countryCode = "RU"),
//            )
//        )
//    }
//}
//
//@Preview
//@Composable
//private fun CityItemPreview() {
//    AvitoTheme {
//        Column {
//            CityItem(
//                city = City(
//                    "МоскваМоскваМоскваМоскваМоскваМоскваМоскваМоскваМосква",
//                    1.0,
//                    1.0,
//                    "RU"
//                )
//            )
//            CityItem(
//                city = City(
//                    "Москва",
//                    1.0,
//                    1.0,
//                    "RU"
//                )
//            )
//            CityShimmer()
//        }
//    }
//}

private const val SKELETON_CITY_NAME_SIZE = 7
private const val SKELETON_COUNTRY_CODE_SIZE = 3

