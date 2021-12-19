package ru.tagilov.avitotrainee.forecast.ui.entity

import android.annotation.SuppressLint
import ru.tagilov.avitotrainee.forecast.data.entity.DailyForecastResponse
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
    val pressure: Int,
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
    wind = response.windSpeed.roundToInt(),
    humidity = response.humidity,
    feelsLike = response.feelsLike.day.roundToInt(),
    sunset = SimpleDateFormat("H:m")
        .format(Date(response.sunset.toLong() * 1000)),
    sunrise = SimpleDateFormat("H:m")
        .format(Date(response.sunrise.toLong() * 1000)),
    day = SimpleDateFormat("E")
        .format(Date(response.dt.toLong() * 1000)).replaceFirstChar { it.uppercase() },
    pressure = (response.pressure.toFloat() * 3 / 4).roundToInt() //гПа
)