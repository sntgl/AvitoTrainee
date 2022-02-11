package ru.tagilov.avitotrainee.forecast.data.entity

import ru.tagilov.avitotrainee.forecast.ui.entity.City
import ru.tagilov.avitotrainee.forecast.ui.entity.GeoOrigin

data class SetLocationData(
    val long: Double,
    val lat: Double,
    val origin: GeoOrigin
)

fun SetLocationData.toCity(): City = City.WithGeo(
    longitude = long,
    latitude = lat,
    origin = origin
)