package com.example.weather.ui.home

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import com.example.weather.R
import com.example.weather.data.model.entity.Weather
import com.example.weather.databinding.FragmentWeatherBinding
import com.example.weather.utils.Constant
import com.example.weather.utils.Utils
import com.example.weather.utils.Utils.getIcon
import com.example.weather.utils.ext.kelvinToCelsius
import com.example.weather.utils.ext.mpsToKmph
import com.example.weather.utils.ext.unixTimestampToDateTimeString
import com.example.weather.utils.ext.unixTimestampToHourString
import com.example.weather.base.BaseFragment
import com.example.weather.ui.SharedViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

@Suppress("TooManyFunctions")
class WeatherFragment :
    BaseFragment<FragmentWeatherBinding>(FragmentWeatherBinding::inflate),
    AdapterView.OnItemSelectedListener {
    private var mLatitude: Double = 0.0
    private var mLongitude: Double = 0.0
    private var mWeatherCurrent: Weather? = null
    private var mIsNetworkEnable: Boolean = false
    private var mPosition: Int = 0
    private var mListItemSpinner: ArrayList<String> = arrayListOf()
    private var mSpinnerCheck: Int = 0
    private var mIsAppStarted: Boolean = false

    private lateinit var mWeatherList: List<Weather>
    private lateinit var mSpinnerAdapter: ArrayAdapter<String>

    override val viewModel: WeatherViewModel by viewModel()
    override val sharedViewModel: SharedViewModel by activityViewModel()

    override fun initView() {
        mSpinnerAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, mListItemSpinner)
        mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.layoutHeader.spinner.adapter = mSpinnerAdapter

        binding.layoutHeader.refreshIcon.setOnClickListener {
            if (mWeatherList.isNotEmpty()) {
                onRefresh()
            }
        }
        binding.layoutWeatherBasic.cardView.setOnClickListener {
            // TODO implement later
        }
        binding.btnNavMap.setOnClickListener {
            // TODO implement later
        }

        val initialSelectedPosition = binding.layoutHeader.spinner.selectedItemPosition
        binding.layoutHeader.spinner.setSelection(initialSelectedPosition, false)
        binding.layoutHeader.spinner.onItemSelectedListener = this
    }

    @Suppress("NestedBlockDepth")
    override fun initData() {
        sharedViewModel.isNetworkAvailable.observe(viewLifecycleOwner) { isNetworkEnable ->
            mIsNetworkEnable = isNetworkEnable
            Toast.makeText(context, "$isNetworkEnable", Toast.LENGTH_LONG).show()
        }

        //Toast.makeText(context, "$mIsNetworkEnable", Toast.LENGTH_LONG).show()
        arguments?.let {
            mLatitude = it.getDouble(Constant.LATITUDE_KEY)
            mLongitude = it.getDouble(Constant.LONGITUDE_KEY)
            if (!mIsAppStarted) {
                mIsAppStarted = true
                viewModel.getWeather(mLatitude, mLongitude, mPosition, mIsNetworkEnable, true)
            } else {
                if (mPosition != 0) { // Fix spinner auto click first item
                    mSpinnerCheck = 0
                }
                if (mIsNetworkEnable) {
                    val dateTime = mWeatherList[mPosition].weatherCurrent?.dateTime
                    if (dateTime?.let { dateTime -> Utils.checkTimeInterval(dateTime) } == true) {
                        onRefresh()
                    } else {
                        val latitude = mWeatherList[mPosition].latitude
                        val longitude = mWeatherList[mPosition].longitude
                        latitude?.let { lat ->
                            longitude?.let { lon ->
                                viewModel.getWeather(lat, lon, mPosition)
                            }
                        }
                    }
                } else {
                    onRefresh()
                }
            }
        }
    }

    override fun bindData() {
        viewModel.currentWeather.observe(this) { currentWeather ->
            if (currentWeather != null) {
                onGetCurrentWeatherSuccess(currentWeather)
            }
        }

        viewModel.listWeather.observe(this) { listWeather ->
            onGetSpinnerList(listWeather)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            onProgressLoading(isLoading)
        }

        viewModel.isDBEmpty.observe(this) { isDBEmpty ->
            if (isDBEmpty) {
                onDBEmpty()
            }
        }
    }

    @Suppress("NestedBlockDepth")
    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        if (mSpinnerCheck++ > 0) { // fix spinner auto click at default position (pos = 0) when it initialization
            mPosition = pos
            val itemLatitude = mWeatherList[pos].latitude
            val itemLongitude = mWeatherList[pos].longitude
            val itemID = mWeatherList[pos].id
            val currentID = mWeatherCurrent?.id
            val isCurrent = itemID == currentID

            if (itemLatitude != null && itemLongitude != null) {
                val dateTime = mWeatherList[pos].weatherCurrent?.dateTime
                if (dateTime?.let { dateTime -> Utils.checkTimeInterval(dateTime) } == true) {
                    viewModel.getWeather(
                        itemLatitude,
                        itemLongitude,
                        mPosition,
                        mIsNetworkEnable,
                        isCurrent
                    )
                } else {
                    viewModel.getWeather(itemLatitude, itemLongitude, mPosition)
                }
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback.
        // TODO implement later
    }

    private fun onGetSpinnerList(weatherList: List<Weather>) {
        mWeatherList = weatherList
        val newListItemSpinner: ArrayList<String> = arrayListOf()
        weatherList.forEach { mWeather ->
            mWeather.city?.let { cityName ->
                newListItemSpinner.add(cityName)
            }
        }
        mListItemSpinner.apply {
            clear()
            addAll(newListItemSpinner)
        }
        mSpinnerAdapter.notifyDataSetChanged()
    }

    private fun onProgressLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.outputGroup.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.outputGroup.visibility = View.VISIBLE
        }
    }

    private fun onGetCurrentWeatherSuccess(weather: Weather) {
        if (weather.isFavorite == Constant.FALSE) {
            mWeatherCurrent = weather
        }
        bindDataToView(weather)
    }

    @Suppress("NestedBlockDepth")
    private fun bindDataToView(weather: Weather) {
        binding.layoutHeader.locationIcon.visibility =
            if (weather.isFavorite == Constant.FALSE) View.VISIBLE else View.GONE

        weather.weatherCurrent?.let { weatherCurrent ->
            val time = weatherCurrent.dateTime?.unixTimestampToHourString()?.toInt()
            if (time != null) {
                weatherCurrent.weatherMainCondition?.let { mainCondition ->
                    getIcon(mainCondition, time)?.let { image ->
                        binding.icWeather.setImageResource(image)
                    }
                }
            }
            binding.layoutWeatherBasic.tvDateTime.text =
                "Today, " + weatherCurrent.dateTime?.unixTimestampToDateTimeString()
            binding.layoutWeatherBasic.tvTemperature.text =
                weatherCurrent.temperature?.kelvinToCelsius().toString()
            binding.layoutWeatherBasic.tvDescription.text = weatherCurrent.weatherDescription
            binding.layoutWeatherBasic.layoutBasicDetail.tvWindValue.text =
                weatherCurrent.windSpeed?.mpsToKmph().toString() + " km/h"
            binding.layoutWeatherBasic.layoutBasicDetail.tvHumidityValue.text =
                weatherCurrent.humidity.toString() + " %"
        }
    }

    private fun onDBEmpty() {
        if (mIsNetworkEnable) {
            viewModel.getWeather(
                mLatitude,
                mLongitude,
                mPosition,
                isNetworkEnable = true,
                isCurrent = true
            )
        } else {
            binding.tvError.visibility = View.VISIBLE
            binding.outputGroup.visibility = View.GONE
            binding.layoutHeader.refreshIcon.visibility = View.GONE
            binding.layoutHeader.spinner.visibility = View.GONE
        }
    }

    private fun onRefresh() {
        val itemID = mWeatherList[mPosition].id
        val currentID = mWeatherCurrent?.id
        val isCurrent = itemID == currentID
        mWeatherList[mPosition].latitude?.let { latitude ->
            mWeatherList[mPosition].longitude?.let { longitude ->
                viewModel.getWeather(
                    latitude,
                    longitude,
                    mPosition,
                    mIsNetworkEnable,
                    isCurrent
                )
            }
        }
    }

    companion object {
        fun newInstance(latitude: Double, longitude: Double) = WeatherFragment().apply {
            arguments = bundleOf(
                Constant.LATITUDE_KEY to latitude,
                Constant.LONGITUDE_KEY to longitude
            )
        }
    }
}
