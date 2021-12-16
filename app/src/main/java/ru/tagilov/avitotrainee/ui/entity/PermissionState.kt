package ru.tagilov.avitotrainee.ui.entity

sealed class PermissionState {
    object None: PermissionState()
    object Required: PermissionState()
    object Waiting : PermissionState()
    object Granted : PermissionState()
    object Denied : PermissionState()
}