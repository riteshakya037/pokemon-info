package com.riteshakya.pokemoninfo.di.pokemon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.riteshakya.pokemoninfo.core.di.PerActivity
import com.riteshakya.pokemoninfo.core.di.ViewModelKey
import com.riteshakya.pokemoninfo.ui.pokemon.data.PokemonDataFactory
import com.riteshakya.pokemoninfo.ui.pokemon.screens.MainActivity
import com.riteshakya.pokemoninfo.ui.pokemon.viewmodel.PokemonViewModel
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module(includes = [MainActivityModule.ProvideViewModel::class])
abstract class MainActivityModule {
    @PerActivity
    @ContributesAndroidInjector(modules = [InjectViewModel::class, FragmentModules::class])
    abstract fun provideMainActivityFactory(): MainActivity

    @Module(includes = [PokemonDetailModule::class])
    class FragmentModules

    @Module
    class ProvideViewModel {

        @Provides
        @IntoMap
        @ViewModelKey(PokemonViewModel::class)
        fun providePokemonViewModel(
            dataFactory: PokemonDataFactory
        ): ViewModel = PokemonViewModel(dataFactory)
    }

    @Module
    class InjectViewModel {

        @Provides
        fun provideAPokemonListViewModel(
            factory: ViewModelProvider.Factory,
            target: MainActivity
        ): PokemonViewModel =
            ViewModelProvider(target.viewModelStore, factory).get(PokemonViewModel::class.java)
    }
}