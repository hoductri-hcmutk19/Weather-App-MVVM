package com.example.weather.di

import com.example.weather.data.repository.source.WeatherDataSource
import com.example.weather.data.repository.source.local.WeatherLocalDataSource
import com.example.weather.data.repository.source.remote.WeatherRemoteDataSource
import org.koin.dsl.module

val DataSourceModule = module {
    single<WeatherDataSource.Remote> { WeatherRemoteDataSource(get()) }
    single<WeatherDataSource.Local> { WeatherLocalDataSource(get()) }
}
