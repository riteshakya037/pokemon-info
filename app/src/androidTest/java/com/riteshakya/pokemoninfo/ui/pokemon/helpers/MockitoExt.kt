package com.riteshakya.pokemoninfo.ui.pokemon.helpers

import org.mockito.Mockito

/**
 * a kotlin friendly mock that handles generics
 */
inline fun <reified T> mock(): T = Mockito.mock(T::class.java)
