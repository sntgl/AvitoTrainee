package ru.tagilov.avitotrainee.core.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = SavedCityContract.TABLE_NAME)
data class SavedCity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = SavedCityContract.Columns.ID)
    val id: String,
    @ColumnInfo(name = SavedCityContract.Columns.COUNTRY_CODE) val countryCode: String,
    @ColumnInfo(name = SavedCityContract.Columns.NAME) val name: String,
    @ColumnInfo(name = SavedCityContract.Columns.LONGITUDE) val lon: Double,
    @ColumnInfo(name = SavedCityContract.Columns.LATITUDE) val lat: Double
)