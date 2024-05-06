package com.example.weather.ui

import com.example.weather.R
import com.example.weather.databinding.ActivityMainBinding
import com.example.weather.ui.home.HomeFragment
import com.sun.android.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    override val viewModel: MainViewModel by viewModel()

    override fun initialize() {
        supportFragmentManager
            .beginTransaction()
            .addToBackStack(HomeFragment::javaClass.name)
            .replace(R.id.container, HomeFragment())
            .commit()
    }
}
