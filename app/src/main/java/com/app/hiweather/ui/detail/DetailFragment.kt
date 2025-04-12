package com.app.hiweather.ui.detail

import android.view.View
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.children
import com.app.hiweather.R
import com.app.hiweather.base.BaseFragment
import com.app.hiweather.data.model.entity.Weather
import com.app.hiweather.data.model.entity.WeatherBasic
import com.app.hiweather.databinding.FragmentDetailBinding
import com.app.hiweather.ui.SharedViewModel
import com.app.hiweather.ui.adapter.DailyAdapter
import com.app.hiweather.ui.adapter.HourlyAdapter
import com.app.hiweather.ui.home.WeatherViewModel
import com.app.hiweather.utils.Constant
import com.app.hiweather.utils.ext.unixTimestampToDateString
import com.app.hiweather.utils.ext.unixTimestampToDateYearString
import com.app.hiweather.utils.goBackFragment
import com.app.hiweather.utils.listener.OnItemClickListener
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailFragment :
    BaseFragment<FragmentDetailBinding>(FragmentDetailBinding::inflate),
    OnItemClickListener {

    private val hourlyAdapter: HourlyAdapter by lazy {
        HourlyAdapter(this, requireContext())
    }
    private val dailyAdapter: DailyAdapter by lazy {
        DailyAdapter()
    }
    private var mHourlyPosition = 0
    private lateinit var mWeather: Weather

    override val viewModel: WeatherViewModel by viewModel()
    override val sharedViewModel: SharedViewModel by activityViewModel()

    override fun initView() {
        binding.layoutHourly.recyclerViewHourly.adapter = hourlyAdapter
        binding.layoutDaily.recyclerViewDaily.adapter = dailyAdapter
        binding.layoutHeader.icBack.setOnClickListener {
            goBackFragment()
        }
    }

    override fun initData() {
        arguments?.let { args ->
            mWeather = args.getParcelable(Constant.WEATHER_KEY)!!
            bindDataToView(mWeather)
        }
    }

    override fun bindData() {
        // TODO implement later
    }

    override fun onItemClickListener(view: View, position: Int, action: String) {
        mHourlyPosition = position
        binding.layoutHourly.recyclerViewHourly.children.iterator().forEach { item ->
            item as CardView
            item.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.transparent
                )
            )
        }
        view as CardView
        view.setCardBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.cardBackgroundOpacity
            )
        )

        val currentDay = mWeather.weatherCurrent?.dateTime?.unixTimestampToDateString()?.toInt()
        val itemHourlyClickDay =
            mWeather.weatherHourlyList?.get(mHourlyPosition)?.dateTime?.unixTimestampToDateString()?.toInt()
        if (itemHourlyClickDay != null) {
            binding.layoutHourly.currentDate.text =
                mWeather.weatherHourlyList?.get(mHourlyPosition)?.dateTime?.unixTimestampToDateYearString()
            val distance = itemHourlyClickDay - currentDay!!
            when (distance) {
                0 -> binding.layoutHourly.today.text = getString(R.string.today)
                1 -> binding.layoutHourly.today.text = getString(R.string.tomorrow)
                2 -> binding.layoutHourly.today.text = getString(R.string.after_tomorrow)
            }
        }
    }

    private fun bindDataToView(weather: Weather) {
        weather.weatherHourlyList?.let {
            hourlyAdapter.updateData(it as MutableList<WeatherBasic>)
        }
        weather.weatherDailyList?.let {
            dailyAdapter.updateData(it as MutableList<WeatherBasic>)
        }
        binding.layoutHourly.currentDate.text =
            mWeather.weatherCurrent?.dateTime?.unixTimestampToDateYearString() ?: ""
    }

    companion object {
        fun newInstance(weather: Weather) =
            DetailFragment().apply {
                arguments = bundleOf(
                    Constant.WEATHER_KEY to weather
                )
            }
    }
}
