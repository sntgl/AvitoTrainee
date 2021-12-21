package ru.tagilov.avitotrainee.forecast.ui.screen

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import ru.tagilov.avitotrainee.R
import ru.tagilov.avitotrainee.core.SnackBarMessage
import ru.tagilov.avitotrainee.core.routing.CityParcelable
import ru.tagilov.avitotrainee.forecast.ui.component.*
import ru.tagilov.avitotrainee.forecast.ui.entity.PermissionState
import ru.tagilov.avitotrainee.forecast.ui.viewmodel.ForecastViewModel

@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
fun Forecast(
    navController: NavController,
    city: CityParcelable? = null
) {
    val vm: ForecastViewModel = viewModel()
    LaunchedEffect(key1 = Unit) {
        vm.configure(city = city)
    }

    val permissionState = remember { vm.permissionStateFlow }.collectAsState()
    val cityState = remember { vm.cityFlow }.collectAsState()
    val forecastState = remember { vm.forecastFlow }.collectAsState()
    val isRefreshing = remember { vm.isRefreshingFlow }.collectAsState()
    val screenState = remember { vm.stateFlow }.collectAsState()
    val context = LocalContext.current
    val saved = remember { vm.savedCityFlow }.collectAsState()
    val isLocation = remember { vm.isLocationFlow }.collectAsState()
    //локация
    val sendLocation = {
        LocationServices
            .getFusedLocationProviderClient(context)
            .lastLocation
            .addOnSuccessListener { location ->
                if (location != null)
                    vm.setLocation(
                        long = location.longitude,
                        lat = location.latitude,
                        fromApi = false
                    )
                else
                    vm.newPermissionState(PermissionState.Denied)
            }
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            vm.newPermissionState(PermissionState.Granted)
            sendLocation()
        } else
            vm.newPermissionState(PermissionState.Denied)
    }
    SideEffect {
        if (permissionState.value == PermissionState.Required) {
            vm.newPermissionState(PermissionState.Waiting)
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                vm.newPermissionState(PermissionState.Granted)
                sendLocation()
            } else
                permissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }
//    логика снекбара
    val snackbarState = remember { SnackbarHostState() }
    val snackbarEvents = remember { vm.showSnackBarEvent }.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val unableUpdateMessage = stringResource(id = R.string.unable_to_update)
    val unableSaveMessage = stringResource(id = R.string.unable_to_save)
    LaunchedEffect(key1 = snackbarEvents.value) {
        val event = snackbarEvents.value
        if (event != null)
            coroutineScope.launch {
                snackbarState.showSnackbar(
                    message = when (event.state) {
                        SnackBarMessage.UNABLE_LOAD -> unableUpdateMessage
                        SnackBarMessage.UNABLE_SAVE -> unableSaveMessage
                    }
                )
            }
    }
//    непосредственно верстка

    Column {

        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colors.surface)
                .weight(1f),
        ) {
            Scaffold(
                snackbarHost = {
                    SnackbarHost(
                        hostState = snackbarState,
                    ) {
                        Snackbar(
                            contentColor = MaterialTheme.colors.secondary,
                            backgroundColor = MaterialTheme.colors.error,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier
                                .padding(12.dp)
                        ) {
                            Text(it.message)
                        }
                    }
                }
            ) {
                Column(
                    modifier = Modifier
                ) {
                    if (screenState.value == ForecastState.Content
                        || screenState.value == ForecastState.Loading
                    ) {
                        CityTitle(city = cityState.value)
                        SwipeRefresh(
                            state = rememberSwipeRefreshState(isRefreshing.value),
                            onRefresh = { vm.refresh() },
                        ) {
                            LazyColumn(
                                modifier = Modifier
                                    .background(color = MaterialTheme.colors.surface)
                                    .padding(horizontal = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                item { Current(forecast = forecastState.value?.current) }
                                item { Hourly(forecastList = forecastState.value?.hourly) }
                                item { Daily(forecastList = forecastState.value?.daily) }
                                item {
                                    Text(
                                        text = stringResource(id = R.string.my_tag),
                                        style = MaterialTheme.typography.body2,
                                        color = MaterialTheme.colors.secondaryVariant,
                                        modifier = Modifier
                                            .padding(top = 8.dp, bottom = 20.dp)
                                            .fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    } else if (screenState.value == ForecastState.ErrorState.Connection) {
                        ConnectionError { vm.refresh() }
                    } else if (screenState.value == ForecastState.ErrorState.Location) {
                        LocationError { vm.refresh() }
                    }
                }
            }
        }
        NavBar(
            navController = navController,
            isLocation = isLocation.value,
            isSaved = saved.value == SavedState.SAVED,
            onSave = { vm.save() }
        )
    }
}


@Preview
@Composable
fun ForecastPreview() {

}