package ru.tagilov.avitotrainee.city.ui.util

import ru.tagilov.avitotrainee.city.ui.entity.CityModel
import ru.tagilov.avitotrainee.core.db.SavedCity
import kotlin.random.Random

fun SavedCity.toModel() = CityModel (
    id = id ?: Random.nextInt(),  //не используется, но по-хорошему надо было разделить сущности
    name = name,
    lon = lon,
    lat = lat,
    countryCode = countryCode
)