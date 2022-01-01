package com.example.dictionary.utils.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

/**
 * Проверяем есть ли интернет
 */
fun isOnline(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val netInfo: NetworkInfo?
    netInfo = connectivityManager.activeNetworkInfo
    return netInfo != null && netInfo.isConnected
}