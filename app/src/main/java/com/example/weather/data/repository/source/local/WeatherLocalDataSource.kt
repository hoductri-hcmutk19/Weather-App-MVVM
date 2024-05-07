package com.example.weather.data.repository.source.local

import com.example.weather.data.model.entity.Weather
import com.example.weather.data.repository.source.WeatherDataSource
import com.example.weather.data.repository.source.local.dao.WeatherDao
import com.example.weather.utils.Constant
import com.example.weather.utils.Utils

class WeatherLocalDataSource(
    private val weatherDao: WeatherDao
) : WeatherDataSource.Local {

    override fun insertCurrentWeather(current: Weather, hourly: Weather, daily: Weather) {
        val weather = Utils.mergeComponentIntoWeather(current, hourly, daily, Constant.FALSE)
        synchronized(this) {
            weatherDao.deleteCurrentWeather()
            weatherDao.insertWeather(weather)
        }
    }

    override fun insertCurrentWeather(weather: Weather) {
        weather.isFavorite = Constant.FALSE
        synchronized(this) {
            weatherDao.deleteCurrentWeather()
            weatherDao.insertWeather(weather)
        }
    }

    override fun insertFavoriteWeather(current: Weather, hourly: Weather, daily: Weather) {
        val weather = Utils.mergeComponentIntoWeather(current, hourly, daily, Constant.TRUE)
        weatherDao.insertWeather(weather)
    }

    override fun insertFavoriteWeather(weather: Weather) {
        weather.isFavorite = Constant.TRUE
        weatherDao.insertWeather(weather)
    }

    override fun getAllLocalWeathers(): List<Weather> {
        return weatherDao.getAllData()
    }

    override fun getLocalWeather(id: String): Weather? {
        return weatherDao.getWeather(id)
    }

    override fun deleteWeather(id: String) {
        weatherDao.deleteWeather(id)
    }
}
