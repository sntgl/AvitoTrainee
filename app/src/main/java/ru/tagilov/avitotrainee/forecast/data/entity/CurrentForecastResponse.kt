package ru.tagilov.avitotrainee.forecast.data.entity

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import ru.tagilov.avitotrainee.forecast.ui.entity.CurrentForecast
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

data class CurrentForecastResponse(
    val sunrise: Int,
    val sunset: Int,
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
fun CurrentForecastResponse.toCurrentForecast(
    minTemp: Int,
    maxTemp: Int
) = CurrentForecast(
    icon = weather[0].icon,
    minTemp = minTemp,
    maxTemp = maxTemp,
    currentTemp = temp.roundToInt(),
    currentDescription = weather[0].description.replaceFirstChar { it.uppercase() },
    wind = windSpeed.roundToInt(),
    humidity = humidity,
    feelsLike = feelsLike.roundToInt(),
    sunset = SimpleDateFormat("H:m").format(Date(sunset.toLong() * 1000)),
    sunrise = SimpleDateFormat("H:m").format(Date(sunrise.toLong() * 1000)),
)