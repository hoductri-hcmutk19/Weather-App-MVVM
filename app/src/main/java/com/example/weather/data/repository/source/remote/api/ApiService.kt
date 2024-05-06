package com.example.weather.data.repository.source.remote.api

import com.example.weather.data.model.CurrentResponse
import com.example.weather.data.model.DailyResponse
import com.example.weather.data.model.HourlyResponse
import com.example.weather.utils.Constant
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET(Constant.CURRENT)
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String
    ): CurrentResponse

    @GET(Constant.HOURLY)
    suspend fun getHourlyWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("cnt") cnt: String,
        @Query("appid") apiKey: String
    ): HourlyResponse

    @GET(Constant.DAILY)
    suspend fun getDailyWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("cnt") cnt: String,
        @Query("appid") apiKey: String
    ): DailyResponse
}
