package ru.tagilov.avitotrainee.city.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import ru.tagilov.avitotrainee.R
import ru.tagilov.avitotrainee.city.ui.entity.CityModel
import ru.tagilov.avitotrainee.core.routing.CityParcelable
import ru.tagilov.avitotrainee.core.util.navigateWithParcelable
import ru.tagilov.avitotrainee.core.routing.Destination
import timber.log.Timber

@ExperimentalMaterialApi
@Composable
fun CityDismissible(
    city: CityModel,
    onDismiss: (CityModel) -> Unit,
    navController: NavController,
) {
    rememberUpdatedState(newValue = city)

    lateinit var dismissState: DismissState
    val isDismissed = remember { mutableStateOf(false) }
    key(city.id) {
        dismissState = rememberDismissState(
            confirmStateChange = {
                isDismissed.value = it == DismissValue.DismissedToStart
                true
            }
        )
    }
    SwipeToDismiss(
        state = dismissState,
        modifier = Modifier.padding(vertical = 4.dp),
        directions = setOf(DismissDirection.EndToStart),
        dismissThresholds = { direction ->
            FractionalThreshold(if (direction == DismissDirection.StartToEnd) 0.15f else 0.3f)
        },
        background = {
            dismissState.dismissDirection ?: return@SwipeToDismiss
            if (isDismissed.value) {
                LaunchedEffect(key1 = Unit) {
                    delay(400)
                    onDismiss(city)
                }
            }
            val color by animateColorAsState(
                if (dismissState.targetValue == DismissValue.DismissedToStart)
                    MaterialTheme.colors.error
                else
                    MaterialTheme.colors.background
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "delete",
                    tint = MaterialTheme.colors.secondary
                )
            }
        },
        dismissContent = {
            CityItem(city = city, navController)
        }
    )
}

@ExperimentalMaterialApi
@Composable
fun CurrentCity(
    navController: NavController,
) {
    CityItemBase(cityName = stringResource(id = R.string.current_city), countryCode = "") {
        navController.navigateWithParcelable(
            route = Destination.Forecast.route,
            key = Destination.Forecast.KEY_CITY,
            parcelable = null
        ) {
            launchSingleTop = true
            popUpTo(Destination.Forecast.route) { inclusive = true }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun CityItemBase(
    cityName: String,
    countryCode: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colors.onBackground,
                    shape = MaterialTheme.shapes.medium
                )
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = cityName,
                style = MaterialTheme.typography.h4,
                color = MaterialTheme.colors.secondary,
                modifier = Modifier
                    .weight(1f)
            )
            Box(
                modifier = Modifier
                    .size(10.dp)
            )
            Text(
                text = countryCode,
                style = MaterialTheme.typography.h2,
                color = MaterialTheme.colors.secondaryVariant
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun CityItem(
    city: CityModel,
    navController: NavController,
) {
    CityItemBase(
        cityName = city.name,
        onClick = {
            navController.navigateWithParcelable(
                route = Destination.Forecast.route,
                key = Destination.Forecast.KEY_CITY,
                parcelable = CityParcelable(city.id, city.name, city.countryCode, city.lat, city.lon)
            ) {
                launchSingleTop = true
                popUpTo(Destination.Forecast.route) { inclusive = true }
            }
        },
        countryCode = city.countryCode,
    )
}