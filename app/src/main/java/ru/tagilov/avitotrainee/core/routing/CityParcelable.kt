package ru.tagilov.avitotrainee.core.routing

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.tagilov.avitotrainee.core.db.SavedCity

@Parcelize
data class CityParcelable(
    val id: String?,
    val name: String?,
    val countryCode: String?,
    val latitude: Double,
    val longitude: Double,
) : Parcelable

@Throws(IllegalArgumentException::class)
fun CityParcelable.toSavedCity(): SavedCity {
    requireNotNull(id)
    requireNotNull(name)
    requireNotNull(countryCode)
    return SavedCity(
        id = id,
        name = name,
        lon = longitude,
        lat = latitude,
        countryCode = countryCode,
    )
}