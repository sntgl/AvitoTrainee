package ru.tagilov.avitotrainee.data.entity

data class WeatherResponse(
    val description: String,
    val icon: String,
)

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

data class HourlyForecastResponse(
    val temp: Float,
    val feels_like: Float,
    val pressure: Int,
    val humidity: Int,
    val wind_speed: Float,
    val weather: List<WeatherResponse>,
)

data class OneCallResponse(
    val current: CurrentForecastResponse,
    val hourly: List<HourlyForecastResponse>,
    val daily: List<DailyForecastResponse>,
)
