package ru.tagilov.avitotrainee.city.data

import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import ru.tagilov.avitotrainee.city.data.entity.toCityModel
import ru.tagilov.avitotrainee.city.ui.entity.CityModel
import ru.tagilov.avitotrainee.city.ui.util.toModel
import ru.tagilov.avitotrainee.core.db.AppDatabase
import ru.tagilov.avitotrainee.core.db.SavedCity
import ru.tagilov.avitotrainee.core.util.TypedResult
import ru.tagilov.avitotrainee.forecast.di.SchedulersFactory
import javax.inject.Inject

interface CityRepository {
    fun searchCitiesRx(query: String): Single<TypedResult<List<CityModel>>>
    val savedCities: Flowable<TypedResult<List<CityModel>>>
    fun deleteFromSavedRx(savedCity: SavedCity): Single<TypedResult<Unit>>
}

class CityRepositoryImpl @Inject constructor(
    private val cityApi: CityApi,
    private val schedulers: SchedulersFactory,
    db: AppDatabase
) : CityRepository {
    private val dao = db.cityDao()

    override fun searchCitiesRx(query: String): Single<TypedResult<List<CityModel>>> =
        cityApi
            .getCitiesRx(query)
            .map { TypedResult.Ok(it.map { it.toCityModel() }) as TypedResult<List<CityModel>> }
            .onErrorResumeNext { Single.just(TypedResult.Err()) }
            .subscribeOn(schedulers.io())

    override val savedCities: Flowable<TypedResult<List<CityModel>>>
        get() = dao.getAllRx()
            .map { TypedResult.Ok(it.map { it.toModel() }) as TypedResult<List<CityModel>> }
            .onErrorResumeNext { Flowable.just(TypedResult.Err()) }
            .subscribeOn(schedulers.io())

    override fun deleteFromSavedRx(savedCity: SavedCity): Single<TypedResult<Unit>> =
        dao.deleteRx(savedCity = savedCity)
            .toSingleDefault(TypedResult.Ok(Unit) as TypedResult<Unit>)
            .onErrorResumeNext { Single.just(TypedResult.Err()) }
            .subscribeOn(schedulers.io())

}