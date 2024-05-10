package com.example.weather.utils.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log

class NetworkStateReceiver : BroadcastReceiver() {
    var listeners: MutableList<NetworkStateReceiverListener>
    private var connected: Boolean? = null
    private val tag = "NetworkStateReceiver"

    init {
        listeners = ArrayList()
    }

    override fun onReceive(context: Context, intent: Intent?) {
        Log.i(tag, "Intent broadcast received")
        if (intent == null || intent.extras == null) return

        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.activeNetworkInfo

        if (networkInfo != null && networkInfo.state == NetworkInfo.State.CONNECTED) {
            connected = true
        } else if (intent.getBooleanExtra(
                ConnectivityManager.EXTRA_NO_CONNECTIVITY,
                java.lang.Boolean.FALSE
            )
        ) {
            connected = false
        }
        notifyStateToAll()
    }

    private fun notifyStateToAll() {
        Log.i(tag, "Notifying state to " + listeners.size + " listener(s)")
        for (eachNetworkStateReceiverListener in listeners) notifyState(
            eachNetworkStateReceiverListener
        )
    }

    private fun notifyState(networkStateReceiverListener: NetworkStateReceiverListener?) {
        if (connected == null || networkStateReceiverListener == null) return
        if (connected == true) {
            networkStateReceiverListener.networkAvailable()
        } else {
            networkStateReceiverListener.networkUnavailable()
        }
    }

    fun addListener(networkStateReceiverListener: NetworkStateReceiverListener) {
        Log.i(
            tag,
            "addListener() - listeners.add(networkStateReceiverListener) + notifyState(networkStateReceiverListener);"
        )
        listeners.add(networkStateReceiverListener)
        notifyState(networkStateReceiverListener)
    }

    fun removeListener(networkStateReceiverListener: NetworkStateReceiverListener) {
        listeners.remove(networkStateReceiverListener)
    }

    interface NetworkStateReceiverListener {

        fun networkAvailable()

        fun networkUnavailable()
    }
}
