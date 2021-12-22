package ru.tagilov.avitotrainee.core.routing


sealed class Destination(val route: String) {
    object Forecast: Destination("forecast") {
        const val KEY_CITY: String = "city"
    }
    object City: Destination("city")
}