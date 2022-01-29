package ru.tagilov.avitotrainee.core.db

import android.content.Context
import androidx.room.Room.databaseBuilder
import timber.log.Timber
import javax.inject.Inject

object Database {
    lateinit var instance: AppDatabase
        private set

    fun init(context: Context): AppDatabase {
        instance = databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DB_NAME
        ).build()
        return instance
    }
}
//
//class Database @Inject constructor(
//        private val context: Context
//) {
//    lateinit var instance: AppDatabase
//        private set
//
//    init {
//        Timber.d("AppDatabase init!!")
//        instance = databaseBuilder(
//                context,
//                AppDatabase::class.java,
//                AppDatabase.DB_NAME
//        ).build()
//    }
//
//    fun init(): AppDatabase {
//        return instance
//    }
//}