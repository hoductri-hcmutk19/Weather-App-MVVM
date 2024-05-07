package com.example.weather.utils

import com.example.weather.data.model.entity.Weather

object Utils {
    fun mergeComponentIntoWeather(
        current: Weather,
        hourly: Weather,
        daily: Weather,
        isFavorite: String
    ): Weather {
        return Weather(
            current.id,
            current.latitude,
            current.longitude,
            current.timeZone,
            current.city,
            current.country,
            isFavorite,
            current.weatherCurrent,
            hourly.weatherHourlyList,
            daily.weatherDailyList
        )
    }
}
