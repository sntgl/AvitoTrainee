package ru.tagilov.avitotrainee.forecast.data.entity

import com.google.gson.annotations.SerializedName
import ru.tagilov.avitotrainee.forecast.ui.entity.CityNameData

data class ResponseCityLocale(
    val ru: String?
)

data class ResponseCityName (
    val name: String,
    @SerializedName("local_names")
    val localNames: ResponseCityLocale?,
    @SerializedName("country")
    val countryCode: String,
)

fun ResponseCityName.toCityNameData() = CityNameData(
    name = localNames?.ru ?: name,
    countryCode = countryCode
)