package com.example.weather.data.repository.source.local.converter

import androidx.room.TypeConverter
import com.example.weather.data.model.entity.WeatherBasic
import com.google.gson.Gson

class WeatherBasicConverter {
    @TypeConverter
    fun fromWeatherBasic(weatherBasic: WeatherBasic?): String? {
        return weatherBasic?.let { Gson().toJson(it) }
    }

    @TypeConverter
    fun toWeatherBasic(json: String?): WeatherBasic? {
        return json?.let { Gson().fromJson(it, WeatherBasic::class.java) }
    }
}
