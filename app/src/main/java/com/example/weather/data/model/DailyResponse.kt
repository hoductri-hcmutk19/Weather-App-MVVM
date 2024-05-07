package com.example.weather.data.model

import com.example.weather.data.model.entity.Weather
import com.example.weather.data.model.entity.WeatherBasic
import com.example.weather.utils.ext.combineWithCountry
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DailyResponse(
    @SerializedName("city")
    @Expose
    val city: City?,
    @SerializedName("cnt")
    @Expose
    val cnt: Int?,
    @SerializedName("cod")
    @Expose
    val cod: String?,
    @SerializedName("list")
    @Expose
    val list: List<Daily>?,
    @SerializedName("message")
    @Expose
    val message: Double?
) {
    data class City(
        @SerializedName("coord")
        @Expose
        val coord: Coord?,
        @SerializedName("country")
        @Expose
        val country: String?,
        @SerializedName("id")
        @Expose
        val id: Int?,
        @SerializedName("name")
        @Expose
        val name: String?,
        @SerializedName("population")
        @Expose
        val population: Int?,
        @SerializedName("timezone")
        @Expose
        val timezone: Int?
    ) {
        data class Coord(
            @SerializedName("lat")
            @Expose
            val lat: Double?,
            @SerializedName("lon")
            @Expose
            val lon: Double?
        )
    }

    data class Daily(
        @SerializedName("clouds")
        @Expose
        val clouds: Int?,
        @SerializedName("deg")
        @Expose
        val deg: Int?,
        @SerializedName("dt")
        @Expose
        val dt: Int?,
        @SerializedName("feels_like")
        @Expose
        val feelsLike: FeelsLike?,
        @SerializedName("gust")
        @Expose
        val gust: Double?,
        @SerializedName("humidity")
        @Expose
        val humidity: Int?,
        @SerializedName("pop")
        @Expose
        val pop: Double?,
        @SerializedName("pressure")
        @Expose
        val pressure: Int?,
        @SerializedName("rain")
        @Expose
        val rain: Double?,
        @SerializedName("speed")
        @Expose
        val speed: Double?,
        @SerializedName("sunrise")
        @Expose
        val sunrise: Int?,
        @SerializedName("sunset")
        @Expose
        val sunset: Int?,
        @SerializedName("temp")
        @Expose
        val temp: Temp?,
        @SerializedName("weather")
        @Expose
        val weather: List<Weather?>?
    ) {
        data class FeelsLike(
            @SerializedName("day")
            @Expose
            val day: Double?,
            @SerializedName("eve")
            @Expose
            val eve: Double?,
            @SerializedName("morn")
            @Expose
            val morn: Double?,
            @SerializedName("night")
            @Expose
            val night: Double?
        )

        data class Temp(
            @SerializedName("day")
            @Expose
            val day: Double?,
            @SerializedName("eve")
            @Expose
            val eve: Double?,
            @SerializedName("max")
            @Expose
            val max: Double?,
            @SerializedName("min")
            @Expose
            val min: Double?,
            @SerializedName("morn")
            @Expose
            val morn: Double?,
            @SerializedName("night")
            @Expose
            val night: Double?
        )

        data class Weather(
            @SerializedName("description")
            @Expose
            val description: String?,
            @SerializedName("icon")
            @Expose
            val icon: String?,
            @SerializedName("id")
            @Expose
            val id: Int?,
            @SerializedName("main")
            @Expose
            val main: String?
        )
    }
}

fun DailyResponse.toWeather(): Weather {
    val latitude: Double? = city?.coord?.lat
    val longitude: Double? = city?.coord?.lon
    val timeZone: Int? = city?.timezone
    val cityName: String? = city?.name
    val country: String? = city?.country
    val isFavorite: String? = null
    val weatherCurrent: WeatherBasic? = null
    val weatherHourlyList: List<WeatherBasic>? = null
    val weatherDailyList: List<WeatherBasic>? = list?.map { it.toWeatherBasic() }
    val id = cityName.combineWithCountry(country)

    return Weather(
        id,
        latitude,
        longitude,
        timeZone,
        cityName,
        country,
        isFavorite,
        weatherCurrent,
        weatherHourlyList,
        weatherDailyList
    )
}

fun DailyResponse.Daily.toWeatherBasic(): WeatherBasic {
    return WeatherBasic(
        dt,
        temp?.day,
        weather?.firstOrNull()?.main,
        null,
        null,
        null
    )
}
