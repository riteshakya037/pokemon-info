package com.riteshakya.pokemoninfo.data.pokemon.datasource

import com.riteshakya.pokemoninfo.data.pokemon.PokemonService
import com.riteshakya.pokemoninfo.data.pokemon.mappers.PokemonMapper
import com.riteshakya.pokemoninfo.repository.pokemon.IPokemonDataSource
import com.riteshakya.pokemoninfo.repository.pokemon.models.PokemonDetail
import com.riteshakya.pokemoninfo.repository.pokemon.models.PokemonResult
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkPokemonDataSource
@Inject constructor(
    private val pokemonService: PokemonService
) : IPokemonDataSource {
    private val pokemonMapper by lazy { PokemonMapper() }

    override fun getPokemon(url: String): Single<PokemonResult> {
        return if (url.isEmpty()) {
            pokemonService.getInitialPokemons()
        } else {
            pokemonService.getSequentialPokemons(url)
        }
            .subscribeOn(Schedulers.io())
            .map {
                pokemonMapper.mapToResult(it)
            }
    }

    override fun getPokemonDetail(url: String): Single<PokemonDetail> {
        return pokemonService.getPokemonDetail(url)
            .subscribeOn(Schedulers.io())
            .map {
                pokemonMapper.mapToDetail(it)
            }
    }
}