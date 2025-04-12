package com.app.hiweather.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.hiweather.R
import com.app.hiweather.data.model.entity.WeatherBasic
import com.app.hiweather.databinding.LayoutItemHourlyBinding
import com.app.hiweather.utils.Utils.getIcon
import com.app.hiweather.utils.ext.kelvinToCelsius
import com.app.hiweather.utils.ext.unixTimestampToHourString
import com.app.hiweather.utils.ext.unixTimestampToTimeString
import com.app.hiweather.utils.listener.OnItemClickListener

class HourlyAdapter(var listener: OnItemClickListener, val context: Context) :
    RecyclerView.Adapter<HourlyAdapter.ViewHolder>() {

    private val mWeathers by lazy { mutableListOf<WeatherBasic>() }
    private var mPrePosition = -1

    var mPairedList: MutableList<Pair<WeatherBasic, Int>> = mutableListOf<Pair<WeatherBasic, Int>>().apply {
        mWeathers.forEach { add(it to 0) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(LayoutItemHourlyBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int {
        return mPairedList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(mPairedList[position])
    }

    fun updateData(listData: MutableList<WeatherBasic>) {
        mWeathers.clear()
        mWeathers.addAll(listData)
        mPairedList = mutableListOf<Pair<WeatherBasic, Int>>().apply {
            mWeathers.forEach { add(it to 0) }
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: LayoutItemHourlyBinding) :
        RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION && position != mPrePosition) {
                if (mPrePosition != -1) {
                    mPairedList[mPrePosition] = mPairedList[mPrePosition].copy(second = 0)
                }
                mPrePosition = position
                mPairedList[position] = mPairedList[position].copy(second = 1)
                if (view != null) {
                    listener.onItemClickListener(view, position)
                }
            }
        }

        fun bindData(pairItem: Pair<WeatherBasic, Int>) {
            if (pairItem.second == 0) {
                binding.cardViewHourly.setCardBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.transparent
                    )
                )
            } else {
                binding.cardViewHourly.setCardBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.cardBackgroundOpacity
                    )
                )
            }

            val weather = pairItem.first
            binding.tvHourlyTime.text = weather.dateTime?.unixTimestampToTimeString()
            val time = weather.dateTime?.unixTimestampToHourString()?.toInt()
            if (time != null) {
                weather.weatherMainCondition?.let { mainCondition ->
                    getIcon(mainCondition, time)?.let { image ->
                        binding.imgHourlyIcon.setImageResource(image)
                    }
                }
            }
            binding.tvHourlyTemperature.text = weather.temperature?.kelvinToCelsius().toString()
        }
    }
}
