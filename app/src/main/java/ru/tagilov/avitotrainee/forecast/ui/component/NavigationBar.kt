package ru.tagilov.avitotrainee.forecast.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.tagilov.avitotrainee.R
import ru.tagilov.avitotrainee.forecast.ui.screen.Destination
import ru.tagilov.avitotrainee.core.util.navigateWithParcelable
import timber.log.Timber


@ExperimentalAnimationApi
@Composable
fun NavBar(
    modifier: Modifier = Modifier,
    navController: NavController,
    isLocation: Boolean,
    isSaved: Boolean,
    onSave: () -> Unit,
) {
    Column(
        modifier = modifier
    ) {
        Box( //Divider
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(color = MaterialTheme.colors.secondaryVariant),
        )
        Row(
            modifier = Modifier
                .background(color = MaterialTheme.colors.background)
                .height(60.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.arrow_gps),
                contentDescription = "gps arrow",
                tint = if (isLocation)
                    MaterialTheme.colors.primary
                else
                    MaterialTheme.colors.secondary,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(80.dp)
                    .clickable(!isLocation) {
                        navController.navigateWithParcelable(
                            route = Destination.Forecast.route,
                            key = Destination.Forecast.KEY_CITY,
                            parcelable = null
                        ) {
                            launchSingleTop = true
                            popUpTo(Destination.Forecast.route) { inclusive = true }
                        }
                    }
                    .padding(16.dp)
            )
            AnimatedVisibility(visible = !isSaved && !isLocation) {
                Button(
                    onClick = { onSave() },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.onBackground,
                        contentColor = MaterialTheme.colors.secondary
                    ),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text(
                        text= stringResource(id = R.string.save),
                    )
                }
            }
            Icon(
                painter = painterResource(id = R.drawable.list),
                contentDescription = "list",
                tint = MaterialTheme.colors.secondary,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(80.dp)
                    .clickable {
                        navController.navigate(route = Destination.City.route,) {
                            launchSingleTop = true
                        }
                    }
                    .padding(16.dp)
            )
        }
    }
}

