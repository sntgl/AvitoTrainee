package ru.tagilov.avitotrainee.city.data.entity

import com.google.gson.annotations.SerializedName
import ru.tagilov.avitotrainee.city.ui.entity.CityModel
import ru.tagilov.avitotrainee.forecast.data.entity.ResponseCityLocale
import java.util.*


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
        id = UUID.randomUUID().toString(), //надо было разделить сущности
        name = localNames?.ru ?: name,
        lat = lat,
        lon = lon,
        countryCode = country
    )
}
