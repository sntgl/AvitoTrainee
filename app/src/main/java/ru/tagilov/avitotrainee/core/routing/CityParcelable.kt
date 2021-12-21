package ru.tagilov.avitotrainee.core.routing

import android.os.Parcel
import android.os.Parcelable

data class CityParcelable(
    val name: String?,
    val latitude: Double,
    val longitude: Double,
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readDouble(),
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CityParcelable> {
        override fun createFromParcel(parcel: Parcel): CityParcelable {
            return CityParcelable(parcel)
        }

        override fun newArray(size: Int): Array<CityParcelable?> {
            return arrayOfNulls(size)
        }
    }
}