package ru.tagilov.avitotrainee.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.tagilov.avitotrainee.ui.entity.DomainLocation
import ru.tagilov.avitotrainee.ui.entity.fromResponse
import java.io.IOException

class LocationRepository {
    suspend fun getLocation(): Flow<DomainLocation?> = flow {
        try {
            emit(Networking.locationApi.location())
        } catch (e: IOException) {
            emit(null)
        }
    }.map {
        if (it != null) (DomainLocation.fromResponse(it))
        else null
    }.flowOn(Dispatchers.IO)

}