package ru.tagilov.avitotrainee.core.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.tagilov.avitotrainee.core.routing.CityParcelable

@Entity(tableName = SavedCityContract.TABLE_NAME)
data class SavedCity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = SavedCityContract.Columns.ID)
    val id: Int? = null,
    @ColumnInfo(name = SavedCityContract.Columns.COUNTRY_CODE) val countryCode: String,
    @ColumnInfo(name = SavedCityContract.Columns.NAME) val name: String,
    @ColumnInfo(name = SavedCityContract.Columns.LONGITUDE) val lon: Double,
    @ColumnInfo(name = SavedCityContract.Columns.LATITUDE) val lat: Double
) {
    companion object
}

fun SavedCity.unwrap(): CityParcelable = CityParcelable(
    name = name,
    countryCode = countryCode,
    longitude = lon,
    latitude = lat
)

fun SavedCity.Companion.wrap(city: CityParcelable): SavedCity = SavedCity(
    name = city.name ?: "", //TODO ЛЮТЫЙ КОСТЫЛЬ!!! надо нормальные модели
    lon = city.longitude,
    lat = city.latitude,
    countryCode = city.countryCode ?: "",
)