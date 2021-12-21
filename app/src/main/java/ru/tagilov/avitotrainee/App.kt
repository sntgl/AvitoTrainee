package ru.tagilov.avitotrainee

import android.app.Application
import ru.tagilov.avitotrainee.core.db.Database
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Database.init(this)
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }
}