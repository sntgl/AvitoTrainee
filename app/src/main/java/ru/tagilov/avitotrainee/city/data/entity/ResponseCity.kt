package ru.tagilov.avitotrainee.city.data.entity

import com.google.gson.annotations.SerializedName
import ru.tagilov.avitotrainee.city.ui.entity.CityModel
import ru.tagilov.avitotrainee.forecast.data.entity.ResponseCityLocale


data class ResponseCity (
    val name: String,
    @SerializedName("local_names")
    val localNames: ResponseCityLocale?,
    val lat: Double,
    val lon: Double,
    val country: String
)

fun ResponseCity.toCityModel(): CityModel {
    return CityModel(
        name = localNames?.ru ?: name,
        lat = lat,
        lon = lon,
        countryCode = country,
        id = name + country, //надо было разделить сущности
    )
}
