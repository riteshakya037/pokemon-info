package com.riteshakya.pokemoninfo.interactor.pokemon

import com.riteshakya.pokemoninfo.repository.pokemon.IPokemonRepository
import com.riteshakya.pokemoninfo.repository.pokemon.models.PokemonResult
import io.reactivex.Single
import javax.inject.Inject

class GetPokemons @Inject constructor(
    private val repository: IPokemonRepository
) {
    operator fun invoke(url: String = ""): Single<PokemonResult> {
        return repository.getPokemon(url)
    }
}