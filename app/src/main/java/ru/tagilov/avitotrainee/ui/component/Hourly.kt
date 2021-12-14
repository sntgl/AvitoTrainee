package ru.tagilov.avitotrainee.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import ru.tagilov.avitotrainee.R
import ru.tagilov.avitotrainee.ui.entity.HourlyForecast
import ru.tagilov.avitotrainee.ui.theme.AvitoTheme
import ru.tagilov.avitotrainee.ui.util.shimmerContent

@ExperimentalCoilApi
@Composable
fun Hourly(
    forecastList: List<HourlyForecast>?
) {
    Column(
        modifier = Modifier
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
                painter = painterResource(id = R.drawable.watches),
                contentDescription = "icon",
                tint = MaterialTheme.colors.secondaryVariant,
                modifier = Modifier
                    .size(20.dp)
            )
            Text(
                text = stringResource(id = R.string.hourly_forecast),
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.secondaryVariant,
            )
        }
        LazyRow {
            if (forecastList != null)
                items(forecastList.size) { i ->
                    HourlyItem(forecast = forecastList[i])
                }
            else
                items(10) {
                    HourlyItem(forecast = null)
                }
        }
    }
}

@ExperimentalCoilApi
@Preview
@Composable
fun HourlyPreview() {
    Column {
        AvitoTheme(darkTheme = false) {
            Column {
                val fl = listOf(
                    HourlyForecast("Сейчас", "10d", -8),
                    HourlyForecast("18", "10d", -9),
                    HourlyForecast("19", "11d", -10),
                    HourlyForecast("20", "11d", -12),
                )
                Hourly(fl)
            }
        }
        AvitoTheme(darkTheme = true) {

            val fl = listOf(
                HourlyForecast("Сейчас", "10n", -8),
                HourlyForecast("18", "10n", -9),
                HourlyForecast("19", "11n", -10),
                HourlyForecast("20", "11n", -12),
                HourlyForecast("20", "11n", -12),
                HourlyForecast("20", "11n", -12),
                HourlyForecast("20", "11n", -12),
                HourlyForecast("20", "11n", -12),
                HourlyForecast("20", "11n", -12),
                HourlyForecast("20", "11n", -12),
            )
            Hourly(fl)
            Hourly(null)
        }

    }
}

@ExperimentalCoilApi
@Composable
fun HourlyItem(
    forecast: HourlyForecast?
) {
    Column(
        modifier = Modifier
            .height(130.dp)
            .defaultMinSize(minWidth = 80.dp)
            .padding(2.dp)
            .background(
                color = MaterialTheme.colors.onBackground,
                shape = MaterialTheme.shapes.medium
            )
            .padding(2.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = forecast?.time ?: "a".repeat(SKELETON_TITLE_SIZE),
            style = MaterialTheme.typography.subtitle2,
            color = MaterialTheme.colors.secondary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .shimmerContent(forecast == null)
        )
        WeatherIcon(icon = forecast?.icon ?: "", size = 60.dp)
        Text(
            text = if (forecast != null)
                "${forecast.temperature}°"
            else
                "a".repeat(SKELETON_TEMPERATURE_SIZE),
            style = MaterialTheme.typography.subtitle2,
            color = MaterialTheme.colors.secondary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .shimmerContent(forecast == null)
        )

    }
}

@ExperimentalCoilApi
@Preview
@Composable
fun HourlyItemPreview() {
    Column {
        AvitoTheme(darkTheme = false) {
            Row {
                HourlyItem(HourlyForecast("Сейчас", "10d", -8))
                HourlyItem(HourlyForecast("13", "02d", -30))
            }
        }
        AvitoTheme(darkTheme = true) {
            Row {
                HourlyItem(HourlyForecast("Now", "10n", -8))
                HourlyItem(HourlyForecast("13", "02n", -30))
            }
        }
    }
}

private const val SKELETON_TITLE_SIZE = 5
private const val SKELETON_TEMPERATURE_SIZE = 3