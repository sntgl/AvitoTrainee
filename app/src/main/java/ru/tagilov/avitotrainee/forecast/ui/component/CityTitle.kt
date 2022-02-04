package ru.tagilov.avitotrainee.forecast.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import java.util.*

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
                .height(1.dp)
                .fillMaxWidth()
                .background(color = MaterialTheme.colors.secondaryVariant),
        )
    }
}

@Preview
@Composable
private fun CityTitlePreview() {
    AvitoTheme {
        CityTitle(
            city = CityParcelable(
                id = UUID.randomUUID().toString(),
                name = "Москва",
                countryCode = null,
                latitude = 0.0,
                longitude = 0.0,
            )
        )
    }
}

private const val SKELETON_CITY_SIZE = 7
