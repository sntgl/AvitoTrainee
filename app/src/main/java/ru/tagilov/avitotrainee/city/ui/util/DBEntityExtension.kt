package ru.tagilov.avitotrainee.city.ui.util

import ru.tagilov.avitotrainee.city.ui.entity.CityModel
import ru.tagilov.avitotrainee.core.db.SavedCity

fun SavedCity.toModel() = CityModel (
    name = name,
    lon = lon,
    lat = lat,
    countryCode = countryCode
)