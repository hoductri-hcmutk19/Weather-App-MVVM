package com.app.hiweather.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.app.hiweather.R
import com.app.hiweather.data.repository.WeatherRepository
import com.app.hiweather.ui.MainActivity
import com.app.hiweather.utils.ext.kelvinToCelsius
import com.app.hiweather.utils.ext.mpsToKmph
import com.app.hiweather.utils.ext.unixTimestampToTimeString
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DailyWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters), KoinComponent {

    override fun doWork(): Result {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val repository: WeatherRepository by inject()

                if (ContextCompat.checkSelfPermission(
                        applicationContext,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    val fusedLocationClient =
                        LocationServices.getFusedLocationProviderClient(applicationContext)

                    val location = suspendCoroutine { continuation ->
                        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                            continuation.resume(location)
                        }.addOnFailureListener { _ ->
                            continuation.resume(null)
                        }
                    }

                    if (location != null) {
                        fetchWeather(repository, location)
                    }
                }
            } catch (e: IOException) {
                Log.e("DailyWorker", "Exception occurred: $e")
            }
        }
        return Result.success()
    }

    private suspend fun fetchWeather(
        repository: WeatherRepository,
        currentLocation: Location?
    ) {
        currentLocation?.let { currentLocation ->
            CoroutineScope(Dispatchers.Default).launch {
                val currentDeferred = async {
                    repository.fetchWeatherForecastCurrent(
                        currentLocation.latitude,
                        currentLocation.longitude
                    )
                }
                val data = currentDeferred.await().singleOrNull()

                val location = data?.getLocation()
                val weatherDescription = data?.weatherCurrent?.weatherDescription
                val temperature = data?.weatherCurrent?.temperature?.kelvinToCelsius().toString()
                val windSpeed = data?.weatherCurrent?.windSpeed?.mpsToKmph().toString()
                val dateTime = data?.weatherCurrent?.dateTime?.unixTimestampToTimeString()
                val notificationTitle = "$location          $dateTime"
                val notificationContent =
                    "$weatherDescription        ${
                    applicationContext.getString(R.string.temperature)
                    } $temperature℃        ${
                    applicationContext.getString(R.string.wind_speed)
                    } $windSpeed km/h"
                showNotification(notificationTitle, notificationContent)
            }
        }
    }

    private fun showNotification(title: String, content: String) {
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(
            applicationContext,
            CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_app)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = CHANNEL_NAME
            val channelDescription = CHANNEL_DESCRIPTION
            val channelImportance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(CHANNEL_ID, channelName, channelImportance).apply {
                description = channelDescription
            }

            val notificationManager = applicationContext.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            with(NotificationManagerCompat.from(applicationContext)) {
                notify(NOTIFICATION_ID, notification.build())
            }
        }
    }

    companion object {
        const val CHANNEL_ID = "channel_id"
        const val NOTIFICATION_ID = 1
        const val CHANNEL_NAME = "Weather Daily"
        const val CHANNEL_DESCRIPTION = "Notify weather in the morning"
    }
}
