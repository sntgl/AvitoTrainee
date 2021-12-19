package ru.tagilov.avitotrainee.forecast.ui.screen

import ru.tagilov.avitotrainee.City


sealed class Destination(val route: String) {
    object Forecast: Destination("forecast") {
        const val key_city: String = "city"
    }
}