package ru.tagilov.avitotrainee.city.data

//import ru.tagilov.avitotrainee.city.data.CityNetworking.cityApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.tagilov.avitotrainee.city.data.entity.toCityModel
import ru.tagilov.avitotrainee.city.ui.entity.CityModel
import java.io.IOException
import javax.inject.Inject

interface CityRepository {
    suspend fun searchCities(
            query: String,
    ): Flow<List<CityModel>?>
}

class CityRepositoryImpl @Inject constructor(
        private val cityApi: CityApi
) : CityRepository {
    override suspend fun searchCities(
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

}