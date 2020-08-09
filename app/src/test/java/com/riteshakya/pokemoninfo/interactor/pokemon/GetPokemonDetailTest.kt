package com.riteshakya.pokemoninfo.interactor.pokemon

import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.verify
import com.riteshakya.pokemoninfo.UnitTest
import com.riteshakya.pokemoninfo.repository.pokemon.IPokemonRepository
import com.riteshakya.pokemoninfo.repository.pokemon.models.PokemonDetail
import io.reactivex.Single
import org.amshove.kluent.shouldBe
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

class GetPokemonDetailTest : UnitTest() {
    private lateinit var getPokemonDetail: GetPokemonDetail

    @Mock
    lateinit var pokemonRepository: IPokemonRepository
    private val _pikachuURl = "https://pokeapi.co/api/v2/pokemon/25"
    private val _pikachuDetail = PokemonDetail("pikachu", 0.4f, 112, 6f)

    @Before
    fun setUp() {
        getPokemonDetail = GetPokemonDetail(pokemonRepository)

        given { pokemonRepository.getPokemonDetail(_pikachuURl) }.willReturn(
            Single.just(_pikachuDetail)
        )
    }

    @Test
    fun `getPokemonDetail arguments should not me modified`() {
        getPokemonDetail(_pikachuURl).subscribe()

        verify(pokemonRepository).getPokemonDetail(_pikachuURl)
    }

    @Test
    fun `getPokemonDetail data should not me modified`() {
        getPokemonDetail(_pikachuURl).subscribe({
            it shouldBe _pikachuDetail
        }, {})
    }

    @Test
    fun `getPokemonDetail error should not me modified`() {
        val runtimeException = RuntimeException()
        val url = "random"
        given { pokemonRepository.getPokemonDetail(url) }.willReturn(
            Single.error(runtimeException)
        )
        getPokemonDetail(url).subscribe({
        }, {
            it shouldBe runtimeException
        })
    }
}