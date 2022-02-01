package ru.tagilov.avitotrainee.forecast.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.tagilov.avitotrainee.core.util.TypedResult
import ru.tagilov.avitotrainee.forecast.data.entity.toDomain
import ru.tagilov.avitotrainee.forecast.ui.entity.DomainLocation
import java.io.IOException
import javax.inject.Inject

interface LocationRepository {
    suspend fun getLocation(): Flow<TypedResult<DomainLocation>>
}

class LocationRepositoryImpl @Inject constructor(
        private val locationApi: LocationApi
): LocationRepository {
    override suspend fun getLocation() = flow {
        try {
            val result = locationApi.location()
            emit(TypedResult.Ok(result.toDomain()))
        } catch (e: IOException) {
            emit(TypedResult.Err())
        }
    }.flowOn(Dispatchers.IO)

}