package com.example.weather.data.repository.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.weather.data.model.entity.Weather
import com.example.weather.data.model.entity.WeatherEntry.TBL_WEATHER_NAME

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: Weather)

    @Transaction
    @Query("SELECT * FROM $TBL_WEATHER_NAME")
    suspend fun getAllData(): List<Weather>

    @Transaction
    @Query("SELECT * FROM $TBL_WEATHER_NAME WHERE id = :idWeather")
    suspend fun getWeather(idWeather: String): Weather?

    @Query("DELETE FROM $TBL_WEATHER_NAME WHERE isFavorite = 'false'")
    suspend fun deleteCurrentWeather()

    @Query("DELETE FROM $TBL_WEATHER_NAME WHERE id = :idWeather")
    suspend fun deleteWeather(idWeather: String)
}
