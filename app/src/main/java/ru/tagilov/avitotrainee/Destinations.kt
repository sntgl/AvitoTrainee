package ru.tagilov.avitotrainee.screen


sealed class Destination(val route: String) {
    object Permission: Destination("permission")
    object Forecast: Destination("forecast/{granted}") {
        fun createRoute(granted: Boolean) = "forecast/$granted"
        const val granted: String = "granted"
    }
}