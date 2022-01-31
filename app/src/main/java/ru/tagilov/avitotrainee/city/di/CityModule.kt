package ru.tagilov.avitotrainee.city.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.create
import ru.tagilov.avitotrainee.city.data.CityApi
import ru.tagilov.avitotrainee.city.data.CityRepository
import ru.tagilov.avitotrainee.city.data.CityRepositoryImpl
import ru.tagilov.avitotrainee.root.di.Forecast

@Module(includes = [CityBinds::class])
class CityModule {

    @Provides
    fun provideCityApi(
            @Forecast forecastRetrofit: Retrofit
    ) : CityApi = forecastRetrofit.create()

}

@Module
interface CityBinds {
    @Binds
    fun bindCityRepository(repository: CityRepositoryImpl): CityRepository
}
