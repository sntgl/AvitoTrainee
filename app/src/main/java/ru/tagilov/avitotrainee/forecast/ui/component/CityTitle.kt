package ru.tagilov.avitotrainee.forecast.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.tagilov.avitotrainee.core.routing.CityParcelable
import ru.tagilov.avitotrainee.core.theme.AvitoTheme
import ru.tagilov.avitotrainee.core.util.shimmerRound

@Composable
fun CityTitle(
    modifier: Modifier = Modifier,
    city: CityParcelable?
) {
    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colors.background)
            .height(60.dp)
    ) {
        Row(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colors.background,
                )
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
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
        Box(
            modifier = Modifier
                .height(1.dp).fillMaxWidth()
                .background(color = MaterialTheme.colors.secondaryVariant),
        )
    }
}

@Preview
@Composable
private fun CityTitlePreview() {
    AvitoTheme { CityTitle(city = CityParcelable(name = "Москва",null, 0.0, 0.0)) }
}

private const val SKELETON_CITY_SIZE = 7
