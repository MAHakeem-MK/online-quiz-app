package com.mahakeemmk.quizapp.model

import android.content.Context
import android.net.ConnectivityManager

class NetworkChecker(var context: Context) {
    fun isConnected():Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo?.isConnected ?: false
    }
}