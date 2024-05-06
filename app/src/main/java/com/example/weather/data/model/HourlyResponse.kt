package com.example.weather.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class HourlyResponse(
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
    val list: List<Hourly>?,
    @SerializedName("message")
    @Expose
    val message: Int?
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
        @SerializedName("sunrise")
        @Expose
        val sunrise: Int?,
        @SerializedName("sunset")
        @Expose
        val sunset: Int?,
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

    data class Hourly(
        @SerializedName("clouds")
        @Expose
        val clouds: Clouds?,
        @SerializedName("dt")
        @Expose
        val dt: Int?,
        @SerializedName("dt_txt")
        @Expose
        val dtTxt: String?,
        @SerializedName("main")
        @Expose
        val main: Main?,
        @SerializedName("pop")
        @Expose
        val pop: Double?,
        @SerializedName("rain")
        @Expose
        val rain: Rain?,
        @SerializedName("sys")
        @Expose
        val sys: Sys?,
        @SerializedName("visibility")
        @Expose
        val visibility: Int?,
        @SerializedName("weather")
        @Expose
        val weather: List<Weather?>?,
        @SerializedName("wind")
        @Expose
        val wind: Wind?
    ) {
        data class Clouds(
            @SerializedName("all")
            @Expose
            val all: Int?
        )

        data class Main(
            @SerializedName("feels_like")
            @Expose
            val feelsLike: Double?,
            @SerializedName("grnd_level")
            @Expose
            val grndLevel: Int?,
            @SerializedName("humidity")
            @Expose
            val humidity: Int?,
            @SerializedName("pressure")
            @Expose
            val pressure: Int?,
            @SerializedName("sea_level")
            @Expose
            val seaLevel: Int?,
            @SerializedName("temp")
            @Expose
            val temp: Double?,
            @SerializedName("temp_kf")
            @Expose
            val tempKf: Double?,
            @SerializedName("temp_max")
            @Expose
            val tempMax: Double?,
            @SerializedName("temp_min")
            @Expose
            val tempMin: Double?
        )

        data class Rain(
            @SerializedName("3h")
            @Expose
            val h: Double?
        )

        data class Sys(
            @SerializedName("pod")
            @Expose
            val pod: String?
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

        data class Wind(
            @SerializedName("deg")
            @Expose
            val deg: Int?,
            @SerializedName("gust")
            @Expose
            val gust: Double?,
            @SerializedName("speed")
            @Expose
            val speed: Double?
        )
    }
}
