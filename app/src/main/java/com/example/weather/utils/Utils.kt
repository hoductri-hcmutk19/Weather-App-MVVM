package com.example.weather.utils

import com.example.weather.R
import com.example.weather.data.anotation.Icon
import com.example.weather.data.model.entity.Weather
import com.example.weather.utils.Constant.HOURS_IN_DAY
import com.example.weather.utils.Constant.MIN_DELAY_INIT_WORKER
import com.example.weather.utils.Constant.MORNING_NOTIFICATION_TIME
import com.example.weather.utils.Constant.SECONDS_IN_HOUR
import com.example.weather.utils.ext.unixTimestampToHourString
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

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

    fun distanceBetweenPoints(
        latA: Double,
        lonA: Double,
        latB: Double,
        lonB: Double
    ): Double {
        val dLat = Math.toRadians(latB - latA)
        val dLon = Math.toRadians(lonB - lonA)
        val latARad = Math.toRadians(latA)
        val latBRad = Math.toRadians(latB)

        // Haversine formula
        val a = sin(dLat / 2) * sin(dLat / 2) +
            sin(dLon / 2) * sin(dLon / 2) * cos(latARad) * cos(latBRad)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return Constant.EARTH_RADIUS * c
    }

    fun checkTimeInterval(dateTime: Int): Boolean {
        val currentTimeUnix = System.currentTimeMillis() / Constant.MILLIS_IN_SECOND
        val interval = currentTimeUnix - dateTime
        return interval > Constant.FETCH_INTERVAL
    }

    fun setTimeInitWorker(): Long {
        var delayTime = MIN_DELAY_INIT_WORKER
        val currentTimeUnix = System.currentTimeMillis() / Constant.MILLIS_IN_SECOND
        val currentHour = currentTimeUnix.toInt().unixTimestampToHourString().toInt()
        return if (currentHour > MORNING_NOTIFICATION_TIME) {
            delayTime += (HOURS_IN_DAY - currentHour + MORNING_NOTIFICATION_TIME) * SECONDS_IN_HOUR
            delayTime
        } else {
            delayTime += (MORNING_NOTIFICATION_TIME - currentHour) * SECONDS_IN_HOUR
            delayTime
        }
    }

    fun getIcon(@Icon icon: String, time: Int): Int? {
        return if (time > Constant.NIGHT_TIME_START || time < Constant.NIGHT_TIME_END) {
            getNightIcons()[icon]
        } else {
            getBrightIcons()[icon]
        }
    }

    private fun getBrightIcons() = hashMapOf(
        Icon.CLEAR to R.drawable.ic_clear_day,
        Icon.RAIN to R.drawable.ic_rain_day,
        Icon.SNOW to R.drawable.ic_snow_day,
        Icon.CLOUDS to R.drawable.ic_clouds_day,
        Icon.SQUALL to R.drawable.ic_rain_day
    )

    private fun getNightIcons() = hashMapOf(
        Icon.CLEAR to R.drawable.ic_clear_night,
        Icon.RAIN to R.drawable.ic_rain_night,
        Icon.SNOW to R.drawable.ic_snow_night,
        Icon.CLOUDS to R.drawable.ic_clouds_night,
        Icon.SQUALL to R.drawable.ic_rain_night
    )
}
