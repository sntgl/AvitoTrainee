package ru.tagilov.avitotrainee.ui.entity

data class CurrentForecast (
    val icon: String,
    val minTemp: Int,
    val maxTemp: Int,
    val currentTemp: Int,
    val currentDescription: String,
    val wind: Int,
    val humidity: Int,
    val feelsLike: Int,
    val sunset: String,
    val sunrise: String,
    val city: String?,
)