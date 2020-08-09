package com.riteshakya.pokemoninfo.repository.pokemon.models

data class PokemonResult(
    val count: Int,
    val next: String,
    val previous: String,
    val data: List<Pokemon>
)