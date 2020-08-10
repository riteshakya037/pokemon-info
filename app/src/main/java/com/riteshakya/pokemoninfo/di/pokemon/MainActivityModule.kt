package com.riteshakya.pokemoninfo.di.pokemon

import com.riteshakya.pokemoninfo.core.di.PerActivity
import com.riteshakya.pokemoninfo.ui.pokemon.screens.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class MainActivityModule {
    @PerActivity
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun provideMainActivityFactory(): MainActivity
}