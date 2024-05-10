package com.example.weather.utils.network

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import android.content.BroadcastReceiver


/**
 * NetworkStateReceiver defines a BroadcastReceiver which allows us to register for system (i.e. network status) or application events.
 * All registered receivers for an event are notified by the Android runtime once this event happens.
 * Source: http://stackoverflow.com/questions/6169059/android-event-for-internet-connectivity-state-change
 */
class NetworkStateReceiver : BroadcastReceiver() {
    protected var listeners: MutableList<NetworkStateReceiverListener>
    protected var connected: Boolean? = null
    private val TAG = "NetworkStateReceiver"

    init {
        listeners = ArrayList()
    }

    /**
     * Called when the BroadcastReceiver is receiving an Intent broadcast (event for which the broadcast receiver has registered occurs).
     * During this time you can use the other methods on BroadcastReceiver to view/modify the current result values.
     * NOTE: When it runs on the main thread you should never perform long-running operations in it (there is a timeout of 10 seconds that the system allows before considering the receiver to be blocked and a candidate to be killed).
     * NOTE: You cannot launch a popup dialog in your implementation of onReceive().
     * @param context   Object to access additional information or to start services or activities
     * @param intent    Object with action used to register your receiver. This object contains additional information (e.g. extras)
     */
    override fun onReceive(context: Context, intent: Intent?) {
        Log.i(TAG, "Intent broadcast received")
        if (intent == null || intent.extras == null) return

        // Retrieve a ConnectivityManager for handling management of network connections
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        // Details about the currently active default data network. When connected, this network is the default route for outgoing connections
        val networkInfo = manager.activeNetworkInfo
        /**
         * NOTE: getActiveNetworkInfo() may return null when there is no default network e.g. Airplane Mode
         */
        if (networkInfo != null && networkInfo.state == NetworkInfo.State.CONNECTED) {
            connected = true
        } else if (intent.getBooleanExtra(
                ConnectivityManager.EXTRA_NO_CONNECTIVITY,
                java.lang.Boolean.FALSE
            )
        ) {    //Boolean that indicates whether there is a complete lack of connectivity
            connected = false
        }
        notifyStateToAll()
    } //After the onReceive() of the receiver class has finished, the Android system is allowed to recycle the receiver

    /**
     * Notify the state to all needed methods
     */
    private fun notifyStateToAll() {
        Log.i(TAG, "Notifying state to " + listeners.size + " listener(s)")
        for (eachNetworkStateReceiverListener in listeners) notifyState(
            eachNetworkStateReceiverListener
        )
    }

    /**
     * Notify the network state, triggering interface functions based on the current state
     * @param networkStateReceiverListener  Object which implements the NetworkStateReceiverListener interface
     */
    private fun notifyState(networkStateReceiverListener: NetworkStateReceiverListener?) {
        if (connected == null || networkStateReceiverListener == null) return
        if (connected == true) {
            // Triggering function on the interface towards network availability
            networkStateReceiverListener.networkAvailable()
        } else {
            // Triggering function on the interface towards network being unavailable
            networkStateReceiverListener.networkUnavailable()
        }
    }

    /**
     * Adds a listener to the list so that it will receive connection state change updates
     * @param networkStateReceiverListener     Object which implements the NetworkStateReceiverListener interface
     */
    fun addListener(networkStateReceiverListener: NetworkStateReceiverListener) {
        Log.i(
            TAG,
            "addListener() - listeners.add(networkStateReceiverListener) + notifyState(networkStateReceiverListener);"
        )
        listeners.add(networkStateReceiverListener)
        notifyState(networkStateReceiverListener)
    }

    /**
     * Removes listener (when no longer necessary) from the list so that it will no longer receive connection state change updates
     * @param networkStateReceiverListener     Object which implements the NetworkStateReceiverListener interface
     */
    fun removeListener(networkStateReceiverListener: NetworkStateReceiverListener) {
        listeners.remove(networkStateReceiverListener)
    }

    /**
     * Inner Interface (i.e. to encapsulate behavior in a generic and re-usable way) which handles connection state changes for classes which registered this receiver (Outer class NetworkStateReceiver)
     * This interface implements the 'Strategy Pattern', where an execution strategy is evaluated and applied internally at runtime
     */
    interface NetworkStateReceiverListener {
        /**
         * When the connection state is changed and there is a connection, this method is called
         */
        fun networkAvailable()

        /**
         * Connection state is changed and there is not a connection, this method is called
         */
        fun networkUnavailable()
    }
}
