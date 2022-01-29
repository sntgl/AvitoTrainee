package ru.tagilov.avitotrainee.core.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.tagilov.avitotrainee.core.routing.CityParcelable

@Entity(tableName = SavedCityContract.TABLE_NAME)
data class SavedCity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = SavedCityContract.Columns.ID)
    val id: String,
    @ColumnInfo(name = SavedCityContract.Columns.COUNTRY_CODE) val countryCode: String,
    @ColumnInfo(name = SavedCityContract.Columns.NAME) val name: String,
    @ColumnInfo(name = SavedCityContract.Columns.LONGITUDE) val lon: Double,
    @ColumnInfo(name = SavedCityContract.Columns.LATITUDE) val lat: Double
) {
    companion object
}

@Throws(IllegalArgumentException::class)
fun SavedCity.Companion.wrap(city: CityParcelable): SavedCity {
    requireNotNull(city.id)
    requireNotNull(city.name)
    requireNotNull(city.countryCode)
    return SavedCity(
        id = city.id,
        name = city.name,
        lon = city.longitude,
        lat = city.latitude,
        countryCode = city.countryCode,
    )
}