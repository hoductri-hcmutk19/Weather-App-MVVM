package com.app.hiweather.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.hiweather.data.model.entity.WeatherBasic
import com.app.hiweather.databinding.LayoutItemDailyBinding
import com.app.hiweather.utils.Utils.getIcon
import com.app.hiweather.utils.ext.kelvinToCelsius
import com.app.hiweather.utils.ext.unixTimestampToDateMonthString
import com.app.hiweather.utils.ext.unixTimestampToHourString

class DailyAdapter : RecyclerView.Adapter<DailyAdapter.ViewHolder>() {

    private val mWeathers by lazy { mutableListOf<WeatherBasic>() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(LayoutItemDailyBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int {
        return mWeathers.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(mWeathers[position])
    }

    fun updateData(listData: MutableList<WeatherBasic>) {
        mWeathers.clear()
        mWeathers.addAll(listData)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: LayoutItemDailyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(newItem: WeatherBasic) {
            binding.tvDailyTime.text = newItem.dateTime?.unixTimestampToDateMonthString()
            val time = newItem.dateTime?.unixTimestampToHourString()?.toInt()
            if (time != null) {
                newItem.weatherMainCondition?.let { mainCondition ->
                    getIcon(mainCondition, time)?.let { image ->
                        binding.imgDailyIcon.setImageResource(image)
                    }
                }
            }
            binding.tvDailyTemperature.text = newItem.temperature?.kelvinToCelsius().toString()
        }
    }
}
