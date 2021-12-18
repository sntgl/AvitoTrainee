package ru.tagilov.avitotrainee.ui.screen


sealed class Destination(val route: String) {
    object Forecast: Destination("forecast/{granted}") {
        fun createRoute(granted: Boolean) = "forecast/$granted"
        const val granted: String = "granted"
    }
}