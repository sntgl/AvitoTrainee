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
import ru.tagilov.avitotrainee.forecast.ui.component.CityTitle
import ru.tagilov.avitotrainee.forecast.ui.component.ConnectionError
import ru.tagilov.avitotrainee.forecast.ui.component.Current
import ru.tagilov.avitotrainee.forecast.ui.component.Daily
import ru.tagilov.avitotrainee.forecast.ui.component.Hourly
import ru.tagilov.avitotrainee.forecast.ui.component.LocationError
import ru.tagilov.avitotrainee.forecast.ui.component.NavBar
import ru.tagilov.avitotrainee.forecast.ui.component.SaveSuggestion
import ru.tagilov.avitotrainee.forecast.ui.entity.City
import ru.tagilov.avitotrainee.forecast.ui.entity.Forecast
import ru.tagilov.avitotrainee.forecast.ui.entity.PermissionState
import ru.tagilov.avitotrainee.forecast.ui.viewmodel.ForecastViewModel
import timber.log.Timber

@Composable
fun Forecast(
    navController: NavController,
    city: CityParcelable? = null,
    vm: ForecastViewModel
) {
    LaunchedEffect(key1 = Unit) {
        if (city != null)
            vm.setCity(city)
        else
            vm.setEmptyCity()
    }

    val permissionState = vm.permissionStateObservable
        .subscribeAsState(initial = PermissionState.None)
    val cityState = vm.cityObservable
        .subscribeAsState(initial = City.Empty)
    val forecastState = vm.forecastObservable
        .subscribeAsState(initial = Forecast.Empty)
    val screenState = vm.screenStateObservable
        .subscribeAsState(initial = ForecastState.None)
    val saved = vm.savedStateObservable
        .subscribeAsState(initial = SavedState.NONE)
    val context = LocalContext.current

//     локация
    val sendLocation = {
        LocationServices
            .getFusedLocationProviderClient(context)
            .lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    vm.setGPSLocation(
                        long = location.longitude,
                        lat = location.latitude,
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
    val snackbarEvents = vm.showSnackBarObservable
        .subscribeAsState(initial = SnackbarEvent.Empty)
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
                        val cityValue = cityState.value
                        CityTitle(city = when (cityValue) {
                            is City.Empty -> null
                            is City.Full -> cityValue
                            is City.WithGeo -> null
                        })
                        Timber.d("state is ${screenState.value}")
                        SwipeRefresh(
                            state =
                            rememberSwipeRefreshState(
                                screenState.value is ForecastState.Loading
//                                        && forecastState.value !is Forecast.Empty
                            ),
                            onRefresh = { vm.refresh() },
                        ) {
                            LazyColumn(
                                modifier = Modifier
                                    .background(color = MaterialTheme.colors.surface)
                                    .padding(horizontal = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                val forecastValue = forecastState.value
                                val forecast: Forecast.Data? = if (forecastValue is Forecast.Data)
                                    forecastValue
                                else
                                    null
                                item { Current(forecast = forecast?.current) }
                                item {
                                    AnimatedVisibility(
                                        visible = saved.value == SavedState.NOT_SAVED
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
            isLocation = city == null,
            isSaved = saved.value == SavedState.SAVED,
            onSave = { vm.save() }
        )
    }
}
