package com.riteshakya.pokemoninfo.ui.pokemon.data

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

    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, Pokemon>
    ) {
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
    }

    private fun onError(throwable: Throwable?) {
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
        if (params.key.isEmpty()) {
            return
        }
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
    }

    private fun onMoreError(throwable: Throwable?) {
        Timber.e(throwable)
    }
}