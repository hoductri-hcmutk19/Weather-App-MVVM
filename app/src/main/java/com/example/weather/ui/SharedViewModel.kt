package com.example.weather.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    var isNetworkAvailable = MutableLiveData<Boolean>()

    fun checkNetwork(isEnable: Boolean) {
        isNetworkAvailable.postValue(isEnable)
    }
}
