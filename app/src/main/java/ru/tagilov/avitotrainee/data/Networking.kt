package ru.tagilov.avitotrainee.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import timber.log.Timber

object Networking {
    private val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC);

    private val locationClient: OkHttpClient = OkHttpClient.Builder()
        .addQueryApiKey("apiKey", LOCATION_API_KEY)
        .addInterceptor(logger)
        .build()

    private val locationRetrofit = Retrofit.Builder()
        .baseUrl("https://api.ipgeolocation.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(locationClient)
        .build()

    val locationApi: LocationApi
        get() = locationRetrofit.create()


    private val weatherClient: OkHttpClient = OkHttpClient.Builder()
        .addQueryApiKey("appid", WEATHER_API_KEY)
        .addInterceptor(logger)
        .build()

    private val forecastRetrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(weatherClient)
        .build()

    val forecastApi: ForecastApi
        get() = forecastRetrofit.create()
//
//    val userApi: UserApi
//        get() = retrofit.create()
}

private const val LOCATION_API_KEY = "dd8bacba38c84ee4a11cbe0614b07322"
private const val WEATHER_API_KEY = "b66745312676702b882b8e673d774421"

private fun OkHttpClient.Builder.addQueryApiKey(
    name: String,
    key: String,
): OkHttpClient.Builder = this.addInterceptor { chain ->
    val request = chain.request()
    val url = request
        .url
        .newBuilder()
        .addQueryParameter(name, key)
        .build()
    val newRequest = request.newBuilder().url(url).build()
    chain.proceed(newRequest)
}