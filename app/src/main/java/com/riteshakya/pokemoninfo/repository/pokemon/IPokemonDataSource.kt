package com.riteshakya.pokemoninfo.repository.pokemon

import com.riteshakya.pokemoninfo.repository.pokemon.models.PokemonResult
import io.reactivex.Single

interface IPokemonDataSource {
    fun getPokemon(url: String): Single<PokemonResult>
}