package com.riteshakya.pokemoninfo.repository.pokemon

import com.riteshakya.pokemoninfo.repository.pokemon.models.PokemonDetail
import com.riteshakya.pokemoninfo.repository.pokemon.models.PokemonResult
import io.reactivex.Single

interface IPokemonRepository {
    fun getPokemon(url: String): Single<PokemonResult>

    fun getPokemonDetail(url: String): Single<PokemonDetail>
}