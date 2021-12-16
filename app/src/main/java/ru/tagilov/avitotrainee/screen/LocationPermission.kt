package ru.tagilov.avitotrainee.screen

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import ru.tagilov.avitotrainee.ui.entity.PermissionState
import timber.log.Timber

@Composable
fun LocationPermission(
    navController: NavController
) {
    val state: MutableState<PermissionState> = remember { mutableStateOf(PermissionState.Required) }
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Timber.d("permission granted")
            state.value = PermissionState.Granted
        } else {
            Timber.d("permission denied")
            state.value = PermissionState.Denied
        }
    }

    if (state.value == PermissionState.Required) {
        state.value = PermissionState.Waiting
        SideEffect {
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) -> {
                    Timber.d("permission already granted")
                    state.value = PermissionState.Granted
                }
                else -> {
                    Timber.d("permission request")
                    launcher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
                }
            }
        }
    }

    when (state.value) {
        PermissionState.Denied -> {
            navController.navigate(Destination.Forecast.createRoute(false)) {
                launchSingleTop = true
                popUpTo(Destination.Permission.route) {inclusive = true}
            }
        }
        PermissionState.Granted -> {
            navController.navigate(Destination.Forecast.createRoute(true)) {
                launchSingleTop = true
                popUpTo(Destination.Permission.route) {inclusive = true}
            }
        }
        else -> {}
    }
}



@Preview
@Composable
fun LocationPermissionPreview() {

}