package com.app.hiweather.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.app.hiweather.R
import com.app.hiweather.base.BaseFragment
import com.app.hiweather.data.model.entity.Weather
import com.app.hiweather.databinding.FragmentMapBinding
import com.app.hiweather.ui.SharedViewModel
import com.app.hiweather.ui.detail.DetailFragment
import com.app.hiweather.ui.favorite.FavoriteFragment
import com.app.hiweather.ui.home.WeatherFragment
import com.app.hiweather.ui.info.InfoFragment
import com.app.hiweather.utils.Constant
import com.app.hiweather.utils.Utils
import com.app.hiweather.utils.Utils.getIcon
import com.app.hiweather.utils.ext.kelvinToCelsius
import com.app.hiweather.utils.ext.unixTimestampToHourString
import com.app.hiweather.utils.goBackFragment
import com.app.hiweather.utils.replaceFragmentToActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException

@Suppress("TooManyFunctions")
class MapFragment :
    BaseFragment<FragmentMapBinding>(FragmentMapBinding::inflate),
    OnMapReadyCallback {

    private var mGoogleMap: GoogleMap? = null
    private var mCurrentLatitude: Double = 0.0
    private var mCurrentLongitude: Double = 0.0
    private var mSelectedLatitude: Double = 0.0
    private var mSelectedLongitude: Double = 0.0
    private var mWeather: Weather? = null
    private var mIndex: Int = 0
    private var mIsNetworkEnable: Boolean = false
    private var mIsWeatherLocalExist: Boolean = true
    private var mFusedLocationClient: FusedLocationProviderClient? = null

    override val viewModel: MapViewModel by viewModel()
    override val sharedViewModel: SharedViewModel by activityViewModel()

    @Suppress("LongMethod", "ComplexMethod")
    override fun initView() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.btnCurrentLocation.setOnClickListener {
            if (mIsNetworkEnable) {
                if (context?.let { context ->
                    ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                } == PackageManager.PERMISSION_GRANTED
                ) {
                    mFusedLocationClient?.lastLocation
                        ?.addOnSuccessListener { location: Location? ->
                            if (location != null) {
                                mCurrentLatitude = location.latitude
                                mCurrentLongitude = location.longitude
                            }
                            mSelectedLatitude = mCurrentLatitude
                            mSelectedLongitude = mCurrentLongitude
                            viewModel.getWeatherRemote(mCurrentLatitude, mCurrentLongitude)
                            moveToLocation(mCurrentLatitude, mCurrentLongitude)
                        }
                }
            }
        }

        binding.mapSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val location = binding.mapSearch.query.toString()
                var addressList: MutableList<Address>? = null
                val geocoder = context?.let { Geocoder(it) }
                try {
                    addressList = geocoder?.getFromLocationName(location, 1)
                } catch (e: IOException) {
                    Log.e("MapFragment", "Exception occurred", e)
                }
                if (addressList != null) {
                    if (addressList.isNotEmpty()) {
                        mSelectedLatitude = addressList[0].latitude
                        mSelectedLongitude = addressList[0].longitude
                        viewModel.getWeatherRemote(mSelectedLatitude, mSelectedLongitude)
                        moveToLocation(mSelectedLatitude, mSelectedLongitude)
                    } else {
                        Toast.makeText(
                            context,
                            getString(R.string.location_not_exist),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        binding.btnFavoriteLocation.setOnClickListener {
            activity?.let {
                (it as AppCompatActivity).replaceFragmentToActivity(
                    it.supportFragmentManager,
                    FavoriteFragment.newInstance(),
                    R.id.container
                )
            }
        }

        binding.btnAppInfo.setOnClickListener {
            activity?.let {
                (it as AppCompatActivity).replaceFragmentToActivity(
                    it.supportFragmentManager,
                    InfoFragment.newInstance(),
                    R.id.container
                )
            }
        }

        binding.icBack.setOnClickListener {
            viewModel.getLocalWeatherList()
            viewModel.weatherList.observe(this) { weatherList ->
                if (mIndex >= weatherList.size && weatherList.isNotEmpty()) {
                    activity?.let {
                        (it as AppCompatActivity).replaceFragmentToActivity(
                            it.supportFragmentManager,
                            WeatherFragment.newInstance(
                                weatherList[0].latitude!!,
                                weatherList[0].longitude!!,
                                mIsNetworkEnable
                            ),
                            R.id.container
                        )
                    }
                } else {
                    goBackFragment()
                }
            }
        }

        binding.layoutWeatherMap.cardViewMap.setOnClickListener {
            activity?.let {
                mWeather?.let { weather -> DetailFragment.newInstance(weather) }
                    ?.let { detailFragment ->
                        (it as AppCompatActivity).replaceFragmentToActivity(
                            it.supportFragmentManager,
                            detailFragment,
                            R.id.container
                        )
                    }
            }
        }

        binding.layoutWeatherMap.icStar.setOnClickListener {
            binding.layoutWeatherMap.icStar.isClickable = false
            if (mWeather?.isFavorite == Constant.FALSE) {
                binding.layoutWeatherMap.icStar.setImageResource(R.drawable.ic_star_yellow)
                mWeather?.let { weather -> viewModel.insertFavoriteWeather(weather) }
                mWeather?.isFavorite = Constant.TRUE
                binding.layoutWeatherMap.icStar.isClickable = true
            } else {
                binding.layoutWeatherMap.icStar.setImageResource(R.drawable.ic_star_white)
                val id = mWeather?.id
                if (id != null) {
                    viewModel.removeFavoriteWeather(id)
                }
                mWeather?.isFavorite = Constant.FALSE
                binding.layoutWeatherMap.icStar.isClickable = true
            }
        }
    }

    @Suppress("NestedBlockDepth")
    override fun initData() {
        sharedViewModel.isNetworkAvailable.observe(viewLifecycleOwner) { isNetworkEnable ->
            mIsNetworkEnable = isNetworkEnable
        }

        mFusedLocationClient = context?.let { LocationServices.getFusedLocationProviderClient(it) }

        arguments?.let { args ->
            if (mSelectedLatitude == 0.0 && mSelectedLongitude == 0.0) {
                mWeather = args.getParcelable(Constant.WEATHER_KEY)
                mIndex = args.getInt(Constant.INDEX_KEY)
                mSelectedLatitude = mWeather?.latitude ?: 0.0
                mSelectedLongitude = mWeather?.longitude ?: 0.0
            }
            val dateTime = mWeather?.weatherCurrent?.dateTime
            val isDateTimeValid = dateTime?.let { Utils.checkTimeInterval(it) } == true
            if (isDateTimeValid && mIsNetworkEnable) {
                viewModel.getWeatherRemote(mSelectedLatitude, mSelectedLongitude)
            } else {
                val id = mWeather?.id
                if (mWeather?.isFavorite == Constant.TRUE && id != null) {
                    viewModel.checkWeatherLocal(id)
                    if (!mIsWeatherLocalExist) {
                        mWeather?.isFavorite = Constant.FALSE
                    }
                }
                mWeather?.let { weather -> bindDataToView(weather) }
            }
        }
    }

    override fun bindData() {
        viewModel.weather.observe(this) { weather ->
            if (weather != null) {
                mWeather = weather
                mWeather?.let { bindDataToView(it) }
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            onProgressLoading(isLoading)
        }

        viewModel.isExist.observe(this) { isExist ->
            mIsWeatherLocalExist = isExist
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        mGoogleMap?.mapType = GoogleMap.MAP_TYPE_HYBRID

        mGoogleMap?.setOnMapLongClickListener { position ->
            if (mIsNetworkEnable) {
                mGoogleMap?.clear()
                mSelectedLatitude = position.latitude
                mSelectedLongitude = position.longitude
                viewModel.getWeatherRemote(mSelectedLatitude, mSelectedLongitude)
                addMarker(position)
            }
        }
        mGoogleMap?.setOnMarkerClickListener { marker ->
            marker.remove()
            false
        }

        zoomOnMap(LatLng(mSelectedLatitude, mSelectedLongitude))
        addMarker(LatLng(mSelectedLatitude, mSelectedLongitude))
    }

    private fun addMarker(position: LatLng): Marker? {
        return mGoogleMap?.addMarker(
            MarkerOptions()
                .position(position)
                .title("Custom Marker")
                .draggable(true)
                .icon(
                    BitmapDescriptorFactory.fromBitmap(
                        Bitmap.createScaledBitmap(
                            BitmapFactory.decodeResource(resources, R.drawable.ic_marker),
                            Constant.MARKER_SIZE,
                            Constant.MARKER_SIZE,
                            false
                        )
                    )
                )
        )
    }

    private fun zoomOnMap(latLng: LatLng) {
        val newLatLngZoom = CameraUpdateFactory.newLatLngZoom(latLng, Constant.ZOOM_RATIO)
        mGoogleMap?.animateCamera(newLatLngZoom)
    }

    private fun moveToLocation(latitude: Double, longitude: Double) {
        mGoogleMap?.clear()
        zoomOnMap(LatLng(latitude, longitude))
        addMarker(LatLng(latitude, longitude))
    }

    private fun onProgressLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressbar.visibility = View.VISIBLE
        } else {
            binding.progressbar.visibility = View.GONE
        }
    }

    @Suppress("NestedBlockDepth")
    private fun bindDataToView(weather: Weather) {
        weather.weatherCurrent?.let { weatherCurrent ->
            val time = weatherCurrent.dateTime?.unixTimestampToHourString()?.toInt()
            if (time != null) {
                weatherCurrent.weatherMainCondition?.let { mainCondition ->
                    getIcon(mainCondition, time)?.let { image ->
                        binding.layoutWeatherMap.ivMainCondition.setImageResource(image)
                    }
                }
            }
            binding.layoutWeatherMap.tvMainCondition.text = weatherCurrent.weatherMainCondition
            binding.layoutWeatherMap.tvTemperature.text =
                weatherCurrent.temperature?.kelvinToCelsius().toString()
        }
        binding.layoutWeatherMap.tvLocation.text = weather.getLocation()
        val starResource =
            if (weather.isFavorite == Constant.TRUE) R.drawable.ic_star_yellow else R.drawable.ic_star_white
        binding.layoutWeatherMap.icStar.setImageResource(starResource)
    }

    companion object {
        fun newInstance(weather: Weather, index: Int) =
            MapFragment().apply {
                arguments = bundleOf(
                    Constant.WEATHER_KEY to weather,
                    Constant.INDEX_KEY to index
                )
            }
    }
}
