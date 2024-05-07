package com.example.weather.data.repository

import com.example.weather.data.model.entity.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    /**
     * Local
     */
    fun insertCurrentWeather(current: Weather, hourly: Weather, daily: Weather)
    fun insertCurrentWeather(weather: Weather)
    fun insertFavoriteWeather(current: Weather, hourly: Weather, daily: Weather)
    fun insertFavoriteWeather(weather: Weather)
    fun getAllLocalWeathers(): List<Weather>
    fun getLocalWeather(id: String): Weather?
    fun deleteWeather(id: String)

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
