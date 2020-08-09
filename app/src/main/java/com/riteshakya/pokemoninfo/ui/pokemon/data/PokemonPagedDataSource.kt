package com.riteshakya.pokemoninfo.ui.pokemon.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.riteshakya.pokemoninfo.core.DataResult
import com.riteshakya.pokemoninfo.core.base.BaseKeyedDataSource
import com.riteshakya.pokemoninfo.interactor.pokemon.GetPokemons
import com.riteshakya.pokemoninfo.repository.pokemon.models.Pokemon
import com.riteshakya.pokemoninfo.repository.pokemon.models.PokemonResult
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber
import javax.inject.Inject


class PokemonPagedDataSource
@Inject constructor(
    private val getPokemons: GetPokemons
) : BaseKeyedDataSource<String, Pokemon>() {

    private val _paginatedNetworkStateLiveData: MutableLiveData<DataResult<Unit>> =
        MutableLiveData()
    private val _initialLoadStateLiveData: MutableLiveData<DataResult<Unit>> = MutableLiveData()

    val paginatedNetworkStateLiveData: LiveData<DataResult<Unit>> by lazy { _paginatedNetworkStateLiveData }
    val initialLoadStateLiveData: LiveData<DataResult<Unit>> by lazy { _initialLoadStateLiveData }

    // For Retry
    private var params: LoadParams<String>? = null
    private var callback: LoadCallback<String, Pokemon>? = null
    private var initialParams: LoadInitialParams<String>? = null
    private var initialCallback: LoadInitialCallback<String, Pokemon>? = null

    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, Pokemon>
    ) {
        _initialLoadStateLiveData.postValue(DataResult.loading())
        this.initialCallback = callback
        this.initialParams = params
        getPokemons.invoke()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onPokemonFetched(it, callback)
            }, { onError(it) })
            .untilCleared()
    }

    private fun onPokemonFetched(
        it: PokemonResult,
        callback: LoadInitialCallback<String, Pokemon>
    ) {
        callback.onResult(
            it.data,
            it.previous,
            it.next
        )
        _initialLoadStateLiveData.postValue(DataResult.success(Unit))
        completeNetworkState(it)
    }

    private fun onError(throwable: Throwable?) {
        _initialLoadStateLiveData.postValue(DataResult.error(throwable?.message))
        Timber.e(throwable)
    }

    override fun loadBefore(
        params: LoadParams<String>,
        callback: LoadCallback<String, Pokemon>
    ) {
    }

    override fun loadAfter(
        params: LoadParams<String>,
        callback: LoadCallback<String, Pokemon>
    ) {
        this.params = params
        this.callback = callback
        if (params.key.isEmpty()) {
            return
        }
        _paginatedNetworkStateLiveData.postValue(DataResult.loading())
        getPokemons.invoke(params.key)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it?.also {
                    onMorePokemonFetched(it, callback)
                }
            }, { onMoreError(it) })
            .untilCleared()
    }

    private fun onMorePokemonFetched(
        it: PokemonResult,
        callback: LoadCallback<String, Pokemon>
    ) {
        callback.onResult(
            it.data,
            it.next
        )
        completeNetworkState(it)
    }

    private fun completeNetworkState(it: PokemonResult) {
        if (it.next.isEmpty()) {
            _paginatedNetworkStateLiveData.postValue(DataResult.success(Unit))
        }
    }

    private fun onMoreError(throwable: Throwable?) {
        _paginatedNetworkStateLiveData.postValue(DataResult.error(throwable?.message))
        Timber.e(throwable)
    }

    fun retryPagination() {
        loadAfter(params!!, callback!!)
    }

    fun refresh() {
        loadInitial(initialParams!!, initialCallback!!)
    }
}