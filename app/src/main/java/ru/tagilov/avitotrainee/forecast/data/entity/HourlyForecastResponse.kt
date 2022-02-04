package ru.tagilov.avitotrainee.forecast.data.entity

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import ru.tagilov.avitotrainee.forecast.ui.entity.HourlyForecast
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

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

@SuppressLint("SimpleDateFormat")
fun HourlyForecastResponse.toHourlyForecast() = HourlyForecast(
    time = "%02d".format(
        SimpleDateFormat("H").format(Date(dt.toLong()*1000)).toInt()
    ),
    icon = weather[0].icon,
    temperature = temp.roundToInt()
)