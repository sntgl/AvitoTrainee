package ru.tagilov.avitotrainee.forecast.ui.entity

import ru.tagilov.avitotrainee.core.db.SavedCity
import ru.tagilov.avitotrainee.core.routing.CityParcelable

sealed class City{
    object Empty: City()

    data class WithGeo(
        val latitude: Double,
        val longitude: Double,
        val origin: GeoOrigin,
    ): City()

    data class Full(
        val id: String,
        val name: String,
        val latitude: Double,
        val longitude: Double,
        val origin: GeoOrigin,
        val countryCode: String,
    ): City()
}

fun City.WithGeo.complete(
    name: String,
    countryCode: String
) : City.Full = City.Full(
    latitude = latitude,
    longitude = longitude,
    origin = origin,
    id = name+countryCode,
    name = name,
    countryCode = countryCode
)

enum class GeoOrigin {
    API, GPS, UNKNOWN,
}

fun CityParcelable?.toCity(): City {
    return if (this == null)
        City.Empty
    else {
        val city = City.WithGeo(latitude = latitude, longitude = longitude, origin = GeoOrigin.UNKNOWN)
        if (this.id != null && this.name != null && this.countryCode != null)
            city.complete(name = name, countryCode = countryCode)
        else
            city
    }
}

fun City.Full.toSaved(): SavedCity = SavedCity(
    id = id,
    countryCode = countryCode,
    name = name,
    lon = longitude,
    lat = latitude
)
