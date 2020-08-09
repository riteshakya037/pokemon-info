package com.riteshakya.pokemoninfo.core.lifecycles

import io.reactivex.disposables.Disposable

interface DisposeOnLifecycleViewModel {

    val lifecycleDisposables: LifecycleDisposables

    fun onPause() = lifecycleDisposables.dispose(LifecycleDisposables.State.Pause)
    fun onStop() = lifecycleDisposables.dispose(LifecycleDisposables.State.Stop)
    fun onDestroyView() = lifecycleDisposables.dispose(LifecycleDisposables.State.DestroyView)
    fun onDestroy() = lifecycleDisposables.dispose(LifecycleDisposables.State.Destroy)
    fun onCleared() = lifecycleDisposables.dispose(LifecycleDisposables.State.Cleared)

    fun Disposable.untilPause() = also {
        lifecycleDisposables.add(LifecycleDisposables.State.Pause, it)
    }

    fun Disposable.untilStop() = also {
        lifecycleDisposables.add(LifecycleDisposables.State.Stop, it)
    }

    fun Disposable.untilDestroyView() = also {
        lifecycleDisposables.add(LifecycleDisposables.State.DestroyView, it)
    }

    fun Disposable.untilDestroy() = also {
        lifecycleDisposables.add(LifecycleDisposables.State.Destroy, it)
    }

    fun Disposable.untilCleared() = also {
        lifecycleDisposables.add(LifecycleDisposables.State.Cleared, it)
    }
}