package com.riteshakya.pokemoninfo.core.base

import androidx.lifecycle.ViewModel
import com.riteshakya.pokemoninfo.core.lifecycles.DisposeOnLifecycleViewModel
import com.riteshakya.pokemoninfo.core.lifecycles.LifecycleDisposables

abstract class BaseViewModel : ViewModel(), DisposeOnLifecycleViewModel {

    override val lifecycleDisposables = LifecycleDisposables()


    override fun onCleared() {
        super<ViewModel>.onCleared()
        super<DisposeOnLifecycleViewModel>.onCleared()
    }
}