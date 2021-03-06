package com.riteshakya.pokemoninfo.ui.pokemon.data

import androidx.paging.DataSource
import com.riteshakya.pokemoninfo.repository.pokemon.models.Pokemon
import javax.inject.Inject


class PokemonDataFactory
@Inject constructor(
    private var pokemonDataSource: PokemonPagedDataSource
) : DataSource.Factory<String, Pokemon>() {

    override fun create(): DataSource<String, Pokemon> {
        return pokemonDataSource
    }

    // Easier for testing by exposing separately
    fun getPagedDataSource(): PokemonPagedDataSource {
        return pokemonDataSource
    }

    fun retryPagination() {
        pokemonDataSource.retryPagination()
    }

    fun refresh() {
        pokemonDataSource.refresh()
    }
}