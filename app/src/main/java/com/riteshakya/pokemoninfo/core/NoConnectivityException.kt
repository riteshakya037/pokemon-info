package com.riteshakya.pokemoninfo.core

import android.content.Context
import com.riteshakya.pokemoninfo.R

class NoConnectivityException(private val context: Context) : RuntimeException() {
    override val message: String?
        get() = context.getString(R.string.network_error_message)
}