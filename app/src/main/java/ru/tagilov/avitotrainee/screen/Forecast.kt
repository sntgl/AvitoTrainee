package ru.tagilov.avitotrainee.screen

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import com.google.android.gms.location.LocationServices
import ru.tagilov.avitotrainee.City
import ru.tagilov.avitotrainee.ForecastViewModel
import ru.tagilov.avitotrainee.ui.component.CityTitle
import ru.tagilov.avitotrainee.ui.component.Current
import ru.tagilov.avitotrainee.ui.component.Daily
import ru.tagilov.avitotrainee.ui.component.Hourly
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
                    Timber.d("Location is null!!")
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
    //непосредственно верстка

    Column {
        CityTitle(city = cityState.value)
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

}

sealed class ForecastScreenState {
    object None: ForecastScreenState()
    object NeedLocation: ForecastScreenState()
    object Loading: ForecastScreenState()
    object Error: ForecastScreenState()
}


@Preview
@Composable
fun ForecastPreview() {

}