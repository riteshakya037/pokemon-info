package com.riteshakya.pokemoninfo.data.pokemon.di

import com.riteshakya.pokemoninfo.data.pokemon.datasource.NetworkPokemonDataSource
import com.riteshakya.pokemoninfo.data.pokemon.repositories.PokemonRepository
import com.riteshakya.pokemoninfo.repository.pokemon.IPokemonDataSource
import com.riteshakya.pokemoninfo.repository.pokemon.IPokemonRepository
import dagger.Module
import dagger.Provides

class Pokemon {

    @Module
    class Repositories {
        @Provides
        fun providePokemonRepository(repository: PokemonRepository): IPokemonRepository = repository

        @Provides
        fun providePokemonDataSource(dataSource: NetworkPokemonDataSource): IPokemonDataSource = dataSource
    }
}

