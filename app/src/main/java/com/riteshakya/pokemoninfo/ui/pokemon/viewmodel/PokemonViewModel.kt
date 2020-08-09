package com.riteshakya.pokemoninfo.ui.pokemon.viewmodel

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.riteshakya.pokemoninfo.constants.Constants.INITIAL_POKEMON_LOAD
import com.riteshakya.pokemoninfo.core.base.BaseViewModel
import com.riteshakya.pokemoninfo.repository.pokemon.models.Pokemon
import com.riteshakya.pokemoninfo.ui.pokemon.data.PokemonDataFactory
import javax.inject.Inject


class PokemonViewModel
@Inject constructor(
    pokemonDataFactory: PokemonDataFactory
) : BaseViewModel() {
    var pokemonLiveData: LiveData<PagedList<Pokemon>> =
        LivePagedListBuilder(pokemonDataFactory, INITIAL_POKEMON_LOAD).build()

}