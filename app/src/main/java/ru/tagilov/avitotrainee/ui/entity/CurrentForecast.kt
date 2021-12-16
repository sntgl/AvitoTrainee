package ru.tagilov.avitotrainee.ui.entity

import android.annotation.SuppressLint
import ru.tagilov.avitotrainee.data.entity.CurrentForecastResponse
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

data class CurrentForecast(
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
) {
    companion object
}

@SuppressLint("SimpleDateFormat")
fun CurrentForecast.Companion.fromResponse(
    response: CurrentForecastResponse,
    minTemp: Int,
    maxTemp: Int
) = CurrentForecast(
    icon = response.weather[0].icon,
    minTemp = minTemp,
    maxTemp = maxTemp,
    currentTemp = response.temp.roundToInt(),
    currentDescription = response.weather[0].description.replaceFirstChar { it.uppercase() },
    wind = response.wind_speed.roundToInt(),
    humidity = response.humidity,
    feelsLike = response.feels_like.roundToInt(),
    sunset = SimpleDateFormat("H:m").format(Date(response.sunset.toLong() * 1000)),
    sunrise = SimpleDateFormat("H:m").format(Date(response.sunrise.toLong() * 1000)),
)