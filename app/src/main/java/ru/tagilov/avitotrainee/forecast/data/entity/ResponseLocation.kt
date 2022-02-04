package ru.tagilov.avitotrainee.forecast.data.entity

import ru.tagilov.avitotrainee.forecast.ui.entity.DomainLocation

data class ResponseLocation(
    val longitude: String,
    val latitude: String
)

fun ResponseLocation.toDomain() = DomainLocation(
    longitude = longitude.toDouble(),
    latitude = latitude.toDouble(),
)