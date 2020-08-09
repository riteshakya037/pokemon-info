package com.riteshakya.pokemoninfo.core

import android.content.Context
import android.net.ConnectivityManager
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

val Context.isConnected: Boolean
    get() =
        connectivityManager.activeNetworkInfo.let {
            it != null && it.isConnected
        }

val Context.connectivityManager: ConnectivityManager
    get() = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


fun Fragment.getFormattedString(@StringRes resId: Int, vararg args: Any?): String = String.format(
    context!!.getString(resId), *args
)