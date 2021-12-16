package ru.tagilov.avitotrainee.data.entity


data class DailyTemperatureResponse(
    val day: Float,
    val min: Float,
    val max: Float,
)

data class DailyFeelsLikeResponse(
    val day: Float,
)

data class DailyForecastResponse(
    val dt: Int,
    val sunrise: Int,
    val sunset: Int,
    val temp: DailyTemperatureResponse,
    val feels_like: DailyFeelsLikeResponse,
    val pressure: Int,
    val humidity: Int,
    val wind_speed: Float,
    val weather: List<WeatherResponse>,
)