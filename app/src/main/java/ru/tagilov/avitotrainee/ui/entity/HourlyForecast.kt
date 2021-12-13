package ru.tagilov.avitotrainee.ui.entity

data class HourlyForecast (
    val time: String, //Now or hour
    val icon: String,
    val temperature: Int,
)