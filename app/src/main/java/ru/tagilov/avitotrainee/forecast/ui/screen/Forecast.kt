package ru.tagilov.avitotrainee.forecast.ui.screen

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.tagilov.avitotrainee.City
import ru.tagilov.avitotrainee.R
import ru.tagilov.avitotrainee.forecast.ui.component.*
import ru.tagilov.avitotrainee.forecast.ui.entity.PermissionState
import ru.tagilov.avitotrainee.forecast.ui.viewmodel.ForecastViewModel
import timber.log.Timber

@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
fun Forecast(
    navController: NavController,
    city: City? = null
) {
    val vm: ForecastViewModel = viewModel()
    LaunchedEffect(key1 = Unit) {
        vm.configure(city = city)
    }

    val permissionState = remember { vm.permissionStateFlow }.collectAsState()
    val cityState = remember { vm.cityFlow }.collectAsState()
    val forecastState = remember { vm.forecastFlow }.collectAsState()
    val isRefreshing = remember { vm.isRefreshingFlow }.collectAsState()
    val screenState = remember { vm.screenStateFlow }.collectAsState()
    val context = LocalContext.current
    //локация
    val sendLocation = {
        Timber.d("SEND LOCATION !!!!!")
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
            Timber.d("permission granted")
            vm.newPermissionState(PermissionState.Granted)
            sendLocation()
        } else {
            Timber.d("permission denied")
            vm.newPermissionState(PermissionState.Denied)
        }
    }
    SideEffect {
        if (permissionState.value == PermissionState.Required) {
            vm.newPermissionState(PermissionState.Waiting)
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Timber.d("permission is already granted")
                vm.newPermissionState(PermissionState.Granted)
                sendLocation()
            } else {
                Timber.d("permission requested")
                permissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
            }
        }
    }


//    //непосредственно верстка
    val snackbarState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

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
                .background(color = MaterialTheme.colors.surface)
                .fillMaxSize(),
        ) {
            Column {
                if (screenState.value == ForecastScreenState.Content
                    || screenState.value == ForecastScreenState.Loading
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
                        }
                    }
                } else if (screenState.value == ForecastScreenState.ErrorState.Connection) {
                    ConnectionError { vm.refresh() }
                } else if (screenState.value == ForecastScreenState.ErrorState.Location) {
                    LocationError { vm.refresh() }
                }
            }
        }
    }
    val errMsg = stringResource(id = R.string.unable_to_update)
    val ch = remember {
        vm.showSnackBarEvent
    }.collectAsState()
    LaunchedEffect(key1 = ch.value) {
        Timber.d("RECEIVED")
        if (ch.value != null)
            coroutineScope.launch {
                snackbarState.showSnackbar(errMsg)
            }
    }

//    if
//    if (ch.value) {
//        val errMsg = stringResource(id = R.string.unable_to_update)
//        SideEffect {
//            Timber.d("Show snackbar")
//            coroutineScope.launch {
//                snackbarState.showSnackbar(errMsg)
//            }
//        }
//    }
//    val
//    val errMsg = stringResource(id = R.string.unable_to_update)
//    LaunchedEffect(key1 = ch){
//
//        coroutineScope.launch {
//            snackbarState.showSnackbar(errMsg)
//        }
//    }

//    for (i in ch)

//    val scaffoldState = rememberScaffoldState() // this contains the `SnackbarHostState`
//    val coroutineScope = rememberCoroutineScope()
//
//    Scaffold(
//        modifier = Modifier,
//        scaffoldState = scaffoldState, // attaching `scaffoldState` to the `Scaffold`,
//    ) {
//        Button(
//            onClick = {
//                coroutineScope.launch { // using the `coroutineScope` to `launch` showing the snackbar
//                    // taking the `snackbarHostState` from the attached `scaffoldState`
//                    val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
//                        message = "This is your message",
//                        actionLabel = "Do something."
//                    )
//                    when (snackbarResult) {
//                        SnackbarResult.Dismissed -> Timber.d("Dismissed")
//                        SnackbarResult.ActionPerformed -> Timber.d("Snackbar's button clicked")
//                    }
//                }
//            }
//        ) {
//            Text(text = "A button that shows a Snackbar")
//        }
//    }

}

sealed class ForecastScreenState {
    object None : ForecastScreenState()
    object Loading : ForecastScreenState()
    object Content : ForecastScreenState()
    sealed class ErrorState : ForecastScreenState() {
        object Location : ErrorState()
        object Connection : ErrorState()
    }
}


@Preview
@Composable
fun ForecastPreview() {

}