package ru.tagilov.avitotrainee.core

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.tagilov.avitotrainee.core.util.addQueryApiKey

object Networking {
    private val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)

    private val locationClient: OkHttpClient = OkHttpClient.Builder()
        .addQueryApiKey("apiKey", Key.LOCATION_API_KEY)
        .addInterceptor(logger)
        .build()

    val locationRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.ipgeolocation.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(locationClient)
        .build()

    private val weatherClient: OkHttpClient = OkHttpClient.Builder()
        .addQueryApiKey("appid", Key.WEATHER_API_KEY)
        .addInterceptor(logger)
        .build()

    val forecastRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(weatherClient)
        .build()

}