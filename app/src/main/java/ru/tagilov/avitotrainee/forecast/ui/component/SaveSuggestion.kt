package ru.tagilov.avitotrainee.forecast.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.tagilov.avitotrainee.R
import ru.tagilov.avitotrainee.core.theme.AvitoTheme

@Composable
fun SaveSuggestion(
    modifier: Modifier = Modifier,
    onSave: () -> Unit,
) {
    Column(
        modifier = modifier
            .background(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colors.onError,
            )
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(1f)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = "warning icon",
                    tint = MaterialTheme.colors.secondaryVariant,
                    modifier = Modifier
                        .size(20.dp)
                )
                Text(
                    text = stringResource(id = R.string.city_not_saved),
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.secondaryVariant,
                )
            }
        }
        Row(
            modifier = Modifier
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Box(
                modifier = Modifier
                    .weight(1f)
            ){
            Text(
                text = stringResource(id = R.string.save_city),
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.secondary,
                modifier = Modifier
                    .padding(4.dp)
            )}
            Button(
                onClick = { onSave() },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.onBackground,
                    contentColor = MaterialTheme.colors.secondary
                ),
                shape = MaterialTheme.shapes.large,
                modifier = Modifier
                    .padding(4.dp)
                    .wrapContentSize()
            ) {
                Text(
                    text = stringResource(id = R.string.save),
                )
            }
        }
    }
}

@Preview
@Composable
fun SaveSuggestionPreview() {
    Column {
        AvitoTheme(darkTheme = false) {
            SaveSuggestion{}
        }
        AvitoTheme(darkTheme = true) {
            SaveSuggestion{}
        }
    }
}