package com.riteshakya.pokemoninfo.di.pokemon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.riteshakya.pokemoninfo.core.di.PerFragment
import com.riteshakya.pokemoninfo.core.di.ViewModelKey
import com.riteshakya.pokemoninfo.interactor.pokemon.GetPokemonDetail
import com.riteshakya.pokemoninfo.ui.pokemon.screens.PokemonDetailBottomSheet
import com.riteshakya.pokemoninfo.ui.pokemon.viewmodel.PokemonDetailViewModel
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap


@Module(includes = [PokemonDetailModule.ProvideViewModel::class])
abstract class PokemonDetailModule {

    @PerFragment
    @ContributesAndroidInjector(modules = [InjectViewModel::class])
    abstract fun providesPokemonDetailFragment(): PokemonDetailBottomSheet

    @Module
    class ProvideViewModel {

        @Provides
        @IntoMap
        @ViewModelKey(PokemonDetailViewModel::class)
        fun providePokemonDetailViewModel(
            getPokemonDetail: GetPokemonDetail
        ): ViewModel = PokemonDetailViewModel(getPokemonDetail)
    }

    @Module
    class InjectViewModel {

        @Provides
        fun providePokemonDetailViewModel(
            factory: ViewModelProvider.Factory,
            target: PokemonDetailBottomSheet
        ): PokemonDetailViewModel =
            ViewModelProvider(
                target.viewModelStore,
                factory
            ).get(PokemonDetailViewModel::class.java)
    }
}