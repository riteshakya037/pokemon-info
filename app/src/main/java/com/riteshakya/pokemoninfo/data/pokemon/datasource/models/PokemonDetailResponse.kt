package com.riteshakya.pokemoninfo.data.pokemon.datasource.models

import com.google.gson.annotations.SerializedName


data class PokemonDetailResponse(
    val name: String,
    val height: Int,
    @SerializedName("base_experience")
    val baseExperience: Int,
    val weight: Int
)

