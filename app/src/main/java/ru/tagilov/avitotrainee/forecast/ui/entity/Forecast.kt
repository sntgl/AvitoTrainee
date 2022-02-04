package ru.tagilov.avitotrainee.forecast.ui.entity

data class Forecast(
    val current: CurrentForecast,
    val hourly: List<HourlyForecast>,
    val daily: List<DailyForecast>,
)