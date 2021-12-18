package ru.tagilov.avitotrainee

import ru.tagilov.avitotrainee.data.entity.ResponseLocation

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