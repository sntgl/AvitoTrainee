package ru.tagilov.avitotrainee.screen

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import ru.tagilov.avitotrainee.City
import ru.tagilov.avitotrainee.ForecastViewModel
import ru.tagilov.avitotrainee.ui.component.CityTitle
import ru.tagilov.avitotrainee.ui.entity.PermissionState
import timber.log.Timber

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
        Button(onClick = { vm.getForecast() }) {
            Text("run")
        }
        Button(onClick = { vm.test() }) {
            Text("stop")
        }
        Text(cityState.value?.latitude.toString() + " " + cityState.value?.longitude.toString())
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