package com.example.weather.di

import com.example.weather.data.repository.WeatherRepository
import com.example.weather.data.repository.WeatherRepositoryImpl
import com.example.weather.data.repository.source.WeatherDataSource
import com.example.weather.data.repository.source.remote.WeatherRemoteDataSource
import org.koin.dsl.module

val RepositoryModule = module {
    single { provideWeatherRepository(WeatherRemoteDataSource(get())) }
}

fun provideWeatherRepository(remote: WeatherDataSource.Remote): WeatherRepository {
    return WeatherRepositoryImpl(remote)
}
