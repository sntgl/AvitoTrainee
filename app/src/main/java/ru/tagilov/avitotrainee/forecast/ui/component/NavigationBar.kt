package ru.tagilov.avitotrainee.forecast.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ru.tagilov.avitotrainee.theme.AvitoTheme
import ru.tagilov.avitotrainee.R
import ru.tagilov.avitotrainee.forecast.ui.screen.Destination


@Composable
fun NavBar(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(color = MaterialTheme.colors.secondaryVariant),
        )
        Row(
            modifier = Modifier
                .background(color = MaterialTheme.colors.background)
                .height(60.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.arrow_gps),
                contentDescription = "gps arrow",
                tint = MaterialTheme.colors.secondary,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { /* TODO */ }
            )
            Icon(
                painter = painterResource(id = R.drawable.list),
                contentDescription = "list",
                tint = MaterialTheme.colors.secondary,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { /* TODO */ },
            )
        }
    }
}

@Preview
@Composable
fun NavBarPreview() {
//    AvitoTheme {
//        NavBar(rememberNavController(navigators = ))
//    }
}