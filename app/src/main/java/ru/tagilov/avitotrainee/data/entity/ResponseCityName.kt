package ru.tagilov.avitotrainee.data.entity

data class ResponseCityLocale(
    val ru: String
)

data class ResponseCityName (
    val name: String,
    val local_names: ResponseCityLocale
)
