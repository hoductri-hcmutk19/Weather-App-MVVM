package com.app.hiweather.utils.listener

import android.location.Location

interface OnFetchListener {
    fun onDataLocation(location: Location?)
}
