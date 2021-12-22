package ru.tagilov.avitotrainee.city.ui.util

import ru.tagilov.avitotrainee.city.ui.entity.CityModel
import ru.tagilov.avitotrainee.core.db.SavedCity
import kotlin.random.Random

fun SavedCity.toModel() = CityModel (
    id = id,
    name = name,
    lon = lon,
    lat = lat,
    countryCode = countryCode
)