package com.example.weather.ui.home

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.data.model.entity.Weather
import com.example.weather.data.repository.WeatherRepository
import com.example.weather.utils.livedata.SingleLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.launch

class WeatherViewModel(private val weatherRepository: WeatherRepository) : ViewModel() {
    private var mCurrent: Weather? = null
    private var mHourly: Weather? = null
    private var mDaily: Weather? = null
    private var mIsDataFetching = false

    var isLoading = SingleLiveData<Boolean>()
    var listWeather = SingleLiveData<List<Weather>>()
    var currentWeather = SingleLiveData<Weather?>()
    var isDBEmpty: SingleLiveData<Boolean> = SingleLiveData<Boolean>().apply { value = false }

    fun getWeather(
        latitude: Double,
        longitude: Double,
        position: Int,
        isNetworkEnable: Boolean = false,
        isCurrent: Boolean = false
    ) {
        if (isNetworkEnable) {
            getRemoteWeather(latitude, longitude, isCurrent)
        } else {
            getLocalWeather(position)
        }
    }

    private fun getRemoteWeather(latitude: Double, longitude: Double, isCurrent: Boolean) {
        if (mIsDataFetching) {
            return
        }
        mIsDataFetching = true

        isLoading.value = true
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

                insertWeatherIfDataAvailable(current, hourly, daily, isCurrent)
            } catch (e: NetworkErrorException) {
                Log.e("WeatherViewModel", "Exception occurred", e)
            } finally {
                mIsDataFetching = false
            }
        }
    }

    private fun insertWeatherIfDataAvailable(
        current: Weather?,
        hourly: Weather?,
        daily: Weather?,
        isCurrent: Boolean
    ) {
        if (current != null && hourly != null && daily != null) {
            val idWeather = current.id
            if (isCurrent) {
                viewModelScope.launch(Dispatchers.IO) {
                    weatherRepository.insertCurrentWeather(current, hourly, daily)
                    onGetDataAndSendToView(idWeather)
                }
            } else {
                viewModelScope.launch(Dispatchers.IO) {
                    weatherRepository.insertFavoriteWeather(current, hourly, daily)
                    onGetDataAndSendToView(idWeather)
                }
            }
        }
    }

    private fun onGetDataAndSendToView(idWeather: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val current = weatherRepository.getLocalWeather(idWeather)
            val listWeatherLocal = weatherRepository.getAllLocalWeathers()

            listWeather.postValue(listWeatherLocal)
            if (current != null) {
                isLoading.postValue(false)
                currentWeather.postValue(current)
            }
            mIsDataFetching = false
        }
    }

    private fun getLocalWeather(position: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val listWeatherLocal = weatherRepository.getAllLocalWeathers()
            isLoading.postValue(false)
            listWeather.postValue(listWeatherLocal)
            if (listWeatherLocal.isNotEmpty()) {
                isDBEmpty.postValue(false)
                currentWeather.postValue(listWeatherLocal[position])
            } else {
                isDBEmpty.postValue(true)
            }
        }
    }
}
