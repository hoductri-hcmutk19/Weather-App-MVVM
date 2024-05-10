package com.example.weather.ui

import android.annotation.SuppressLint
import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.weather.R
import com.example.weather.databinding.ActivityMainBinding
import com.example.weather.ui.home.WeatherFragment
import com.example.weather.utils.Constant
import com.example.weather.utils.PermissionUtils
import com.example.weather.utils.Utils
import com.example.weather.utils.addFragmentToActivity
import com.example.weather.utils.listener.OnFetchListener
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.example.weather.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity :
    BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate),
    OnFetchListener {
    private var mCurrentLocation: Location? = null
    private var mLastLocation: Location? = null

    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    override val viewModel: MainViewModel by viewModel()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun initialize() {
        onDeviceOffline()
        onLocationRequest()
    }

    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()

        mFusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                mLastLocation = location
                initWeatherView(location)
            }

        PermissionUtils.getLastLocation(
            this,
            this,
            PermissionUtils.isLocationEnabled(this)
        )
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun onDeviceOffline() {
        requestPermissions()
    }

    private fun onLocationRequest() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestPermissions() {
        if (!PermissionUtils.checkPermissions(this)) {
            PermissionUtils.requestPermissions(this)
        }
    }

    private fun initWeatherView(location: Location?) {
        location?.let { location ->
            addFragmentToActivity(
                supportFragmentManager,
                WeatherFragment.newInstance(location.latitude, location.longitude),
                R.id.container
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionUtils.onRequestPermissionResult(
            requestCode,
            grantResults,
            this
        )
    }

    override fun onRestart() {
        super.onRestart()
        initWeatherView(mCurrentLocation)
    }

    override fun onDataLocation(location: Location?) {
        this.mCurrentLocation = location
        val distance = mCurrentLocation?.let { currentLocation ->
            mLastLocation?.let { lastLocation ->
                Utils.distanceBetweenPoints(
                    currentLocation.latitude,
                    currentLocation.longitude,
                    lastLocation.latitude,
                    lastLocation.longitude
                )
            }
        }
        if (distance != null) {
            if (distance > Constant.MIN_DISTANCE_FIRST_TRIGGER) {
                initWeatherView(location)
            }
        } else {
            initWeatherView(location)
        }
    }
}
