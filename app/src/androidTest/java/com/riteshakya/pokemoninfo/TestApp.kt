package com.riteshakya.pokemoninfo

import android.app.Application

/**
 * We use a separate App for tests to prevent initializing dependency injection.
 *
 * See [com.riteshakya.pokemoninfo.PokemonTestRunner].
 */
class TestApp : Application()
