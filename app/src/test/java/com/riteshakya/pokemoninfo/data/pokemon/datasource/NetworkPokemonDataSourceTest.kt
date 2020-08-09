package com.riteshakya.pokemoninfo.data.pokemon.datasource

import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.verify
import com.riteshakya.pokemoninfo.UnitTest
import com.riteshakya.pokemoninfo.data.pokemon.PokemonService
import com.riteshakya.pokemoninfo.data.pokemon.datasource.models.PokemonItem
import com.riteshakya.pokemoninfo.data.pokemon.datasource.models.PokemonResponse
import com.riteshakya.pokemoninfo.data.pokemon.mappers.PokemonMapper
import io.reactivex.Single
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

class NetworkPokemonDataSourceTest : UnitTest() {
    private lateinit var networkPokemonDataSource: NetworkPokemonDataSource

    @Mock
    private lateinit var pokemonService: PokemonService

    private val pokemonMapper by lazy { PokemonMapper() }

    private val _nextUrl = "https://pokeapi.co/api/v2/pokemon?offset=20&limit=20"

    private val _pikachuItem = PokemonItem("pikachu", "https://pokeapi.co/api/v2/pokemon/25/")
    private val _bulbasaurItem = PokemonItem("bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/")

    private val _mockList = listOf(
        _pikachuItem,
        _bulbasaurItem
    )

    private val _initialResponse = PokemonResponse(
        count = 20,
        next = _nextUrl,
        previous = "",
        results = _mockList
    )

    @Before
    fun setup() {
        networkPokemonDataSource = NetworkPokemonDataSource(pokemonService)

        given { pokemonService.getInitialPokemons() }.willReturn(
            Single.just(_initialResponse)
        )

        given { pokemonService.getSequentialPokemons(_nextUrl) }.willReturn(
            Single.just(_initialResponse)
        )
    }

    @Test
    fun `getPokemon args should not me modified`() {
        networkPokemonDataSource.getPokemon(_nextUrl).subscribe()

        verify(pokemonService).getSequentialPokemons(_nextUrl)
    }


    @Test
    fun `getPokemon empty args should call appropriate method`() {
        networkPokemonDataSource.getPokemon("").subscribe()

        verify(pokemonService).getInitialPokemons()
    }

    @Test
    fun `getPokemon data should not me modified`() {
        networkPokemonDataSource.getPokemon(_nextUrl).subscribe({
            it shouldBeEqualTo pokemonMapper.mapToResult(_initialResponse)
        }, {})
    }

    @Test
    fun `getPokemon errors should not me modified`() {
        val runtimeException = RuntimeException()
        val url = "random"
        given { pokemonService.getSequentialPokemons(url) }.willReturn(
            Single.error(runtimeException)
        )
        networkPokemonDataSource.getPokemon(url).subscribe({
        }, {
            it shouldBe runtimeException
        })
    }
}