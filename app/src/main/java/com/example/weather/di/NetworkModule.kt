package com.example.weather.di

import android.app.Application
import com.example.weather.data.repository.source.remote.api.ApiService
import com.example.weather.utils.Constant
import com.google.gson.Gson
import okhttp3.Cache
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val NetworkModule = module {
    single { provideOkHttpCache(get()) }

    single { provideOkHttpClient(get()) }

    single { provideRetrofit(get(), get()) }

    single { provideApiService(get()) }
}

fun provideOkHttpCache(app: Application): Cache {
    val cacheSize: Long = NetWorkInstant.CACHE_SIZE
    return Cache(app.cacheDir, cacheSize)
}

fun provideOkHttpClient(cache: Cache): OkHttpClient {
    val httpClientBuilder = OkHttpClient.Builder()
    httpClientBuilder.cache(cache)

    httpClientBuilder.readTimeout(
        NetWorkInstant.READ_TIMEOUT,
        TimeUnit.SECONDS
    )
    httpClientBuilder.writeTimeout(
        NetWorkInstant.WRITE_TIMEOUT,
        TimeUnit.SECONDS
    )
    httpClientBuilder.connectTimeout(
        NetWorkInstant.CONNECT_TIMEOUT,
        TimeUnit.SECONDS
    )

    return httpClientBuilder.build()
}

fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder().baseUrl(Constant.BASE_URL).addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient).build()
}

fun provideApiService(retrofit: Retrofit): ApiService {
    return retrofit.create(ApiService::class.java)
}

object NetWorkInstant {
    internal const val READ_TIMEOUT = 60L
    internal const val WRITE_TIMEOUT = 30L
    internal const val CONNECT_TIMEOUT = 60L

    internal const val CACHE_SIZE = 10 * 1024 * 1024L // 10MB
}
