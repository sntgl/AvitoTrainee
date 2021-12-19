package ru.tagilov.avitotrainee.forecast.ui.screen


sealed class Destination(val route: String) {
    object Forecast: Destination("forecast") {
        const val key_city: String = "city"
    }
    object City: Destination("city")
}