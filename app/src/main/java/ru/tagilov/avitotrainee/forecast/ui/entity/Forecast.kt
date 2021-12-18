package ru.tagilov.avitotrainee.forecast.ui.entity

import android.annotation.SuppressLint
import ru.tagilov.avitotrainee.forecast.data.entity.OneCallResponse
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
    val temp = response.current.temp.roundToInt()
    val hourly = response.hourly.take(25).map { r -> HourlyForecast.fromResponse(r) }

    val minTemp: Int = hourly.minByOrNull { it.temperature }?.temperature ?: temp
    val maxTemp: Int = hourly.maxByOrNull { it.temperature }?.temperature ?: temp

    return Forecast(
        current = CurrentForecast.fromResponse(
            response.current,
            minTemp,
            maxTemp,
        ),
        hourly = hourly,
        daily = response.daily.map { r ->
            DailyForecast.fromResponse(r)
        }
    )
}

