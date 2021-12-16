package ru.tagilov.avitotrainee.data.entity

data class HourlyForecastResponse(
    val dt: Int,
    val temp: Float,
    val feels_like: Float,
    val pressure: Int,
    val humidity: Int,
    val wind_speed: Float,
    val weather: List<WeatherResponse>,
)