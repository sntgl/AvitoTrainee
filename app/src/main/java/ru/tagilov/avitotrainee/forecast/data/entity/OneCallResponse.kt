package ru.tagilov.avitotrainee.forecast.data.entity

import ru.tagilov.avitotrainee.forecast.ui.entity.Forecast
import kotlin.math.roundToInt


data class OneCallResponse(
    val current: CurrentForecastResponse,
    val hourly: List<HourlyForecastResponse>,
    val daily: List<DailyForecastResponse>,
)

fun OneCallResponse.toForecast(): Forecast {
    val temp = current.temp.roundToInt()
    val hourly = hourly.take(25).map { r -> r.toHourlyForecast() }

    val minTemp: Int = hourly.minByOrNull { it.temperature }?.temperature ?: temp
    val maxTemp: Int = hourly.maxByOrNull { it.temperature }?.temperature ?: temp

    return Forecast(
        current = current.toCurrentForecast(
            minTemp,
            maxTemp,
        ),
        hourly = hourly,
        daily = daily.map { r ->
            r.toCurrentForecast()
        }
    )
}

