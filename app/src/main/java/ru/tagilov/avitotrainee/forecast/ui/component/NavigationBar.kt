package ru.tagilov.avitotrainee.forecast.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.tagilov.avitotrainee.CityModel
import ru.tagilov.avitotrainee.R
import ru.tagilov.avitotrainee.forecast.ui.screen.Destination
import ru.tagilov.avitotrainee.util.navigateWithParcelable


@Composable
fun NavBar(
    modifier: Modifier = Modifier,
    navController: NavController,
    isLocation: Boolean,
) {
    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(color = MaterialTheme.colors.secondaryVariant),
        )
        Row(
            modifier = Modifier
                .background(color = MaterialTheme.colors.background)
                .height(60.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier
                    .size(60.dp)
                    .clickable {
                        if (!isLocation)
                            navController.navigateWithParcelable(
                                route = Destination.Forecast.route,
                                key = Destination.Forecast.key_city,
                                parcelable = null
                            ) {
                                launchSingleTop = true
                                popUpTo(Destination.Forecast.route) { inclusive = true }
                            }
                    },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_gps),
                    contentDescription = "gps arrow",
                    tint = if (isLocation)
                        MaterialTheme.colors.primary
                    else
                        MaterialTheme.colors.secondary,
                    modifier = Modifier
                        .size(24.dp)
                )
            }
            Row(
                modifier = Modifier
                    .size(60.dp)
                    .clickable {
//                        navController.navigateWithParcelable(
//                            route = Destination.Forecast.route,
//                            key = Destination.Forecast.key_city,
//                            parcelable = CityModel(null, 50.0, 32.1)
//                        ) {
//                            launchSingleTop = true
//                            popUpTo(Destination.Forecast.route) { inclusive = true }
//                        }
                        navController.navigate(route = Destination.City.route,){
                            launchSingleTop = true
                        }
                    },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.list),
                    contentDescription = "list",
                    tint = MaterialTheme.colors.secondary,
                    modifier = Modifier
                        .size(24.dp)
                        .background(color = MaterialTheme.colors.background, shape = CircleShape),
                )
            }
        }
    }
}



@Preview
@Composable
fun NavBarPreview() {
//    AvitoTheme {
//        NavBar(rememberNavController(navigators = ))
//    }
}