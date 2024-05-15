package com.example.weather.ui.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.data.model.entity.Weather
import com.example.weather.databinding.LayoutItemFavoriteBinding
import com.example.weather.utils.Constant
import com.example.weather.utils.Utils
import com.example.weather.utils.ext.kelvinToCelsius
import com.example.weather.utils.ext.offsetToUTC
import com.example.weather.utils.ext.unixTimestampToHourString
import com.example.weather.utils.ext.unixTimestampToTimeString
import com.example.weather.utils.listener.OnItemClickListener

class FavoriteAdapter(var listener: OnItemClickListener) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    private val mListWeathers by lazy { mutableListOf<Weather>() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(LayoutItemFavoriteBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int {
        return mListWeathers.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(mListWeathers[position])
        holder.buttonDelete.setOnClickListener {
            listener.onItemClickListener(holder.itemView, position, Constant.DELETE)
        }
        holder.item.setOnClickListener {
            listener.onItemClickListener(holder.itemView, position, Constant.DETAIL)
        }
    }

    fun updateData(listData: List<Weather>?) {
        mListWeathers.clear()
        if (listData != null) {
            mListWeathers.addAll(listData)
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: LayoutItemFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {
        val buttonDelete = binding.imgDelete
        val item = binding.itemFavorite

        @RequiresApi(Build.VERSION_CODES.O)
        fun bindData(newItem: Weather) {
            binding.tvTemperature.text = newItem.weatherCurrent?.temperature?.kelvinToCelsius().toString()
            binding.tvCity.text = newItem.city
            binding.tvTime.text = newItem.weatherCurrent?.dateTime?.unixTimestampToTimeString()
            binding.tvGmt.text = newItem.timeZone?.offsetToUTC() ?: ""
            binding.tvDescription.text = newItem.weatherCurrent?.weatherDescription

            val time = newItem.weatherCurrent?.dateTime?.unixTimestampToHourString()?.toInt()
            if (time != null) {
                newItem.weatherCurrent.weatherMainCondition?.let { mainCondition ->
                    Utils.getIcon(mainCondition, time)?.let { image ->
                        binding.imgWeatherIcon.setImageResource(image)
                    }
                }
            }
        }
    }
}
