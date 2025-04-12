package com.app.hiweather.di

import com.app.hiweather.ui.MainViewModel
import com.app.hiweather.ui.SharedViewModel
import com.app.hiweather.ui.favorite.FavoriteViewModel
import com.app.hiweather.ui.home.WeatherViewModel
import com.app.hiweather.ui.map.MapViewModel
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
