package ru.tagilov.avitotrainee.forecast.ui.entity

sealed class PermissionState {
    object None: PermissionState()
    object Required: PermissionState()
    object Waiting : PermissionState()
    object Granted : PermissionState()
    object Denied : PermissionState()
}