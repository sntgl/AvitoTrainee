package ru.tagilov.avitotrainee.forecast.di

import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

interface SchedulersFactory {
    fun io(): Scheduler
}

class SchedulersFactoryImpl @Inject constructor(): SchedulersFactory {
    override fun io(): Scheduler = Schedulers.io()
}