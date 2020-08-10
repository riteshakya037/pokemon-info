package com.riteshakya.pokemoninfo.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.riteshakya.pokemoninfo.core.di.ViewModelKey
import com.riteshakya.pokemoninfo.di.pokemon.PokemonViewModelFactory
import com.riteshakya.pokemoninfo.ui.pokemon.viewmodel.PokemonDetailViewModel
import com.riteshakya.pokemoninfo.ui.pokemon.viewmodel.PokemonViewModel

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(PokemonViewModel::class)
    abstract fun bindPokemonViewModel(pokemonViewModel: PokemonViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PokemonDetailViewModel::class)
    abstract fun bindPokemonDetailViewModel(pokemonDetailViewModel: PokemonDetailViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: PokemonViewModelFactory): ViewModelProvider.Factory
}
