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
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule{

    @Singleton //TODO поменять на нормальный скоуп
    @Provides
    fun provideDatabase(context: Context): AppDatabase {
        Timber.d("Database init!")
        return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                AppDatabase.DB_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)

    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()


    @Singleton
    @Provides
    @Forecast
    fun provideWeatherClient(
            httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
            .addQueryApiKey("appid", Key.WEATHER_API_KEY)
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Singleton
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

}