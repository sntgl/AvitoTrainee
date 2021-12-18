package ru.tagilov.avitotrainee.forecast.data.entity

data class CurrentForecastResponse(
    val sunrise: Int,
    val sunset: Int,
    val temp: Float,
    val feels_like: Float,
    val pressure: Int,
    val humidity: Int,
    val wind_speed: Float,
    val weather: List<WeatherResponse>,
)
