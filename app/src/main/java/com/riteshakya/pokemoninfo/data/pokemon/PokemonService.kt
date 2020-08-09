package com.riteshakya.pokemoninfo.data.pokemon

import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonService
@Inject constructor(
    retrofit: Retrofit
) {
    private val pokemonService by lazy { retrofit.create(PokemonApi::class.java) }

    fun getInitialPokemons() = pokemonService.getInitialPokemons()

    fun getSequentialPokemons(url: String) = pokemonService.getSequentialPokemons(url)
}
