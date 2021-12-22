package ru.tagilov.avitotrainee.city.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.tagilov.avitotrainee.city.ui.entity.CityModel
import ru.tagilov.avitotrainee.core.util.shimmerContent


@Composable
private fun CityShimmer(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .background(
                color = MaterialTheme.colors.onBackground,
                shape = MaterialTheme.shapes.medium
            )
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "a".repeat(SKELETON_CITY_NAME_SIZE),
            style = MaterialTheme.typography.h2,
            color = MaterialTheme.colors.secondary,
            modifier = Modifier
                .shimmerContent(true)
        )
        Box(
            modifier = Modifier
                .size(10.dp)
        )
        Text(
            text = "a".repeat(SKELETON_COUNTRY_CODE_SIZE),
            style = MaterialTheme.typography.h2,
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier
                .shimmerContent(true)
        )
    }
}


@ExperimentalMaterialApi
@Composable
fun Cities(
    modifier: Modifier = Modifier,
    cities: List<CityModel>?,
    navController: NavController,
    title: String,
    onDismiss: (CityModel) -> Unit,
    isLocal: Boolean
) {
    //все-таки решил, что тут не нужен свайп рефреш
    LazyColumn(
        modifier = modifier
            .fillMaxHeight()
    ) {
        if (isLocal) {
            item { CurrentCity(navController = navController) }
        }
        if (isLocal && cities?.isEmpty() == true) {
            item { EmptySaved() }
        } else {
            item {
                Text(
                    text = title,
                    style = MaterialTheme.typography.subtitle2,
                    color = MaterialTheme.colors.secondary,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                )
            }
        }
        if (cities != null) {
            items(cities.size) {
                if (isLocal)
                    CityDismissible(
                        city = cities[it],
                        navController = navController,
                        onDismiss = onDismiss
                    )
                else
                    CityItem(
                        city = cities[it],
                        navController = navController
                    )
            }
        } else
            item { CityShimmer() }
    }
}

private const val SKELETON_CITY_NAME_SIZE = 7
private const val SKELETON_COUNTRY_CODE_SIZE = 3

