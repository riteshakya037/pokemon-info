package com.riteshakya.pokemoninfo.di

import android.app.Application
import android.content.Context
import com.riteshakya.pokemoninfo.data.pokemon.di.Pokemon
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [AppModule.Supporting::class, ViewModelModule::class, Pokemon.Repositories::class, NetworkModule::class])
abstract class AppModule {
    @Module
    class Supporting {
        @Provides
        @Singleton
        fun provideContext(application: Application): Context = application
    }
}