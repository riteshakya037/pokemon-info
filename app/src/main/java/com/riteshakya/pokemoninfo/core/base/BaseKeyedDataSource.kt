package com.riteshakya.pokemoninfo.core.base

import androidx.paging.PageKeyedDataSource
import com.riteshakya.pokemoninfo.core.lifecycles.DisposeOnLifecycleViewModel
import com.riteshakya.pokemoninfo.core.lifecycles.LifecycleDisposables

abstract class BaseKeyedDataSource<K, V>
    : PageKeyedDataSource<K, V>(), DisposeOnLifecycleViewModel {

    override val lifecycleDisposables = LifecycleDisposables()

    fun clear() {
        onCleared()
    }
}