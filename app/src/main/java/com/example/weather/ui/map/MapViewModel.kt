package com.example.weather.ui.map

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.data.model.entity.Weather
import com.example.weather.data.repository.WeatherRepository
import com.example.weather.utils.Constant
import com.example.weather.utils.livedata.SingleLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.launch

class MapViewModel(private val weatherRepository: WeatherRepository) : ViewModel() {
    private var mCurrent: Weather? = null
    private var mHourly: Weather? = null
    private var mDaily: Weather? = null
    private var mIsDataFetching = false

    private val _isLoading = SingleLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _weather = SingleLiveData<Weather?>()
    val weather: LiveData<Weather?> = _weather

    private val _isExist = SingleLiveData<Boolean>()
    val isExist: LiveData<Boolean> = _isExist

    fun insertFavoriteWeather(weather: Weather) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.insertFavoriteWeather(weather)
        }
    }

    fun removeFavoriteWeather(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.deleteWeather(id)
        }
    }

    fun getWeatherRemote(latitude: Double, longitude: Double) {
        if (mIsDataFetching) {
            return
        }
        mIsDataFetching = true

        _isLoading.value = true
        mCurrent = null
        mHourly = null
        mDaily = null

        viewModelScope.launch {
            try {
                val currentDeferred = async { weatherRepository.fetchWeatherForecastCurrent(latitude, longitude) }
                val hourlyDeferred = async { weatherRepository.fetchWeatherForecastHourly(latitude, longitude) }
                val dailyDeferred = async { weatherRepository.fetchWeatherForecastDaily(latitude, longitude) }

                val current = currentDeferred.await().singleOrNull()
                val hourly = hourlyDeferred.await().singleOrNull()
                val daily = dailyDeferred.await().singleOrNull()

                insertWeatherIfDataAvailable(current, hourly, daily)
            } catch (e: NetworkErrorException) {
                println(e)
            } finally {
                mIsDataFetching = false
            }
        }
    }

    fun checkWeatherLocal(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val weather = weatherRepository.getLocalWeather(id)
            if (weather != null) {
                _isExist.postValue(true)
            } else {
                _isExist.postValue(false)
            }
        }
    }

    private fun insertWeatherIfDataAvailable(
        current: Weather?,
        hourly: Weather?,
        daily: Weather?
    ) {
        if (current != null && hourly != null && daily != null) {
            val idWeather = current.id
            viewModelScope.launch(Dispatchers.IO) {
                val weather = weatherRepository.getLocalWeather(idWeather)
                if (weather != null && weather.isFavorite == Constant.TRUE) {
                    current.isFavorite = Constant.TRUE
                    weatherRepository.insertFavoriteWeather(current, hourly, daily)
                    sendToView(current, hourly, daily)
                } else if (weather != null && weather.isFavorite == Constant.FALSE) {
                    current.isFavorite = Constant.FALSE
                    weatherRepository.insertCurrentWeather(current, hourly, daily)
                    sendToView(current, hourly, daily)
                } else {
                    current.isFavorite = Constant.FALSE
                    sendToView(current, hourly, daily)
                }
            }
        }
    }

    private fun sendToView(current: Weather, hourly: Weather, daily: Weather) {
        _isLoading.postValue(false)
        current.weatherHourlyList = hourly.weatherHourlyList
        current.weatherDailyList = daily.weatherDailyList
        _weather.postValue(current)
        mIsDataFetching = false
    }
}
