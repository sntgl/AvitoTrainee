package ru.tagilov.avitotrainee.forecast.ui.screen


sealed class Destination(val route: String) {
    object Forecast: Destination("forecast") {
        const val KEY_CITY: String = "city"
    }
    object City: Destination("city")
}