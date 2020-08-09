package com.riteshakya.pokemoninfo.core

import java.io.Serializable

data class DataResult<T>(
    val status: Status,
    var data: T?,
    val message: String?
) : Serializable {

    companion object {

        fun <T> success(data: T?): DataResult<T> {
            return DataResult(Status.SUCCESS, data, null)
        }

        fun <T> error(message: String?, data: T? = null): DataResult<T> {
            return DataResult(Status.ERROR, data, message)
        }

        fun <T> loading(data: T? = null): DataResult<T> {
            return DataResult(Status.LOADING, data, null)
        }
    }

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING,
    }
}