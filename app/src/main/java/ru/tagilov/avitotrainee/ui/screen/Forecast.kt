package ru.tagilov.avitotrainee.ui.screen

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.android.gms.location.LocationServices
import ru.tagilov.avitotrainee.ui.entity.City
import ru.tagilov.avitotrainee.ui.viewmodel.ForecastViewModel
import ru.tagilov.avitotrainee.ui.component.*
import ru.tagilov.avitotrainee.ui.entity.PermissionState
import timber.log.Timber

@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
fun Forecast(
    navController: NavController,
    city: City? = null
) {
    val vm: ForecastViewModel = viewModel()
    val vmConfigured = remember {
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

    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colors.surface)
            .fillMaxSize(),
    ) {
        if (screenState.value == ForecastScreenState.Content
            || screenState.value == ForecastScreenState.Loading) {
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
    object None: ForecastScreenState()
    object Loading: ForecastScreenState()
    object Content: ForecastScreenState()
    sealed class ErrorState: ForecastScreenState() {
        object Location: ErrorState()
        object Connection: ErrorState()
    }
}


@Preview
@Composable
fun ForecastPreview() {

}