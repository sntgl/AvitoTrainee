package ru.tagilov.avitotrainee.root.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.tagilov.avitotrainee.core.Key
import ru.tagilov.avitotrainee.core.db.AppDatabase
import ru.tagilov.avitotrainee.core.di.AppScope
import ru.tagilov.avitotrainee.core.di.Forecast
import ru.tagilov.avitotrainee.core.di.Location
import ru.tagilov.avitotrainee.core.util.addQueryApiKey
import timber.log.Timber

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

    @AppScope
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)

    @AppScope
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
}