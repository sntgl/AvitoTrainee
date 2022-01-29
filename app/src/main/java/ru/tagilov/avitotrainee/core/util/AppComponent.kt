package ru.tagilov.avitotrainee.core.util

import android.content.Context
import ru.tagilov.avitotrainee.root.App
import ru.tagilov.avitotrainee.root.di.AppComponent


val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> appComponent
        else -> applicationContext.appComponent
    }