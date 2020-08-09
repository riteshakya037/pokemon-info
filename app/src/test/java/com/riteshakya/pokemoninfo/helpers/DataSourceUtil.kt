package com.riteshakya.pokemoninfo.helpers

import androidx.paging.PageKeyedDataSource

abstract class LoadInitialCallback<K, V> : PageKeyedDataSource.LoadInitialCallback<K, V>() {
    override fun onResult(data: MutableList<V>, previousPageKey: K?, nextPageKey: K?) {
    }

    override fun onResult(
        data: MutableList<V>,
        position: Int,
        totalCount: Int,
        previousPageKey: K?,
        nextPageKey: K?
    ) {

    }
}

abstract class LoadCallback<K, V> : PageKeyedDataSource.LoadCallback<K, V>() {
    override fun onResult(data: MutableList<V>, adjacentPageKey: K?) {

    }
}