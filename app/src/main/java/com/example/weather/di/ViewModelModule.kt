package com.example.weather.di

import com.example.weather.ui.MainViewModel
import com.example.weather.ui.SharedViewModel
import com.example.weather.ui.home.WeatherViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val ViewModelModule: Module = module {
    viewModel { MainViewModel() }
    viewModel { SharedViewModel() }
    viewModel { WeatherViewModel(get()) }
}
