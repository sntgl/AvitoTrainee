package ru.tagilov.avitotrainee.city.data

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.tagilov.avitotrainee.city.data.entity.toCityModel
import ru.tagilov.avitotrainee.city.ui.entity.CityModel
import ru.tagilov.avitotrainee.city.ui.util.toModel
import ru.tagilov.avitotrainee.core.db.AppDatabase
import ru.tagilov.avitotrainee.core.db.SavedCity
import ru.tagilov.avitotrainee.core.util.TypedResult
import javax.inject.Inject

interface CityRepository {
    fun searchCitiesRx(query: String): Single<TypedResult<List<CityModel>>>
    val savedCities: Flowable<List<CityModel>>
    fun deleteFromSavedRx(savedCity: SavedCity): Completable
}

class CityRepositoryImpl @Inject constructor(
    private val cityApi: CityApi,
    db: AppDatabase
) : CityRepository {
    val dao = db.cityDao()

    override fun searchCitiesRx(query: String): Single<TypedResult<List<CityModel>>> =
        cityApi
            .getCitiesRx(query)
            .map {
                val body = it.body()?.map { it.toCityModel() }
                if (it.isSuccessful && body != null)
                    TypedResult.Ok(body)
                else
                    TypedResult.Err()
            }
            .subscribeOn(Schedulers.io())

    override val savedCities: Flowable<List<CityModel>>
        get() = dao.getAllRx().map { it.map { it.toModel() } }

    override fun deleteFromSavedRx(savedCity: SavedCity) = dao.deleteRx(savedCity = savedCity)

}