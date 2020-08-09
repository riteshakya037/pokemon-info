package com.riteshakya.pokemoninfo.di

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.riteshakya.pokemoninfo.PokemonApp
import com.riteshakya.pokemoninfo.data.pokemon.di.Pokemon
import dagger.Binds
import dagger.Module
import dagger.Provides
import com.riteshakya.pokemoninfo.core.di.AppViewModelFactory
import javax.inject.Provider
import javax.inject.Singleton

@Module(includes = [AppModule.Supporting::class, Pokemon.Repositories::class])
abstract class AppModule {
    @Binds
    abstract fun bindApplication(app: PokemonApp): Application

    @Module
    class Supporting {
        @Provides
        @Singleton
        fun provideContext(application: Application): Context = application

        @Provides
        fun provideViewModelFactory(
            providers: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
        ): ViewModelProvider.Factory = AppViewModelFactory(providers)
    }
}