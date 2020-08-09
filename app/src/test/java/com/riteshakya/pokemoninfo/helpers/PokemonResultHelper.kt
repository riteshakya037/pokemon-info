package com.riteshakya.pokemoninfo.helpers

import com.riteshakya.pokemoninfo.repository.pokemon.models.PokemonResult
import io.reactivex.Single

object PokemonResultHelper {
    private fun empty(): PokemonResult {
        return PokemonResult(0, "", "", emptyList())
    }

    fun emptySingle(): Single<PokemonResult> {
        return Single.just(empty())
    }
}