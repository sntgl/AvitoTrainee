package ru.tagilov.avitotrainee.forecast.data.entity

import com.google.gson.annotations.SerializedName

data class ResponseCityLocale(
    val ru: String?
)

data class ResponseCityName (
    val name: String,
    @SerializedName("local_names")
    val localNames: ResponseCityLocale?
)
