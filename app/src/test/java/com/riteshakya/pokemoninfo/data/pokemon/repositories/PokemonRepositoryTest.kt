package com.riteshakya.pokemoninfo.data.pokemon.repositories

import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.verify
import com.riteshakya.pokemoninfo.UnitTest
import com.riteshakya.pokemoninfo.repository.pokemon.IPokemonDataSource
import com.riteshakya.pokemoninfo.repository.pokemon.models.Pokemon
import com.riteshakya.pokemoninfo.repository.pokemon.models.PokemonResult
import io.reactivex.Single
import org.amshove.kluent.shouldBe
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

class PokemonRepositoryTest : UnitTest() {

    private lateinit var pokemonRepository: PokemonRepository

    @Mock
    private lateinit var pokemonDataSource: IPokemonDataSource

    private val _pikachuItem = Pokemon("pikachu", "https://pokeapi.co/api/v2/pokemon/25/")
    private val _bulbasaurItem = Pokemon("bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/")
    private val _nextUrl = "https://pokeapi.co/api/v2/pokemon?offset=20&limit=20"

    private val _mockList = listOf(
        _pikachuItem,
        _bulbasaurItem
    )

    private val _initialResponse = PokemonResult(
        count = 20,
        next = _nextUrl,
        previous = "",
        data = _mockList
    )

    @Before
    fun setUp() {
        pokemonRepository = PokemonRepository(pokemonDataSource)

        given { pokemonDataSource.getPokemon(_nextUrl) }.willReturn(
            Single.just(_initialResponse)
        )
    }

    @Test
    fun `getPokemon args should not me modified`() {
        pokemonRepository.getPokemon(_nextUrl).subscribe()

        verify(pokemonDataSource).getPokemon(_nextUrl)
    }

    @Test
    fun `getPokemon data should not me modified`() {
        pokemonRepository.getPokemon(_nextUrl).subscribe({
            it shouldBe _initialResponse
        }, {})
    }

    @Test
    fun `getPokemon error should not me modified`() {
        val runtimeException = RuntimeException()
        val url = "random"
        given { pokemonDataSource.getPokemon(url) }.willReturn(
            Single.error(runtimeException)
        )
        pokemonRepository.getPokemon(url).subscribe({
        }, {
            it shouldBe runtimeException
        })
    }
}