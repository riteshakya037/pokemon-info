package com.riteshakya.pokemoninfo.ui.pokemon.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.verify
import com.riteshakya.pokemoninfo.AndroidTest
import com.riteshakya.pokemoninfo.helpers.createMockDataSourceFactory
import com.riteshakya.pokemoninfo.helpers.getOrAwaitValue
import com.riteshakya.pokemoninfo.repository.pokemon.models.Pokemon
import com.riteshakya.pokemoninfo.ui.pokemon.data.PokemonDataFactory
import com.riteshakya.pokemoninfo.ui.pokemon.data.PokemonPagedDataSource
import org.amshove.kluent.shouldBeEqualTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.internal.verification.Times

class PokemonViewModelTest : AndroidTest() {
    private lateinit var pokemonViewModel: PokemonViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var pokemonDataFactory: PokemonDataFactory

    @Mock
    private lateinit var pokemonPagedDataSource: PokemonPagedDataSource

    private val _pikachuItem = Pokemon("pikachu", "https://pokeapi.co/api/v2/pokemon/25/")
    private val _bulbasaurItem = Pokemon("bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/")

    private val _mockList = listOf(
        _pikachuItem,
        _bulbasaurItem
    )

    @Before
    fun setUp() {
        given {
            pokemonDataFactory.getPagedDataSource()
        }.willReturn(
            pokemonPagedDataSource
        )

        pokemonViewModel = PokemonViewModel(pokemonDataFactory)

        given {
            pokemonDataFactory.create()
        }.willReturn(
            createMockDataSourceFactory<String, Pokemon>(
                _mockList
            ).create()
        )
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `pokemonLiveData data should not me modified`() {
        val data = pokemonViewModel.pokemonLiveData.getOrAwaitValue()

        data shouldBeEqualTo _mockList
    }


    @Test
    fun `retry should call appropriate method`() {

        pokemonViewModel.retry()

        verify(pokemonDataFactory).retryPagination()

        verify(pokemonDataFactory, Times(1)).retryPagination()
    }

    @Test
    fun `refresh should call appropriate method`() {

        pokemonViewModel.refresh()

        verify(pokemonDataFactory).refresh()

        verify(pokemonDataFactory, Times(1)).refresh()
    }
}