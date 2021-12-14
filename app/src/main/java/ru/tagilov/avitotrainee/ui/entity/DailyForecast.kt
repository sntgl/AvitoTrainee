package ru.tagilov.avitotrainee.ui.entity

data class DailyForecast (
    val day: String, //Now or week day
    val icon: String,
    val minTemp: Int,
    val maxTemp: Int,
    val wind: Int,
    val humidity: Int,
    val feelsLike: Int,
    val sunset: String,
    val sunrise: String,
)