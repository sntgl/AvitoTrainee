package ru.tagilov.avitotrainee.data.entity


data class OneCallResponse(
    val current: CurrentForecastResponse,
    val hourly: List<HourlyForecastResponse>,
    val daily: List<DailyForecastResponse>,
)