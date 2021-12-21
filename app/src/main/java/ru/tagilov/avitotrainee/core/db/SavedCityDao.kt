package ru.tagilov.avitotrainee.core.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedCityDao {
    @Query("SELECT * FROM ${SavedCityContract.TABLE_NAME}")
    suspend fun getAll(): List<SavedCity>

    //все равно одинаковые города нельзя искать, да и сервак никаких id не возвращает,
    // чтобы нормально идентифицировать города
    //ну либо сделать через сравнение всех хар-к города
    @Query(
        "SELECT * FROM ${SavedCityContract.TABLE_NAME} " +
                "WHERE ${SavedCityContract.Columns.NAME} = :name " +
                "LIMIT 1"
    )
    fun get(name: String): Flow<SavedCity?>

    @Insert
    suspend fun save(savedCity: SavedCity)

    @Delete
    suspend fun delete(savedCity: SavedCity)
}
