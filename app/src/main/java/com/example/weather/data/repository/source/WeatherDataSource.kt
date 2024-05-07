package com.example.weather.data.repository.source

import com.example.weather.data.model.CurrentResponse
import com.example.weather.data.model.DailyResponse
import com.example.weather.data.model.HourlyResponse
import com.example.weather.data.model.entity.Weather

interface WeatherDataSource {
    /**
     * Local
     */
    interface Local {
        fun insertCurrentWeather(current: Weather, hourly: Weather, daily: Weather)
        fun insertCurrentWeather(weather: Weather)
        fun insertFavoriteWeather(current: Weather, hourly: Weather, daily: Weather)
        fun insertFavoriteWeather(weather: Weather)
        fun getAllLocalWeathers(): List<Weather>
        fun getLocalWeather(id: String): Weather?
        fun deleteWeather(id: String)
    }

    /**
     * Remote
     */
    interface Remote {
        suspend fun fetchWeatherForecastCurrent(
            latitude: Double,
            longitude: Double
        ): CurrentResponse

        suspend fun fetchWeatherForecastHourly(
            latitude: Double,
            longitude: Double
        ): HourlyResponse

        suspend fun fetchWeatherForecastDaily(
            latitude: Double,
            longitude: Double
        ): DailyResponse
    }
}
