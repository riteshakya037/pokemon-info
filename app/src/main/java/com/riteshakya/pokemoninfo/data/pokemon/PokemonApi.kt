package com.riteshakya.pokemoninfo.data.pokemon

import com.riteshakya.pokemoninfo.data.pokemon.datasource.models.PokemonResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Url

interface PokemonApi {
    @GET("pokemon")
    fun getInitialPokemons(): Single<PokemonResponse>

    @GET
    fun getSequentialPokemons(
        @Url url: String
    ): Single<PokemonResponse>
}