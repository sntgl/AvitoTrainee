package ru.tagilov.avitotrainee.forecast.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.tagilov.avitotrainee.R
import ru.tagilov.avitotrainee.forecast.ui.theme.AvitoTheme

@Composable
fun LocationError(
    onRetry: () -> Unit,
) {
    Error(
        onRetry = onRetry,
        description = stringResource(id = R.string.gps_off_description),
        icon = painterResource(id = R.drawable.gps_off),
    )
}

@Composable
fun ConnectionError(
    onRetry: () -> Unit,
) {
    Error(
        onRetry = onRetry,
        description = stringResource(id = R.string.connection_error_description),
        icon = painterResource(id = R.drawable.cell_tower),
    )
}

@Composable
private fun Error(
    onRetry: () -> Unit,
    description: String,
    icon: Painter,
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            painter = icon,
            contentDescription = "connection error icon",
            modifier = Modifier
                .padding(24.dp)
                .size(72.dp),
            tint = MaterialTheme.colors.secondary
        )
        Text(
            text = stringResource(id = R.string.error_title),
            style = MaterialTheme.typography.subtitle2,
            color = MaterialTheme.colors.secondary,
            modifier = Modifier
                .padding(8.dp)
        )
        Text(
            text = description,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.secondaryVariant,
            modifier = Modifier
                .padding(8.dp)
        )
        Text(
            text = stringResource(id = R.string.try_again),
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.primary,
            modifier = Modifier
                .clickable { onRetry() }
                .padding(8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GeoErrorPreview() {
    AvitoTheme {
        Column {
            ConnectionError {}
            LocationError {}
        }
    }
}
