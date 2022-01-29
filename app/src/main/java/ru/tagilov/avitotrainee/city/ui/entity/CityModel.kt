package ru.tagilov.avitotrainee.city.ui.entity

import ru.tagilov.avitotrainee.city.data.entity.ResponseCity
import ru.tagilov.avitotrainee.core.db.SavedCity
import java.util.*

data class CityModel(
    val id: String,
    val name: String,
    val lat: Double,
    val lon: Double,
    val countryCode: String
) {
    companion object
}

fun CityModel.Companion.fromResponse(r: ResponseCity): CityModel {
    return CityModel(
        id = UUID.randomUUID().toString(), //надо было разделить сущности
        name = r.localNames?.ru ?: r.name,
        lat = r.lat,
        lon = r.lon,
        countryCode = r.country
    )
}

fun CityModel.toSaved(): SavedCity = SavedCity(
    id = id,
    name = name,
    lat = lat,
    lon = lon,
    countryCode = countryCode
)