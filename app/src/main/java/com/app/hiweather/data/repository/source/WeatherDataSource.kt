package com.app.hiweather.data.repository.source

import com.app.hiweather.data.model.CurrentResponse
import com.app.hiweather.data.model.DailyResponse
import com.app.hiweather.data.model.HourlyResponse
import com.app.hiweather.data.model.entity.Weather

interface WeatherDataSource {
    /**
     * Local
     */
    interface Local {
        suspend fun insertCurrentWeather(current: Weather, hourly: Weather, daily: Weather)
        suspend fun insertCurrentWeather(weather: Weather)
        suspend fun insertFavoriteWeather(current: Weather, hourly: Weather, daily: Weather)
        suspend fun insertFavoriteWeather(weather: Weather)
        suspend fun getAllLocalWeathers(): List<Weather>
        suspend fun getLocalWeather(id: String): Weather?
        suspend fun deleteWeather(id: String)
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
