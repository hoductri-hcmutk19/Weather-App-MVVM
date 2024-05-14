@file:Suppress("TooManyFunctions")

package com.example.weather.utils.ext

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.weather.utils.Constant
import com.example.weather.widget.WeatherWidgetProvider
import java.text.SimpleDateFormat
import java.time.ZoneOffset
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

fun String?.combineWithCountry(country: String?): String {
    return "$this$country"
}

fun Int.unixTimestampToDateTimeString(): String {
    try {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this * 1000.toLong()

        val outputDateFormat = SimpleDateFormat("dd MMM - hh:mm a", Locale.ENGLISH)
        outputDateFormat.timeZone = TimeZone.getDefault() // user's default time zone
        return outputDateFormat.format(calendar.time)
    } catch (e: IllegalArgumentException) {
        println(e)
    }

    return this.toString()
}

fun Int.unixTimestampToDateYearString(): String {
    try {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this * 1000.toLong()

        val outputDateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH)
        outputDateFormat.timeZone = TimeZone.getDefault()
        return outputDateFormat.format(calendar.time)
    } catch (e: IllegalArgumentException) {
        println(e)
    }

    return this.toString()
}

fun Int.unixTimestampToDateMonthString(): String {
    try {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this * 1000.toLong()

        val outputDateFormat = SimpleDateFormat("MMM, dd", Locale.ENGLISH)
        outputDateFormat.timeZone = TimeZone.getDefault()
        return outputDateFormat.format(calendar.time)
    } catch (e: IllegalArgumentException) {
        println(e)
    }

    return this.toString()
}

fun Int.unixTimestampToDateString(): String {
    try {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this * 1000.toLong()

        val outputDateFormat = SimpleDateFormat("dd", Locale.ENGLISH)
        outputDateFormat.timeZone = TimeZone.getDefault()
        return outputDateFormat.format(calendar.time)
    } catch (e: IllegalArgumentException) {
        println(e)
    }

    return this.toString()
}

fun Int.unixTimestampToHourString(): String {
    try {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this * 1000.toLong()

        val outputDateFormat = SimpleDateFormat("HH", Locale.ENGLISH)
        outputDateFormat.timeZone = TimeZone.getDefault()
        return outputDateFormat.format(calendar.time)
    } catch (e: IllegalArgumentException) {
        println(e)
    }

    return this.toString()
}

fun Int.unixTimestampToTimeString(): String {
    try {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this * 1000.toLong()

        val outputDateFormat = SimpleDateFormat("HH:mm", Locale.ENGLISH)
        outputDateFormat.timeZone = TimeZone.getDefault()
        return outputDateFormat.format(calendar.time)
    } catch (e: IllegalArgumentException) {
        println(e)
    }

    return this.toString()
}

@RequiresApi(Build.VERSION_CODES.O)
fun Int.offsetToUTC(): String {
    val zoneOffset = ZoneOffset.ofTotalSeconds(this)
    return "UTC$zoneOffset"
}

/**
 * The temperature T in degrees Celsius (°C) is equal to the temperature T in Kelvin (K) minus 273.15:
 * T(°C) = T(K) - 273.15
 *
 * Example
 * Convert 300 Kelvin to degrees Celsius:
 * T(°C) = 300K - 273.15 = 26.85 °C
 */
fun Double.kelvinToCelsius(): Int {
    return (this - Constant.KELVIN_TO_CELSIUS_NUMBER).toInt()
}

fun Double.mpsToKmph(): Int {
    return (this * Constant.MPS_TO_KMPH_NUMBER).toInt()
}

fun Context.updateWidget() {
    val widgetUpdateIntent = Intent(this, WeatherWidgetProvider::class.java).apply {
        action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        putExtra(
            AppWidgetManager.EXTRA_APPWIDGET_IDS,
            AppWidgetManager.getInstance(this@updateWidget).getAppWidgetIds(
                ComponentName(
                    this@updateWidget,
                    WeatherWidgetProvider::class.java
                )
            )
        )
    }
    sendBroadcast(widgetUpdateIntent)
}
