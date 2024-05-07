package com.example.weather.data.repository.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weather.data.model.entity.Weather
import com.example.weather.data.repository.source.local.converter.WeatherBasicConverter
import com.example.weather.data.repository.source.local.converter.WeatherBasicListConverter
import com.example.weather.data.repository.source.local.dao.WeatherDao

@Database(entities = [Weather::class], version = 1, exportSchema = false)
@TypeConverters(WeatherBasicConverter::class, WeatherBasicListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao

    companion object {
        private const val DB_NAME = "hiWeather.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DB_NAME
            ).build()
    }
}
