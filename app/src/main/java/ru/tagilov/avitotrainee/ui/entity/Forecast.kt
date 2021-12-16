package ru.tagilov.avitotrainee.ui.entity

import android.annotation.SuppressLint
import ru.tagilov.avitotrainee.data.entity.OneCallResponse
import kotlin.math.roundToInt

data class Forecast(
    val current: CurrentForecast,
    val hourly: List<HourlyForecast>,
    val daily: List<DailyForecast>,
) {
    companion object
}

@SuppressLint("SimpleDateFormat")
fun Forecast.Companion.fromResponse(
    response: OneCallResponse,
): Forecast {
    val temp = response.current.temp
    val minTemp: Float = response.hourly.map{it.minTemp}.minByOrNull { it } ?: temp
    val maxTemp: Float = response.hourly.map{it.maxTemp}.maxByOrNull { it } ?: temp

    return Forecast(
        current = CurrentForecast.fromResponse(
            response.current,
            minTemp.roundToInt(),
            maxTemp.roundToInt(),
        ),
        hourly = response.hourly.map { r ->
            HourlyForecast.fromResponse(r)
        },
        daily = response.daily.map { r ->
            DailyForecast.fromResponse(r)
        }
    )
}

