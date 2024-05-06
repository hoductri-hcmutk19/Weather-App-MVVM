package com.example.weather.data.repository

import com.example.weather.data.model.CurrentResponse
import com.example.weather.data.model.DailyResponse
import com.example.weather.data.model.HourlyResponse
import com.example.weather.data.repository.source.WeatherDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent

class WeatherRepositoryImpl(
    private val remoteDataSource: WeatherDataSource.Remote
) : KoinComponent, WeatherRepository {

    override fun fetchWeatherForecastCurrent(
        latitude: Double,
        longitude: Double
    ): Flow<CurrentResponse> {
        return flow {
            emit(remoteDataSource.fetchWeatherForecastCurrent(latitude, longitude))
        }
    }

    override fun fetchWeatherForecastHourly(
        latitude: Double,
        longitude: Double
    ): Flow<HourlyResponse> {
        return flow {
            emit(remoteDataSource.fetchWeatherForecastHourly(latitude, longitude))
        }
    }

    override fun fetchWeatherForecastDaily(
        latitude: Double,
        longitude: Double
    ): Flow<DailyResponse> {
        return flow {
            emit(remoteDataSource.fetchWeatherForecastDaily(latitude, longitude))
        }
    }
}
