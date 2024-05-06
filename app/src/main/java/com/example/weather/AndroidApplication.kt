package com.example.weather

import android.app.Application
import com.example.weather.di.AppModule
import com.example.weather.di.DataSourceModule
import com.example.weather.di.NetworkModule
import com.example.weather.di.RepositoryModule
import com.example.weather.di.ViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.core.context.startKoin

class AndroidApplication : Application() {

    private val rootModule =
        listOf(AppModule, NetworkModule, DataSourceModule, RepositoryModule, ViewModelModule)

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@AndroidApplication)
            androidFileProperties()
            modules(rootModule)
        }
    }
}
