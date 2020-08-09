package com.riteshakya.pokemoninfo.data.pokemon.mappers

import com.riteshakya.pokemoninfo.data.pokemon.datasource.models.PokemonItem
import com.riteshakya.pokemoninfo.data.pokemon.datasource.models.PokemonResponse
import com.riteshakya.pokemoninfo.repository.pokemon.models.Pokemon
import com.riteshakya.pokemoninfo.repository.pokemon.models.PokemonResult

class PokemonMapper {
    fun mapToResult(pokemonResponse: PokemonResponse): PokemonResult {
        with(pokemonResponse) {
            return PokemonResult(
                count = count,
                next = next ?: "",
                previous = previous ?: "",
                data = results.map { mapToPokemon(it) }
            )
        }
    }

    private fun mapToPokemon(item: PokemonItem): Pokemon {
        with(item) {
            return Pokemon(
                name = name,
                url = url
            )
        }
    }
}