package com.example.weather.data.repository.source.local

import com.example.weather.data.model.entity.Weather
import com.example.weather.data.repository.source.WeatherDataSource
import com.example.weather.data.repository.source.local.dao.WeatherDao
import com.example.weather.utils.Constant
import com.example.weather.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherLocalDataSource(
    private val weatherDao: WeatherDao
) : WeatherDataSource.Local {

    override suspend fun insertCurrentWeather(current: Weather, hourly: Weather, daily: Weather) {
        val weather = Utils.mergeComponentIntoWeather(current, hourly, daily, Constant.FALSE)
        withContext(Dispatchers.IO) {
            weatherDao.deleteCurrentWeather()
            weatherDao.insertWeather(weather)
        }
    }

    override suspend fun insertCurrentWeather(weather: Weather) {
        weather.isFavorite = Constant.FALSE
        withContext(Dispatchers.IO) {
            weatherDao.deleteCurrentWeather()
            weatherDao.insertWeather(weather)
        }
    }

    override suspend fun insertFavoriteWeather(current: Weather, hourly: Weather, daily: Weather) {
        val weather = Utils.mergeComponentIntoWeather(current, hourly, daily, Constant.TRUE)
        weatherDao.insertWeather(weather)
    }

    override suspend fun insertFavoriteWeather(weather: Weather) {
        weather.isFavorite = Constant.TRUE
        weatherDao.insertWeather(weather)
    }

    override suspend fun getAllLocalWeathers(): List<Weather> {
        return weatherDao.getAllData()
    }

    override suspend fun getLocalWeather(id: String): Weather? {
        return weatherDao.getWeather(id)
    }

    override suspend fun deleteWeather(id: String) {
        weatherDao.deleteWeather(id)
    }
}
