package ru.tagilov.avitotrainee.forecast.data.entity


data class OneCallResponse(
    val current: CurrentForecastResponse,
    val hourly: List<HourlyForecastResponse>,
    val daily: List<DailyForecastResponse>,
)