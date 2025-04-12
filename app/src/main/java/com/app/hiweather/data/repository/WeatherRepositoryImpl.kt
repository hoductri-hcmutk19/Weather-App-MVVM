package com.app.hiweather.data.repository

import com.app.hiweather.data.model.entity.Weather
import com.app.hiweather.data.model.toWeather
import com.app.hiweather.data.repository.source.WeatherDataSource
import com.app.hiweather.utils.Constant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent

class WeatherRepositoryImpl(
    private val localDataSource: WeatherDataSource.Local,
    private val remoteDataSource: WeatherDataSource.Remote
) : KoinComponent, WeatherRepository {
    override suspend fun insertCurrentWeather(current: Weather, hourly: Weather, daily: Weather) {
        localDataSource.insertCurrentWeather(current, hourly, daily)
    }

    override suspend fun insertCurrentWeather(weather: Weather) {
        localDataSource.insertCurrentWeather(weather)
    }

    override suspend fun insertFavoriteWeather(current: Weather, hourly: Weather, daily: Weather) {
        localDataSource.insertFavoriteWeather(current, hourly, daily)
    }

    override suspend fun insertFavoriteWeather(weather: Weather) {
        localDataSource.insertFavoriteWeather(weather)
    }

    override suspend fun getAllLocalWeathers(): List<Weather> {
        return localDataSource.getAllLocalWeathers().sortedWith(
            compareBy({ it.isFavorite == Constant.TRUE }, { it.city })
        )
    }

    override suspend fun getLocalWeather(id: String): Weather? {
        return localDataSource.getLocalWeather(id)
    }

    override suspend fun deleteWeather(id: String) {
        localDataSource.deleteWeather(id)
    }

    override fun fetchWeatherForecastCurrent(
        latitude: Double,
        longitude: Double
    ): Flow<Weather> {
        return flow {
            emit(remoteDataSource.fetchWeatherForecastCurrent(latitude, longitude).toWeather())
        }
    }

    override fun fetchWeatherForecastHourly(
        latitude: Double,
        longitude: Double
    ): Flow<Weather> {
        return flow {
            emit(remoteDataSource.fetchWeatherForecastHourly(latitude, longitude).toWeather())
        }
    }

    override fun fetchWeatherForecastDaily(
        latitude: Double,
        longitude: Double
    ): Flow<Weather> {
        return flow {
            emit(remoteDataSource.fetchWeatherForecastDaily(latitude, longitude).toWeather())
        }
    }
}
