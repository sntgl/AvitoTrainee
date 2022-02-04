package ru.tagilov.avitotrainee.core.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Location

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Forecast