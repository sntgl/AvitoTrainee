package ru.tagilov.avitotrainee.forecast.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.tagilov.avitotrainee.forecast.ui.entity.DomainLocation
import ru.tagilov.avitotrainee.forecast.ui.entity.fromResponse
import java.io.IOException
import javax.inject.Inject

interface LocationRepository {
    suspend fun getLocation(): Flow<DomainLocation?>
}

class LocationRepositoryImpl @Inject constructor(
        private val locationApi: LocationApi
): LocationRepository {
    override suspend fun getLocation(): Flow<DomainLocation?> = flow {
        try {
            emit(locationApi.location())
        } catch (e: IOException) {
            emit(null)
        }
    }.map {
        if (it != null) (DomainLocation.fromResponse(it))
        else null
    }.flowOn(Dispatchers.IO)

}