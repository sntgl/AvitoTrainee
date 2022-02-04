package ru.tagilov.avitotrainee.forecast.ui.screen

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import ru.tagilov.avitotrainee.R
import ru.tagilov.avitotrainee.core.SnackBarMessage
import ru.tagilov.avitotrainee.core.SnackbarEvent
import ru.tagilov.avitotrainee.core.routing.CityParcelable
import ru.tagilov.avitotrainee.core.util.TypedResult
import ru.tagilov.avitotrainee.forecast.ui.component.CityTitle
import ru.tagilov.avitotrainee.forecast.ui.component.ConnectionError
import ru.tagilov.avitotrainee.forecast.ui.component.Current
import ru.tagilov.avitotrainee.forecast.ui.component.Daily
import ru.tagilov.avitotrainee.forecast.ui.component.Hourly
import ru.tagilov.avitotrainee.forecast.ui.component.LocationError
import ru.tagilov.avitotrainee.forecast.ui.component.NavBar
import ru.tagilov.avitotrainee.forecast.ui.component.SaveSuggestion
import ru.tagilov.avitotrainee.forecast.ui.entity.Forecast
import ru.tagilov.avitotrainee.forecast.ui.entity.PermissionState
import ru.tagilov.avitotrainee.forecast.ui.viewmodel.ForecastViewModel

@Composable
fun Forecast(
    navController: NavController,
    city: CityParcelable? = null,
    vm: ForecastViewModel
) {
    LaunchedEffect(key1 = Unit) { vm.configure(city = city) }

    val permissionState = remember { vm.permissionStateObservable }.subscribeAsState(initial = PermissionState.None)
    val cityState = remember { vm.cityObservable }.subscribeAsState(initial = TypedResult.Err())
    val forecastState = remember { vm.forecastObservable }.subscribeAsState(initial = Forecast.Empty())
    val isRefreshing = remember { vm.isRefreshingObservable }.subscribeAsState(initial = false)
    val screenState = remember { vm.screenStateObservable }.subscribeAsState(initial = ForecastState.None)
    val context = LocalContext.current
    val saved = remember { vm.savedCityObservable }.subscribeAsState(initial = SavedState.NONE)
    val isLocation = remember { vm.isGPSLocationObservable }.subscribeAsState(initial = true)

    //локация
    val sendLocation = {
        LocationServices
            .getFusedLocationProviderClient(context)
            .lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    vm.setLocation(
                        long = location.longitude,
                        lat = location.latitude,
                        fromApi = false
                    )
                }
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
    val snackbarEvents = remember {
        vm.showSnackBar
    }.subscribeAsState(initial = SnackbarEvent.Empty())
    val coroutineScope = rememberCoroutineScope()
    val unableUpdateMessage = stringResource(id = R.string.unable_to_update)
    val unableSaveMessage = stringResource(id = R.string.unable_to_save)
    LaunchedEffect(key1 = snackbarEvents.value) {
        val event = snackbarEvents.value
        if (event is SnackbarEvent.Show)
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
                        val cityV = cityState.value
                        CityTitle(city = when (cityV) {
                            is TypedResult.Ok -> cityV.result
                            is TypedResult.Err -> null
                        })
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
                                val fv = forecastState.value
                                val forecast: Forecast.Data? = if (fv is Forecast.Data) fv else null
                                item { Current(forecast = forecast?.current) }
                                item {
                                    AnimatedVisibility(
                                        visible = !isLocation.value &&
                                                saved.value != SavedState.SAVED
                                    ) {
                                        SaveSuggestion { vm.save() }
                                    }
                                }
                                item { Hourly(forecastList = forecast?.hourly) }
                                item { Daily(forecastList = forecast?.daily) }
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
