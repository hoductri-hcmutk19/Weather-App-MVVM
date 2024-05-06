package com.example.weather.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CurrentResponse(
    @SerializedName("base")
    @Expose
    val base: String?,
    @SerializedName("clouds")
    @Expose
    val clouds: Clouds?,
    @SerializedName("cod")
    @Expose
    val cod: Int?,
    @SerializedName("coord")
    @Expose
    val coord: Coord?,
    @SerializedName("dt")
    @Expose
    val dt: Int?,
    @SerializedName("id")
    @Expose
    val id: Int?,
    @SerializedName("main")
    @Expose
    val main: Main?,
    @SerializedName("name")
    @Expose
    val name: String?,
    @SerializedName("rain")
    @Expose
    val rain: Rain?,
    @SerializedName("sys")
    @Expose
    val sys: Sys?,
    @SerializedName("timezone")
    @Expose
    val timezone: Int?,
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

    data class Coord(
        @SerializedName("lat")
        @Expose
        val lat: Double?,
        @SerializedName("lon")
        @Expose
        val lon: Double?
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
        @SerializedName("temp_max")
        @Expose
        val tempMax: Double?,
        @SerializedName("temp_min")
        @Expose
        val tempMin: Double?
    )

    data class Rain(
        @SerializedName("1h")
        @Expose
        val h: Double?
    )

    data class Sys(
        @SerializedName("country")
        @Expose
        val country: String?,
        @SerializedName("id")
        @Expose
        val id: Int?,
        @SerializedName("sunrise")
        @Expose
        val sunrise: Int?,
        @SerializedName("sunset")
        @Expose
        val sunset: Int?,
        @SerializedName("type")
        @Expose
        val type: Int?
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
