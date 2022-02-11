package ru.tagilov.avitotrainee.forecast.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.create
import ru.tagilov.avitotrainee.core.di.Forecast
import ru.tagilov.avitotrainee.core.di.Location
import ru.tagilov.avitotrainee.forecast.data.ForecastApi
import ru.tagilov.avitotrainee.forecast.data.ForecastRepository
import ru.tagilov.avitotrainee.forecast.data.ForecastRepositoryImpl
import ru.tagilov.avitotrainee.forecast.data.LocationApi
import ru.tagilov.avitotrainee.forecast.data.LocationRepository
import ru.tagilov.avitotrainee.forecast.data.LocationRepositoryImpl

@Module(includes = [ForecastBinds::class])
class ForecastModule {
    @Provides
    fun provideForecastApi(
            @Forecast forecastRetrofit: Retrofit
    ) : ForecastApi = forecastRetrofit.create()

    @Provides
    fun provideLocationApi(
            @Location locationRetrofit: Retrofit
    ) : LocationApi = locationRetrofit.create()

}

@Module
interface ForecastBinds {
    @Binds
    fun bindForecastRepository(repository: ForecastRepositoryImpl): ForecastRepository

    @Binds
    fun bindLocationRepository(repository: LocationRepositoryImpl): LocationRepository
}
