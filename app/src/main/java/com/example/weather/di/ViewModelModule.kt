package com.example.weather.di

import com.example.weather.ui.MainViewModel
import com.example.weather.ui.SharedViewModel
import com.example.weather.ui.favorite.FavoriteViewModel
import com.example.weather.ui.home.WeatherViewModel
import com.example.weather.ui.map.MapViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val ViewModelModule: Module = module {
    viewModel { MainViewModel() }
    viewModel { SharedViewModel() }
    viewModel { WeatherViewModel(get()) }
    viewModel { MapViewModel(get()) }
    viewModel { FavoriteViewModel(get()) }
}
