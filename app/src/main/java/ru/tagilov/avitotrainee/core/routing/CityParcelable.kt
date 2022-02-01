package ru.tagilov.avitotrainee.core.routing

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CityParcelable(
    val id: String?,
    val name: String?,
    val countryCode: String?,
    val latitude: Double,
    val longitude: Double,
) : Parcelable