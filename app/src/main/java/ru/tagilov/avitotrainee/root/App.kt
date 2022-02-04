package ru.tagilov.avitotrainee.root

import android.app.Application
import ru.tagilov.avitotrainee.BuildConfig
import ru.tagilov.avitotrainee.root.di.AppComponent
import ru.tagilov.avitotrainee.root.di.DaggerAppComponent
import timber.log.Timber

class App : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
        appComponent = DaggerAppComponent.factory().create(this)
    }
}
