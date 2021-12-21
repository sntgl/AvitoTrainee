package ru.tagilov.avitotrainee.forecast.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.tagilov.avitotrainee.R
import ru.tagilov.avitotrainee.forecast.ui.entity.CurrentForecast
import ru.tagilov.avitotrainee.core.theme.AvitoTheme
import ru.tagilov.avitotrainee.core.util.shimmerRound


// умышленно нет бекграунда
@Composable
fun Current(
    modifier: Modifier = Modifier,
    forecast: CurrentForecast?,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(color = MaterialTheme.colors.surface),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Row(modifier = Modifier.padding(vertical = 4.dp)) {
            //вроде и костыль, но так точно по центру (у каждой буквы своя длина)
            if (forecast != null)
                Text(
                    text = stringResource(id = R.string.celsius),
                    color = MaterialTheme.colors.surface,
                    style = MaterialTheme.typography.h1,
                )
            Text(
                // температура
                text = if (forecast != null)
                    stringResource(id = R.string.celsius_placeholder).format(forecast.currentTemp)
                else
                    "a".repeat(SKELETON_TEMP_SIZE),
                //                .padStart(3), //надо же, вот только не так как ожидалось работает(
                modifier = Modifier
                    .shimmerRound(forecast == null),
                style = MaterialTheme.typography.h1,
                color = MaterialTheme.colors.secondary,
            )
        }
        Text(
            // описание
            text = forecast?.currentDescription ?: "a".repeat(SKELETON_DESCRIPTION_SIZE),
            modifier = Modifier
                .shimmerRound(forecast == null),
            style = MaterialTheme.typography.h3,
            color = MaterialTheme.colors.secondary,
        )
        Text(
            // чувствуется как
            text = if (forecast != null)
                stringResource(id = R.string.feels_like).format(forecast.feelsLike)
            else
                "a".repeat(SKELETON_FEELS_LIKE_SIZE),
            modifier = Modifier
                .shimmerRound(forecast == null),
            style = MaterialTheme.typography.h3,
            color = MaterialTheme.colors.secondary,
        )
        Text(
            // макс мин
            text = if (forecast != null)
                stringResource(id = R.string.max_min).format(forecast.maxTemp, forecast.minTemp)
            else
                "a".repeat(SKELETON_MAX_MIN_SIZE),
            modifier = Modifier
                .shimmerRound(forecast == null),
            style = MaterialTheme.typography.h3,
            color = MaterialTheme.colors.secondary,
        )
    }
}

@Preview
@Composable
private fun CurrentPreview() {
    val forecast = CurrentForecast(
        icon = "10d",
        minTemp = -1,
        maxTemp = 3,
        wind = 1,
        humidity = 99,
        feelsLike = -4,
        sunset = "8:42",
        sunrise = "17:20",
        currentTemp = 0,
        currentDescription = "Снег"
    )
    Column {
        AvitoTheme(darkTheme = false) {
            Column(Modifier.background(color = MaterialTheme.colors.background)) {
                Current(forecast = forecast)
                Current(forecast = null)
            }
        }
        AvitoTheme(darkTheme = true) {
            Column(Modifier.background(color = MaterialTheme.colors.background)) {
                Current(forecast = forecast)
                Current(forecast = null)
            }
        }
    }
}

private const val SKELETON_TEMP_SIZE = 2
private const val SKELETON_FEELS_LIKE_SIZE = 15
private const val SKELETON_DESCRIPTION_SIZE = 8
private const val SKELETON_MAX_MIN_SIZE = 12
