package ru.tagilov.avitotrainee.root.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.tagilov.avitotrainee.city.data.CityApi
import ru.tagilov.avitotrainee.core.Key
import ru.tagilov.avitotrainee.core.db.AppDatabase
import ru.tagilov.avitotrainee.core.util.addQueryApiKey
import ru.tagilov.avitotrainee.forecast.data.ForecastApi
import ru.tagilov.avitotrainee.forecast.data.LocationApi
import timber.log.Timber
import javax.inject.Qualifier
import javax.inject.Scope


@Module
class AppModule{

    @AppScope //Есть вопросы
    @Provides
    fun provideDatabase(context: Context): AppDatabase {
        Timber.d("Database init!")
        return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                AppDatabase.DB_NAME
        ).build()
    }

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)

    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Forecast
    fun provideWeatherClient(
            httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
            .addQueryApiKey("appid", Key.WEATHER_API_KEY)
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Provides
    @Forecast
    fun provideForecastRetrofit(
            @Forecast weatherClient: OkHttpClient,
            gsonConverterFactory: GsonConverterFactory
    ) : Retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(gsonConverterFactory)
            .client(weatherClient)
            .build()

    @Location
    @Provides
    fun provideLocationClient(
            httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
            .addQueryApiKey("apiKey", Key.LOCATION_API_KEY)
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Location
    @Provides
    fun provideLocationRetrofit(
            @Location locationClient: OkHttpClient,
            gsonConverterFactory: GsonConverterFactory
    ): Retrofit = Retrofit.Builder()
            .baseUrl("https://api.ipgeolocation.io/")
            .addConverterFactory(gsonConverterFactory)
            .client(locationClient)
            .build()

    @Provides
    fun provideForecastApi(
            @Forecast forecastRetrofit: Retrofit
    ) : ForecastApi = forecastRetrofit.create()

    @Provides
    fun provideLocationApi(
            @Location forecastRetrofit: Retrofit
    ) : LocationApi = forecastRetrofit.create()

    @Provides
    fun provideCityApi(
            @Forecast forecastRetrofit: Retrofit
    ) : CityApi = forecastRetrofit.create()

}


@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Location

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Forecast

@Scope
annotation class AppScope