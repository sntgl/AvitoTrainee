package ru.tagilov.avitotrainee.city.ui.entity

import ru.tagilov.avitotrainee.core.db.SavedCity

data class CityModel(
    val id: String,
    val name: String,
    val lat: Double,
    val lon: Double,
    val countryCode: String
)

fun CityModel.toSaved(): SavedCity = SavedCity(
    id = id,
    name = name,
    lat = lat,
    lon = lon,
    countryCode = countryCode
)