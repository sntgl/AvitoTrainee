package ru.tagilov.avitotrainee.city.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.tagilov.avitotrainee.city.data.CityNetworking.cityApi
import ru.tagilov.avitotrainee.city.ui.entity.CityModel
import ru.tagilov.avitotrainee.city.ui.entity.fromResponse
import java.io.IOException

class CityRepository {
    suspend fun searchCities(
        query: String,
    ): Flow<List<CityModel>?> = flow {
        try {
            emit(
                cityApi.getCities(q = query)
            )
        } catch (e: IOException) {
            emit(null)
        }
    }.map{ list ->
        list?.map{
            CityModel.fromResponse(it)
        }
    }.flowOn(Dispatchers.IO)

}