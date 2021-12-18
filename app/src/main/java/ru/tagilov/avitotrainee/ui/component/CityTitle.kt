package ru.tagilov.avitotrainee.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.tagilov.avitotrainee.City
import ru.tagilov.avitotrainee.ui.theme.AvitoTheme
import ru.tagilov.avitotrainee.ui.util.shimmer
import ru.tagilov.avitotrainee.ui.util.shimmerRound

@Composable
fun CityTitle(
    modifier: Modifier = Modifier,
    city: City?
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colors.background,
            )
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Center,
    ){
        Text(
            // город
            text = if (city?.name != null)
                city.name
            else
                "a".repeat(SKELETON_CITY_SIZE),
            modifier = modifier
                .shimmerRound(city?.name == null),
            style = MaterialTheme.typography.h2,
            color = MaterialTheme.colors.secondary,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun CityTitlePreview() {
    AvitoTheme {
        CityTitle(city = City(name="Москва", 0.0, 0.0))
    }
}

private const val SKELETON_CITY_SIZE = 7
