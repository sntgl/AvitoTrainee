package ru.tagilov.avitotrainee.ui.component

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import ru.tagilov.avitotrainee.R
import ru.tagilov.avitotrainee.ui.entity.DailyForecast
import ru.tagilov.avitotrainee.ui.theme.AvitoTheme
import ru.tagilov.avitotrainee.ui.util.shimmerContent
import java.text.SimpleDateFormat
import java.util.*


@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
fun DailyItem(
    forecast: DailyForecast?
) {
    val isCollapsed = remember { mutableStateOf(true) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colors.onBackground,
                shape = MaterialTheme.shapes.medium
            )
            .clickable {
                if (forecast != null)
                    isCollapsed.value = !isCollapsed.value
            }
            .padding(vertical = 2.dp, horizontal = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = forecast?.day ?: "a".repeat(SKELETON_TITLE_SIZE),
                    style = MaterialTheme.typography.subtitle2,
                    color = MaterialTheme.colors.secondary,
                    modifier = Modifier
                        .shimmerContent(forecast == null)
                        .width(100.dp)
                )
                WeatherIcon(icon = forecast?.icon ?: "", size = 60.dp)
            }
            if (forecast != null) {
                Row {
                    Text(
                        text = "${forecast.minTemp}°",
                        style = MaterialTheme.typography.subtitle2,
                        color = MaterialTheme.colors.secondaryVariant,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .width(40.dp)
                    )
                    Text(
                        text = "${forecast.maxTemp}°",
                        style = MaterialTheme.typography.subtitle2,
                        color = MaterialTheme.colors.secondary,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .width(40.dp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.wind),
                        tint = MaterialTheme.colors.secondaryVariant,
                        contentDescription = "wind icon",
                        modifier = Modifier
                            .padding(8.dp)
                            .size(24.dp)
                    )
                    Text(
                        text = "${forecast.wind} м/с",
                        style = MaterialTheme.typography.subtitle2,
                        color = MaterialTheme.colors.secondaryVariant,
                        modifier = Modifier
                            .width(65.dp)
                    )
                }
            } else {
                Text(
                    text = "a".repeat(SKELETON_CONTENT_SIZE),
                    style = MaterialTheme.typography.subtitle2,
                    modifier = Modifier
                        .shimmerContent(true)
                )
            }
        }

        AnimatedVisibility(visible = !isCollapsed.value) {
            Divider(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                color = MaterialTheme.colors.secondaryVariant
            )
        }

        AnimatedVisibility(visible = !isCollapsed.value) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.humidity).format(forecast?.humidity),
                    style = MaterialTheme.typography.subtitle2,
                    color = MaterialTheme.colors.secondary,
                )
                Text(
                    text = stringResource(id = R.string.feels_like).format(forecast?.feelsLike),
                    style = MaterialTheme.typography.subtitle2,
                    color = MaterialTheme.colors.secondary,
                )
            }
        }
        AnimatedVisibility(visible = !isCollapsed.value) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.sunrise).format(forecast?.sunrise),
                    style = MaterialTheme.typography.subtitle2,
                    color = MaterialTheme.colors.secondary,
                    modifier = Modifier
                )
                Text(
                    text = stringResource(id = R.string.sunset).format(forecast?.sunset),
                    style = MaterialTheme.typography.subtitle2,
                    color = MaterialTheme.colors.secondary,
                    modifier = Modifier
                )
            }
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalCoilApi
@Preview
@Composable
fun DailyItemPreview() {
    Column {
        AvitoTheme(darkTheme = false) {
            DailyItem(
//                forecast = null
                forecast = DailyForecast(
                    day = "Сегодня",
                    icon = "10d",
                    minTemp = -1,
                    maxTemp = 3,
                    wind = 1,
                    humidity = 99,
                    feelsLike = -4,
                    sunset = "8:42",
                    sunrise = "17:20"
                )
            )
            DailyItem(
                forecast = DailyForecast(
                    day = "Среда",
                    icon = "11d",
                    minTemp = -2,
                    maxTemp = -1,
                    wind = 3,
                    humidity = 89,
                    feelsLike = -2,
                    sunset = "17:00",
                    sunrise = "8:40"
                )
            )
            DailyItem(
                forecast = null
            )
        }
        AvitoTheme(darkTheme = true) {
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
fun Daily(
    modifier: Modifier = Modifier,
    forecastList: List<DailyForecast>?,
) {
    Column(
        modifier = modifier
            .background(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colors.background,
            )
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.calendar),
                contentDescription = "icon",
                tint = MaterialTheme.colors.secondaryVariant,
                modifier = Modifier
                    .size(20.dp)
            )
            Text(
                text = stringResource(id = R.string.daily_forecast),
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.secondaryVariant,
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            if (forecastList != null)
                for (forecastItem in forecastList)
                    DailyItem(forecast = forecastItem)
            else
                for (i in 1..3)
                    DailyItem(forecast = null)
        }
    }
}

@SuppressLint("SimpleDateFormat")
@ExperimentalCoilApi
@ExperimentalAnimationApi
@Preview
@Composable
fun DailyPreview() {
    val fl = listOf(
        DailyForecast(
            day = "Сегодня",
            icon = "10d",
            minTemp = -1,
            maxTemp = 3,
            wind = 1,
            humidity = 99,
            feelsLike = -4,
            sunset = "8:42",
            sunrise = "17:20"
        ),
        DailyForecast(
            day = SimpleDateFormat("E").format(Date(0)),
            icon = "11d",
            minTemp = -99,
            maxTemp = -99,
            wind = 15,
            humidity = 89,
            feelsLike = -100,
            sunset = "17:00",
            sunrise = "18:40"
        )
    )
    Column {
        AvitoTheme(darkTheme = false) {
            Column {
                Daily(forecastList = fl)
                Daily(forecastList = null)
            }
        }
        AvitoTheme(darkTheme = true) {
            Column {
                Daily(forecastList = fl)
                Daily(forecastList = null)
            }
        }
    }
}


private const val SKELETON_TITLE_SIZE = 7
private const val SKELETON_CONTENT_SIZE = 15
