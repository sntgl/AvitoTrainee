package ru.tagilov.avitotrainee.forecast.data.entity

import com.google.gson.annotations.SerializedName


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
    @SerializedName("feels_like")
    val feelsLike: DailyFeelsLikeResponse,
    val pressure: Int,
    val humidity: Int,
    @SerializedName("wind_speed")
    val windSpeed: Float,
    val weather: List<WeatherResponse>,
)