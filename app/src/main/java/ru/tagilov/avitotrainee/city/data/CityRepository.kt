package ru.tagilov.avitotrainee.city.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import retrofit2.Retrofit
//import ru.tagilov.avitotrainee.city.data.CityNetworking.cityApi
import ru.tagilov.avitotrainee.city.ui.entity.CityModel
import ru.tagilov.avitotrainee.city.ui.entity.fromResponse
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
            CityModel.fromResponse(it)
        }
    }.flowOn(Dispatchers.IO)

}