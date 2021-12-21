package ru.tagilov.avitotrainee.city.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.tagilov.avitotrainee.R
import ru.tagilov.avitotrainee.core.theme.AvitoTheme

@Composable
fun EmptySearch(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.nothing_found),
            style = MaterialTheme.typography.subtitle2,
            color = MaterialTheme.colors.secondary,
        )
        Text(
            text = stringResource(id = R.string.try_to_correct),
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.secondaryVariant,
        )

    }
}

@Composable
fun CityLoadError(
    modifier: Modifier = Modifier,
    onRetry: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.error_title),
            style = MaterialTheme.typography.subtitle2,
            color = MaterialTheme.colors.secondary,
        )
        Text(
            text = stringResource(id = R.string.connection_error_description),
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.secondaryVariant,
        )
        Text(
            text = stringResource(id = R.string.try_again),
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.primary,
            modifier = Modifier
                .clickable{ onRetry() }
        )

    }
}

@Preview
@Composable
fun EmptySearchPreview() {
    AvitoTheme {
        CityLoadError(onRetry = {})
    }
}