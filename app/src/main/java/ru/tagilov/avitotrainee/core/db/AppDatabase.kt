package ru.tagilov.avitotrainee.core.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SavedCity::class], version = AppDatabase.DB_VERSION)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cityDao(): SavedCityDao
    companion object {
        const val DB_VERSION = 1
        const val DB_NAME = "app-database"
    }
}