package com.example.weather.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentFilter
import android.location.Location
import android.net.ConnectivityManager
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.weather.R
import com.example.weather.base.BaseActivity
import com.example.weather.databinding.ActivityMainBinding
import com.example.weather.ui.home.WeatherFragment
import com.example.weather.utils.Constant
import com.example.weather.utils.PermissionUtils
import com.example.weather.utils.Utils
import com.example.weather.utils.addFragmentToActivity
import com.example.weather.utils.listener.OnFetchListener
import com.example.weather.utils.network.NetworkStateReceiver
import com.example.weather.utils.network.NetworkStateReceiver.NetworkStateReceiverListener
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.koin.androidx.viewmodel.ext.android.viewModel

@Suppress("TooManyFunctions")
class MainActivity :
    BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate),
    OnFetchListener,
    NetworkStateReceiverListener {
    private var mCurrentLocation: Location? = null
    private var mLastLocation: Location? = null
    private var mNetworkStateReceiver: NetworkStateReceiver? = null
    private var mIsNetworkEnable: Boolean = true

    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    override val viewModel: MainViewModel by viewModel()
    override val sharedViewModel: SharedViewModel by viewModel()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun initialize() {
        requestPermissions()
        onLocationRequest()
        startNetworkBroadcastReceiver(this)
    }

    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()
        registerNetworkBroadcastReceiver(this)

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

    override fun onPause() {
        unregisterNetworkBroadcastReceiver(this)
        super.onPause()
    }

    override fun onRestart() {
        super.onRestart()
        initWeatherView(mCurrentLocation)
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

    override fun networkAvailable() {
        if (!mIsNetworkEnable) {
            Toast.makeText(
                this,
                getString(R.string.message_network_connected),
                Toast.LENGTH_SHORT
            ).show()
        }
        sharedViewModel.checkNetwork(true)
        mIsNetworkEnable = true
    }

    override fun networkUnavailable() {
        Toast.makeText(
            this,
            getString(R.string.message_network_not_responding),
            Toast.LENGTH_SHORT
        ).show()
        sharedViewModel.checkNetwork(false)
        mIsNetworkEnable = false
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
                WeatherFragment.newInstance(location.latitude, location.longitude, mIsNetworkEnable),
                R.id.container
            )
        }
    }

    private fun startNetworkBroadcastReceiver(currentContext: Context) {
        mNetworkStateReceiver = NetworkStateReceiver()
        mNetworkStateReceiver?.let { receiver ->
            (currentContext as? NetworkStateReceiverListener)?.let {
                receiver.addListener(it)
            }
        }
        registerNetworkBroadcastReceiver(currentContext)
    }

    private fun registerNetworkBroadcastReceiver(currentContext: Context) {
        currentContext.registerReceiver(mNetworkStateReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    private fun unregisterNetworkBroadcastReceiver(currentContext: Context) {
        currentContext.unregisterReceiver(mNetworkStateReceiver)
    }
}
