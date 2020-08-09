package com.riteshakya.pokemoninfo.di

import com.riteshakya.pokemoninfo.di.pokemon.MainActivityModule
import dagger.Module

@Module(includes = [MainActivityModule::class])
abstract class ActivityBuilderModule