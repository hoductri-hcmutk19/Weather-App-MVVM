package com.example.weather.ui.home

import androidx.lifecycle.ViewModel
import com.example.weather.data.model.CurrentResponse
import com.example.weather.utils.livedata.SingleLiveData

class HomeViewModel : ViewModel() {
    val weather = SingleLiveData<CurrentResponse>()
}
