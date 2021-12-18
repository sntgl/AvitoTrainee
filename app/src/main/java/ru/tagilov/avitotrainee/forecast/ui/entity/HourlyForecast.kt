package ru.tagilov.avitotrainee.forecast.ui.entity

import android.annotation.SuppressLint
import ru.tagilov.avitotrainee.forecast.data.entity.HourlyForecastResponse
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

data class HourlyForecast (
    val time: String, //Now or hour!!
    val icon: String,
    val temperature: Int,
){
    companion object
}

@SuppressLint("SimpleDateFormat")
fun HourlyForecast.Companion.fromResponse(
    response: HourlyForecastResponse,
) = HourlyForecast(
    time = "%02d".format(
        SimpleDateFormat("H").format(Date(response.dt.toLong()*1000)).toInt()
    ),
    icon = response.weather[0].icon,
    temperature = response.temp.roundToInt()
)