package ru.tagilov.avitotrainee.forecast.ui.entity

sealed class Forecast {
    data class Data(
        val current: CurrentForecast,
        val hourly: List<HourlyForecast>,
        val daily: List<DailyForecast>,
    ): Forecast()
    class Empty: Forecast()
}
