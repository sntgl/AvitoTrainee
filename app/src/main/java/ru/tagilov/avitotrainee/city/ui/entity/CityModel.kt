package ru.tagilov.avitotrainee.city.ui.entity

import ru.tagilov.avitotrainee.city.data.entity.ResponseCity

data class CityModel(
    val name: String,
    val lat: Double,
    val lon: Double,
    val countryCode: String
){
    companion object
}

fun CityModel.Companion.fromResponse(r: ResponseCity): CityModel {
    return CityModel(
        name = r.localNames?.ru ?: r.name,
        lat = r.lat,
        lon = r.lon,
        countryCode = r.country
    )
}