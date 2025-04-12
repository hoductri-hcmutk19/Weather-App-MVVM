package com.app.hiweather.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.hiweather.data.model.entity.Weather
import com.app.hiweather.data.repository.WeatherRepository
import com.app.hiweather.utils.Constant
import com.app.hiweather.utils.livedata.SingleLiveData
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
