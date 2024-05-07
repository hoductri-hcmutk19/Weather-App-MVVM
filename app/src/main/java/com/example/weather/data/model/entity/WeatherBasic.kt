package com.example.weather.data.model.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherBasic(
    var dateTime: Int? = 0,
    var temperature: Double? = 0.0,
    var weatherMainCondition: String? = "",
    var weatherDescription: String? = "",
    var humidity: Int? = 0,
    var windSpeed: Double? = 0.0
) : Parcelable
