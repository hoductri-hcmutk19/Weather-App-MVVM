package com.app.hiweather.di

import com.app.hiweather.data.repository.source.WeatherDataSource
import com.app.hiweather.data.repository.source.local.WeatherLocalDataSource
import com.app.hiweather.data.repository.source.remote.WeatherRemoteDataSource
import org.koin.dsl.module

val DataSourceModule = module {
    single<WeatherDataSource.Remote> { WeatherRemoteDataSource(get()) }
    single<WeatherDataSource.Local> { WeatherLocalDataSource(get()) }
}
