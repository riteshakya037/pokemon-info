package com.riteshakya.pokemoninfo.data.pokemon.repositories

import com.riteshakya.pokemoninfo.repository.pokemon.IPokemonDataSource
import com.riteshakya.pokemoninfo.repository.pokemon.IPokemonRepository
import com.riteshakya.pokemoninfo.repository.pokemon.models.PokemonDetail
import com.riteshakya.pokemoninfo.repository.pokemon.models.PokemonResult
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonRepository
@Inject constructor(
    private var pokemonDataSource: IPokemonDataSource
) : IPokemonRepository {

    override fun getPokemon(url: String): Single<PokemonResult> {
        return pokemonDataSource.getPokemon(url)
    }

    override fun getPokemonDetail(url: String): Single<PokemonDetail> {
        return pokemonDataSource.getPokemonDetail(url)
    }
}
