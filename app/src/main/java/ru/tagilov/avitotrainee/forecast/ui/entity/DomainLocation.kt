package ru.tagilov.avitotrainee.forecast.ui.entity

import ru.tagilov.avitotrainee.forecast.data.entity.ResponseLocation

data class DomainLocation(
    val longitude: Double,
    val latitude: Double
) {
    companion object
}

fun DomainLocation.Companion.fromResponse(loc: ResponseLocation) =
    DomainLocation(
        longitude = loc.longitude.toDouble(),
        latitude = loc.latitude.toDouble(),
    )