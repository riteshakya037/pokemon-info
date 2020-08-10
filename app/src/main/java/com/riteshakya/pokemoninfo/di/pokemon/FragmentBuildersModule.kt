package com.riteshakya.pokemoninfo.di.pokemon

import com.riteshakya.pokemoninfo.core.di.PerFragment
import com.riteshakya.pokemoninfo.ui.pokemon.screens.PokemonDetailBottomSheet
import com.riteshakya.pokemoninfo.ui.pokemon.screens.PokemonListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Suppress("unused")
@Module
abstract class FragmentBuildersModule {

    @PerFragment
    @ContributesAndroidInjector
    abstract fun providesPokemonDetailFragment(): PokemonDetailBottomSheet

    @PerFragment
    @ContributesAndroidInjector
    abstract fun providesPokemonListFragment(): PokemonListFragment

}