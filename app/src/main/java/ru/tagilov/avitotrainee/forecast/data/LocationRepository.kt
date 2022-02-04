package ru.tagilov.avitotrainee.forecast.data

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.tagilov.avitotrainee.core.util.TypedResult
import ru.tagilov.avitotrainee.forecast.data.entity.toDomain
import ru.tagilov.avitotrainee.forecast.ui.entity.DomainLocation
import javax.inject.Inject

interface LocationRepository {
    fun getLocationRx(): Single<TypedResult<DomainLocation>>
}

class LocationRepositoryImpl @Inject constructor(
        private val locationApi: LocationApi
): LocationRepository {

    override fun getLocationRx(): Single<TypedResult<DomainLocation>> =
        locationApi.locationRx()
            .map {
                val body = it.body()
                if (it.isSuccessful && body != null) TypedResult.Ok(body.toDomain())
                else TypedResult.Err()
            }
            .onErrorResumeNext { Single.just(TypedResult.Err()) }
            .subscribeOn(Schedulers.io())

}