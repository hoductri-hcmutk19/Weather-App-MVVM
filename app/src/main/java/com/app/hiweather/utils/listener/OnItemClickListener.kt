package com.app.hiweather.utils.listener

import android.view.View

interface OnItemClickListener {
    fun onItemClickListener(view: View, position: Int, action: String = "null")
}
