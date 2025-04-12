package com.app.hiweather.data.repository.source.remote

import com.app.hiweather.data.model.CurrentResponse
import com.app.hiweather.data.model.DailyResponse
import com.app.hiweather.data.model.HourlyResponse
import com.app.hiweather.data.repository.source.WeatherDataSource
import com.app.hiweather.data.repository.source.remote.api.ApiService
import com.app.hiweather.utils.Constant

class WeatherRemoteDataSource(private val apiService: ApiService) : WeatherDataSource.Remote {
    override suspend fun fetchWeatherForecastCurrent(
        latitude: Double,
        longitude: Double
    ): CurrentResponse {
        return apiService.getCurrentWeather(latitude, longitude, apiKey = Constant.BASE_API_KEY)
    }

    override suspend fun fetchWeatherForecastHourly(
        latitude: Double,
        longitude: Double
    ): HourlyResponse {
        return apiService.getHourlyWeather(
            latitude,
            longitude,
            cnt = Constant.HOURLY_NUM_TIME,
            apiKey = Constant.BASE_API_KEY
        )
    }

    override suspend fun fetchWeatherForecastDaily(
        latitude: Double,
        longitude: Double
    ): DailyResponse {
        return apiService.getDailyWeather(
            latitude,
            longitude,
            cnt = Constant.DAILY_NUM_DAY,
            apiKey = Constant.BASE_API_KEY
        )
    }
}
