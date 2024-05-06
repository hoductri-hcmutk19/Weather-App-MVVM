package com.example.weather.data.repository

import com.example.weather.data.model.CurrentResponse
import com.example.weather.data.model.DailyResponse
import com.example.weather.data.model.HourlyResponse
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun fetchWeatherForecastCurrent(
        latitude: Double,
        longitude: Double
    ): Flow<CurrentResponse>

    fun fetchWeatherForecastHourly(
        latitude: Double,
        longitude: Double
    ): Flow<HourlyResponse>

    fun fetchWeatherForecastDaily(
        latitude: Double,
        longitude: Double
    ): Flow<DailyResponse>
}
