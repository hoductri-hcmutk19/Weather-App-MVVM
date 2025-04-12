package com.app.hiweather

import android.app.Application
import com.app.hiweather.di.AppModule
import com.app.hiweather.di.DataSourceModule
import com.app.hiweather.di.NetworkModule
import com.app.hiweather.di.RepositoryModule
import com.app.hiweather.di.ViewModelModule
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
