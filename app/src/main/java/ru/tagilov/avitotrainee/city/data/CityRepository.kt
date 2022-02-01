package ru.tagilov.avitotrainee.city.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.tagilov.avitotrainee.city.data.entity.toCityModel
import ru.tagilov.avitotrainee.city.ui.entity.CityModel
import ru.tagilov.avitotrainee.city.ui.util.toModel
import ru.tagilov.avitotrainee.core.db.AppDatabase
import ru.tagilov.avitotrainee.core.db.SavedCity
import java.io.IOException
import javax.inject.Inject

interface CityRepository {
    fun searchCities(query: String): Flow<List<CityModel>?>
    val savedCities: Flow<List<CityModel>>
    suspend fun deleteFromSaved(savedCity: SavedCity)
}

class CityRepositoryImpl @Inject constructor(
        private val cityApi: CityApi,
        db: AppDatabase
) : CityRepository {
    val dao = db.cityDao()

    override fun searchCities(
            query: String,
    ): Flow<List<CityModel>?> = flow {
        try {
            emit(
                cityApi.getCities(q = query)
            )
        } catch (e: IOException) {
            emit(null)
        }
    }.map { list ->
        list?.map {
            it.toCityModel()
        }
    }.flowOn(Dispatchers.IO)

    override val savedCities: Flow<List<CityModel>>
        get() = dao.getAll().map { it.map { it.toModel() } }

    override suspend fun deleteFromSaved(savedCity: SavedCity) {
        dao.delete(savedCity = savedCity)
    }

}