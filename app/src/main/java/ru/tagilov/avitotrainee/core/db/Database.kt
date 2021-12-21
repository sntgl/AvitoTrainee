package ru.tagilov.avitotrainee.core.db

import android.content.Context
import androidx.room.Room.databaseBuilder

object Database {
    lateinit var instance: AppDatabase
        private set

    fun init(context: Context) {
        instance = databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DB_NAME
        ).build()
    }
}