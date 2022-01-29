package ru.tagilov.avitotrainee.root

import android.app.Application
import android.content.Context
import kotlinx.coroutines.flow.count
import ru.tagilov.avitotrainee.BuildConfig
import ru.tagilov.avitotrainee.core.db.AppDatabase
import ru.tagilov.avitotrainee.core.db.Database
import ru.tagilov.avitotrainee.root.di.AppComponent
import ru.tagilov.avitotrainee.root.di.AppModule
import ru.tagilov.avitotrainee.root.di.DaggerAppComponent
import timber.log.Timber
import javax.inject.Inject

class App : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
        Database.init(this)
        appComponent = DaggerAppComponent.builder().context(this).build()
    }
}
