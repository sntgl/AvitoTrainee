package ru.tagilov.avitotrainee.ui.entity

import android.annotation.SuppressLint
import androidx.compose.ui.text.capitalize
import ru.tagilov.avitotrainee.data.entity.CurrentForecastResponse
import ru.tagilov.avitotrainee.data.entity.DailyForecastResponse
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

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
){
    companion object
}

@SuppressLint("SimpleDateFormat")
fun DailyForecast.Companion.fromResponse(
    response: DailyForecastResponse,
) = DailyForecast(
    icon = response.weather[0].icon,
    minTemp = response.temp.min.roundToInt(),
    maxTemp = response.temp.max.roundToInt(),
    wind = response.wind_speed.roundToInt(),
    humidity = response.humidity,
    feelsLike = response.feels_like.day.roundToInt(),
    sunset = SimpleDateFormat("H:m")
        .format(Date(response.sunset.toLong() * 1000)),
    sunrise = SimpleDateFormat("H:m")
        .format(Date(response.sunrise.toLong() * 1000)),
    day = SimpleDateFormat("E")
        .format(Date(response.dt.toLong() * 1000)).replaceFirstChar { it.uppercase() },
)