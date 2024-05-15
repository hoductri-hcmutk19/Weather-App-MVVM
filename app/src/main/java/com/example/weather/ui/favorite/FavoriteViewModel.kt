package com.example.weather.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.data.model.entity.Weather
import com.example.weather.data.repository.WeatherRepository
import com.example.weather.utils.Constant
import com.example.weather.utils.livedata.SingleLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel(private val weatherRepository: WeatherRepository) : ViewModel() {

    private val _weatherList = SingleLiveData<List<Weather>>()
    val weatherList: LiveData<List<Weather>> = _weatherList

    fun getAllFavorite() {
        viewModelScope.launch(Dispatchers.IO) {
            val weatherList = weatherRepository.getAllLocalWeathers().toMutableList()
            weatherList.removeIf { it.isFavorite == Constant.FALSE }
            _weatherList.postValue(weatherList)
        }
    }

    fun removeFavoriteWeather(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.deleteWeather(id)
        }
    }
}
