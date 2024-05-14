package com.example.weather.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.weather.R
import com.example.weather.data.repository.source.local.AppDatabase
import com.example.weather.ui.MainActivity
import com.example.weather.utils.Constant
import com.example.weather.utils.Utils.getIcon
import com.example.weather.utils.ext.kelvinToCelsius
import com.example.weather.utils.ext.unixTimestampToHourString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherWidgetProvider : AppWidgetProvider() {

    @Suppress("NestedBlockDepth")
    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        val context = context ?: return

        val intent = Intent(context, MainActivity::class.java)
        intent.action = Intent.ACTION_MAIN
        intent.addCategory(Intent.CATEGORY_LAUNCHER)

        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        CoroutineScope(Dispatchers.Main.immediate).launch {
            val db = AppDatabase.getInstance(context.applicationContext).weatherDao()
            val weatherList = db.getAllData()
            val currentWeather = weatherList.find { it.isFavorite == Constant.FALSE }

            appWidgetIds?.forEach { appWidgetId ->
                val views = RemoteViews(context.packageName, R.layout.widget_weather)

                currentWeather?.weatherCurrent?.let { weatherCurrent ->
                    val time = weatherCurrent.dateTime?.unixTimestampToHourString()?.toInt()
                    if (time != null) {
                        weatherCurrent.weatherMainCondition?.let { mainCondition ->
                            getIcon(mainCondition, time)?.let { image ->
                                views.setImageViewResource(R.id.img_widget, image)
                            }
                        }
                    }
                }
                views.setTextViewText(
                    R.id.tv_description_widget,
                    currentWeather?.weatherCurrent?.weatherDescription
                )
                views.setTextViewText(R.id.tv_location_widget, currentWeather?.getLocation())
                views.setTextViewText(
                    R.id.tv_temperature_widget,
                    currentWeather?.weatherCurrent?.temperature?.kelvinToCelsius().toString()
                )

                views.setOnClickPendingIntent(R.id.widget_weather, pendingIntent)

                appWidgetManager?.updateAppWidget(appWidgetId, views)
            }
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }
}
