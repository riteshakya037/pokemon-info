package com.riteshakya.pokemoninfo.interactor.pokemon

import com.riteshakya.pokemoninfo.repository.pokemon.IPokemonRepository
import com.riteshakya.pokemoninfo.repository.pokemon.models.PokemonDetail
import io.reactivex.Single
import javax.inject.Inject

class GetPokemonDetail @Inject constructor(
    private val repository: IPokemonRepository
) {
    operator fun invoke(url: String): Single<PokemonDetail> {
        return repository.getPokemonDetail(url)
    }
}