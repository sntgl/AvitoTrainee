package ru.tagilov.avitotrainee.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.tagilov.avitotrainee.City
import ru.tagilov.avitotrainee.ui.theme.AvitoTheme
import ru.tagilov.avitotrainee.ui.util.shimmerRound

@Composable
fun CityTitle(
    modifier: Modifier = Modifier,
    city: City?
) {
    Text(
        // город
        text = if (city?.name != null)
            city.name
        else
            "a".repeat(SKELETON_CITY_SIZE),
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.background)
            .padding(vertical = 8.dp)
            .shimmerRound(city?.name == null),
        style = MaterialTheme.typography.h2,
        color = MaterialTheme.colors.secondary,
        textAlign = TextAlign.Center
    )
}

@Preview
@Composable
fun CityTitlePreview() {
    AvitoTheme {
        CityTitle(city = City(name="Москва", 0.0, 0.0))
    }
}

private const val SKELETON_CITY_SIZE = 7
