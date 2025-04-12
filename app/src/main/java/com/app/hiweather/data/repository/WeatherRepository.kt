package com.app.hiweather.data.repository

import com.app.hiweather.data.model.entity.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    /**
     * Local
     */
    suspend fun insertCurrentWeather(current: Weather, hourly: Weather, daily: Weather)
    suspend fun insertCurrentWeather(weather: Weather)
    suspend fun insertFavoriteWeather(current: Weather, hourly: Weather, daily: Weather)
    suspend fun insertFavoriteWeather(weather: Weather)
    suspend fun getAllLocalWeathers(): List<Weather>
    suspend fun getLocalWeather(id: String): Weather?
    suspend fun deleteWeather(id: String)

    /**
     * Remote
     */
    fun fetchWeatherForecastCurrent(
        latitude: Double,
        longitude: Double
    ): Flow<Weather>

    fun fetchWeatherForecastHourly(
        latitude: Double,
        longitude: Double
    ): Flow<Weather>

    fun fetchWeatherForecastDaily(
        latitude: Double,
        longitude: Double
    ): Flow<Weather>
}
