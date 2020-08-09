package com.riteshakya.pokemoninfo.interactor.pokemon

import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.verify
import com.riteshakya.pokemoninfo.UnitTest
import com.riteshakya.pokemoninfo.repository.pokemon.IPokemonRepository
import com.riteshakya.pokemoninfo.repository.pokemon.models.Pokemon
import com.riteshakya.pokemoninfo.repository.pokemon.models.PokemonResult
import io.reactivex.Single
import org.amshove.kluent.shouldBe
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

class GetPokemonsTest : UnitTest() {
    private lateinit var getPokemons: GetPokemons

    @Mock
    lateinit var pokemonRepository: IPokemonRepository

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
        getPokemons = GetPokemons(pokemonRepository)

        given { pokemonRepository.getPokemon("") }.willReturn(
            Single.just(_initialResponse)
        )

        given { pokemonRepository.getPokemon(_nextUrl) }.willReturn(
            Single.just(_initialResponse)
        )
    }

    private fun mountTest(url: String = "") {
        getPokemons(url).subscribe()
    }

    @Test
    fun `getPokemon empty args should not me modified`() {
        mountTest()

        verify(pokemonRepository).getPokemon("")
    }

    @Test
    fun `getPokemon args should not me modified`() {
        mountTest(_nextUrl)

        verify(pokemonRepository).getPokemon(_nextUrl)
    }

    @Test
    fun `getPokemon data should not me modified`() {
        getPokemons(_nextUrl).subscribe({
            it shouldBe _initialResponse
        }, {})
    }

    @Test
    fun `getPokemon error should not me modified`() {
        val runtimeException = RuntimeException()
        val url = "random"
        given { pokemonRepository.getPokemon(url) }.willReturn(
            Single.error(runtimeException)
        )
        getPokemons(url).subscribe({
        }, {
            it shouldBe runtimeException
        })
    }
}