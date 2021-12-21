package ru.tagilov.avitotrainee.forecast.ui.viewmodel

sealed class ForecastState {
    object None : ForecastState()
    object Loading : ForecastState()
    object Content : ForecastState()
    sealed class ErrorState : ForecastState() {
        object Location : ErrorState()
        object Connection : ErrorState()
    }
}