package com.riteshakya.pokemoninfo.navigator

import androidx.fragment.app.Fragment
import com.riteshakya.pokemoninfo.testing.OpenForTesting
import com.riteshakya.pokemoninfo.ui.pokemon.screens.PokemonDetailBottomSheet
import javax.inject.Inject

@OpenForTesting
class Navigator @Inject constructor() {
    fun showPokemonDetail(pokemonListFragment: Fragment, name: String, url: String) {
        PokemonDetailBottomSheet.create(name, url)
            .show(pokemonListFragment.parentFragmentManager)
    }
}
