package ru.tagilov.avitotrainee.forecast.data.entity

import com.google.gson.annotations.SerializedName

data class HourlyForecastResponse(
    val dt: Int,
    val temp: Float,
    @SerializedName("feels_like")
    val feelsLike: Float,
    val pressure: Int,
    val humidity: Int,
    @SerializedName("wind_speed")
    val windSpeed: Float,
    val weather: List<WeatherResponse>,
)