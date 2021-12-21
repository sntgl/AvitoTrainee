package ru.tagilov.avitotrainee.core.db

object SavedCityContract {
    const val TABLE_NAME = "savedCities"

    object Columns {
        const val ID = "id"
        const val NAME = "name"
        const val COUNTRY_CODE = "cc"
        const val LONGITUDE = "lon"
        const val LATITUDE = "lat"
    }
}