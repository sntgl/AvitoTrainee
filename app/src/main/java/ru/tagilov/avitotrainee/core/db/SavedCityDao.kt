package ru.tagilov.avitotrainee.core.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedCityDao {
    @Query("SELECT * FROM ${SavedCityContract.TABLE_NAME}")
    fun getAll(): Flow<List<SavedCity>>

    @Query("SELECT * FROM ${SavedCityContract.TABLE_NAME}")
    fun getAllRx(): Flowable<List<SavedCity>>

    //все равно одинаковые города нельзя искать, да и сервак никаких id не возвращает,
    // чтобы нормально идентифицировать города
    // ну либо сделать через сравнение всех хар-к города
    @Query(
        "SELECT * FROM ${SavedCityContract.TABLE_NAME} " +
                "WHERE ${SavedCityContract.Columns.ID} = :id " +
                "LIMIT 1"
    )
    fun get(id: String): Flow<SavedCity?>

    @Insert
    suspend fun save(savedCity: SavedCity)

    @Delete
    suspend fun delete(savedCity: SavedCity)

    @Delete
    fun deleteRx(savedCity: SavedCity): Completable
}
