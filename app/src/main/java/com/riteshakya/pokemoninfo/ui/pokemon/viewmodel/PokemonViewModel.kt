package com.riteshakya.pokemoninfo.ui.pokemon.viewmodel

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.riteshakya.pokemoninfo.constants.Constants.INITIAL_POKEMON_LOAD
import com.riteshakya.pokemoninfo.core.DataResult
import com.riteshakya.pokemoninfo.core.base.BaseViewModel
import com.riteshakya.pokemoninfo.repository.pokemon.models.Pokemon
import com.riteshakya.pokemoninfo.ui.pokemon.data.PokemonDataFactory
import javax.inject.Inject


class PokemonViewModel
@Inject constructor(
    private val pokemonDataFactory: PokemonDataFactory
) : BaseViewModel() {

    var loadingState: LiveData<DataResult<Unit>> =
        pokemonDataFactory.create().paginatedNetworkStateLiveData

    var initialState: LiveData<DataResult<Unit>> =
        pokemonDataFactory.create().initialLoadStateLiveData

    var pokemonLiveData: LiveData<PagedList<Pokemon>> =
        LivePagedListBuilder(pokemonDataFactory, INITIAL_POKEMON_LOAD).build()

    fun retry() {
        pokemonDataFactory.retryPagination()
    }

    fun refresh() {
        pokemonDataFactory.refresh()
    }
}