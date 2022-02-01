package ru.tagilov.avitotrainee.forecast.data.entity

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import ru.tagilov.avitotrainee.forecast.ui.entity.DailyForecast
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


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

@SuppressLint("SimpleDateFormat")
fun DailyForecastResponse.toCurrentForecast() = DailyForecast(
    icon = weather[0].icon,
    minTemp = temp.min.roundToInt(),
    maxTemp = temp.max.roundToInt(),
    wind = windSpeed.roundToInt(),
    humidity = humidity,
    feelsLike = feelsLike.day.roundToInt(),
    sunset = SimpleDateFormat("H:m")
        .format(Date(sunset.toLong() * 1000)),
    sunrise = SimpleDateFormat("H:m")
        .format(Date(sunrise.toLong() * 1000)),
    day = SimpleDateFormat("E")
        .format(Date(dt.toLong() * 1000)).replaceFirstChar { it.uppercase() },
    pressure = (pressure.toFloat() * 3 / 4).roundToInt() //гПа
)