package com.riteshakya.pokemoninfo.ui.pokemon.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.verify
import com.riteshakya.pokemoninfo.UnitTest
import org.amshove.kluent.shouldBe
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.internal.verification.Times

class PokemonDataFactoryTest : UnitTest() {
    private lateinit var pokemonDataFactory: PokemonDataFactory

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var pokemonPagedDataSource: PokemonPagedDataSource

    @Before
    fun setUp() {
        pokemonDataFactory = PokemonDataFactory(pokemonPagedDataSource)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `create should return the data source provided by constructor`() {
        val dataSource = pokemonDataFactory.create()

        dataSource shouldBe pokemonPagedDataSource
    }

    @Test
    fun `getPagedDataSource should return the data source provided by constructor`() {
        val dataSource = pokemonDataFactory.getPagedDataSource()

        dataSource shouldBe pokemonPagedDataSource
    }

    @Test
    fun `retryPagination should call appropriate method`() {

        pokemonDataFactory.retryPagination()

        verify(pokemonPagedDataSource).retryPagination()

        verify(pokemonPagedDataSource, Times(1)).retryPagination()
    }

    @Test
    fun `refresh should call appropriate method`() {

        pokemonDataFactory.refresh()

        verify(pokemonPagedDataSource).refresh()

        verify(pokemonPagedDataSource, Times(1)).refresh()
    }
}