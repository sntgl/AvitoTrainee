package ru.tagilov.avitotrainee.city.data.entity

import com.google.gson.annotations.SerializedName
import ru.tagilov.avitotrainee.forecast.data.entity.ResponseCityLocale


data class ResponseCity (
    val name: String,
    @SerializedName("local_names")
    val localNames: ResponseCityLocale?,
    val lat: Double,
    val lon: Double,
    val country: String
)