package ru.tagilov.avitotrainee.city.ui.entity

import ru.tagilov.avitotrainee.city.data.entity.ResponseCity
import ru.tagilov.avitotrainee.forecast.data.entity.ResponseCityLocale

data class City(
    val name: String,
    val lat: Double,
    val lon: Double,
    val countryCode: String
){
    companion object
}

fun City.Companion.fromResponse(r: ResponseCity): City {
    return City(
        name = r.localNames?.ru ?: r.name,
        lat = r.lat,
        lon = r.lon,
        countryCode = r.country
    )
}