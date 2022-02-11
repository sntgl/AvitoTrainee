package ru.tagilov.avitotrainee.core

sealed class SnackbarEvent {
    class Show(val state: SnackBarMessage) : SnackbarEvent()
    object Empty : SnackbarEvent()
}

enum class SnackBarMessage {
    UNABLE_LOAD, UNABLE_SAVE
}

