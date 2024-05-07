package com.example.weather.data.repository.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weather.data.model.entity.Weather
import com.example.weather.data.model.entity.WeatherEntry.TBL_WEATHER_NAME

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeather(weather: Weather)

    @Query("SELECT * FROM $TBL_WEATHER_NAME")
    fun getAllData(): List<Weather>

    @Query("SELECT * FROM $TBL_WEATHER_NAME WHERE id = :idWeather")
    fun getWeather(idWeather: String): Weather?

    @Query("DELETE FROM $TBL_WEATHER_NAME WHERE isFavorite = 'false'")
    fun deleteCurrentWeather()

    @Query("DELETE FROM $TBL_WEATHER_NAME WHERE id = :idWeather")
    fun deleteWeather(idWeather: String)
}
