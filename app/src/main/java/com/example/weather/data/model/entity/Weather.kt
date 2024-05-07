package com.example.weather.data.model.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weather.data.model.entity.WeatherEntry.CURRENTLY_OBJECT
import com.example.weather.data.model.entity.WeatherEntry.DAILY_OBJECT
import com.example.weather.data.model.entity.WeatherEntry.HOURLY_OBJECT
import com.example.weather.data.model.entity.WeatherEntry.TBL_WEATHER_NAME
import kotlinx.parcelize.Parcelize

@Entity(tableName = TBL_WEATHER_NAME)
@Parcelize
data class Weather(
    @PrimaryKey
    val id: String = "",
    val latitude: Double? = 0.0,
    val longitude: Double? = 0.0,
    val timeZone: Int? = 0,
    var city: String? = "",
    var country: String? = "",
    var isFavorite: String? = "",
    @ColumnInfo(name = CURRENTLY_OBJECT)
    val weatherCurrent: WeatherBasic?,
    @ColumnInfo(name = HOURLY_OBJECT)
    var weatherHourlyList: List<WeatherBasic>?,
    @ColumnInfo(name = DAILY_OBJECT)
    var weatherDailyList: List<WeatherBasic>?
) : Parcelable {

    fun getLocation(): String {
        return if (!city.isNullOrEmpty() && !country.isNullOrEmpty()) {
            "$city, $country"
        } else {
            "Unknown"
        }
    }
}

object WeatherEntry {
    // Local database entries
    const val TBL_WEATHER_NAME = "weather_forecasts"
    const val CURRENTLY_OBJECT = "currently"
    const val HOURLY_OBJECT = "hourly"
    const val DAILY_OBJECT = "daily"
}
