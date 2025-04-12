package com.app.hiweather.di

import com.app.hiweather.data.repository.WeatherRepository
import com.app.hiweather.data.repository.WeatherRepositoryImpl
import com.app.hiweather.data.repository.source.WeatherDataSource
import com.app.hiweather.data.repository.source.local.WeatherLocalDataSource
import com.app.hiweather.data.repository.source.remote.WeatherRemoteDataSource
import org.koin.dsl.module

val RepositoryModule = module {
    single {
        provideWeatherRepository(
            WeatherLocalDataSource(get()),
            WeatherRemoteDataSource(get())
        )
    }
}

fun provideWeatherRepository(
    local: WeatherDataSource.Local,
    remote: WeatherDataSource.Remote
): WeatherRepository {
    return WeatherRepositoryImpl(local, remote)
}
