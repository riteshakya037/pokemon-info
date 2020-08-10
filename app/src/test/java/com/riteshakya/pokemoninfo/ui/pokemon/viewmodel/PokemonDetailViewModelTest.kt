package com.riteshakya.pokemoninfo.ui.pokemon.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.riteshakya.pokemoninfo.AndroidTest
import com.riteshakya.pokemoninfo.core.DataResult
import com.riteshakya.pokemoninfo.interactor.pokemon.GetPokemonDetail
import com.riteshakya.pokemoninfo.repository.pokemon.models.PokemonDetail
import com.riteshakya.pokemoninfo.util.getOrAwaitValue
import com.riteshakya.pokemoninfo.util.observeForTesting
import io.reactivex.Single
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotBe
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.internal.verification.Times
import java.util.concurrent.TimeUnit

class PokemonDetailViewModelTest : AndroidTest() {
    private lateinit var pokemonDetailViewModel: PokemonDetailViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var getPokemonDetail: GetPokemonDetail

    private val _pikachuURl = "https://pokeapi.co/api/v2/pokemon/25"
    private val _pikachuDetail = PokemonDetail("pikachu", 0.4f, 112, 6f)
    private val _randomUrl = "https://pokeapi.co/api/v2/pokemon/random"
    private val _randomUrlErrorMessage = "Random Error Messsage"

    @Before
    fun setUp() {
        pokemonDetailViewModel = PokemonDetailViewModel(getPokemonDetail)

        given { getPokemonDetail(_pikachuURl) }.willReturn(
            Single.just(
                _pikachuDetail
            )
        )

        // Throw Error
        given { getPokemonDetail(_randomUrl) }.willReturn(
            Single.error(RuntimeException(_randomUrlErrorMessage))
        )
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `getDetail should trigger the correct argument for interactor`() {
        pokemonDetailViewModel.getDetail(_pikachuURl)

        verify(getPokemonDetail).invoke(_pikachuURl)
    }

    @Test
    fun `getDetail should not trigger the interactor more than once`() {

        pokemonDetailViewModel.getDetail(_pikachuURl)

        verify(getPokemonDetail).invoke(_pikachuURl)

        verifyNoMoreInteractions(getPokemonDetail)
    }

    @Test
    fun `getDetail should call with the same url if the same args is provided`() {
        pokemonDetailViewModel.getDetail(_pikachuURl)

        verify(getPokemonDetail).invoke(_pikachuURl)

        pokemonDetailViewModel.getDetail(_pikachuURl)

        verify(getPokemonDetail, Times(2)).invoke(_pikachuURl)

    }

    @Test
    fun `getDetail should call with the different url if the different args is provided`() {
        pokemonDetailViewModel.getDetail(_pikachuURl)

        pokemonDetailViewModel.getDetail(_randomUrl)

        verify(getPokemonDetail, Times(1)).invoke(_pikachuURl)
        verify(getPokemonDetail, Times(1)).invoke(_randomUrl)
    }

    @Test
    fun `getDetail data should not me modified`() {
        pokemonDetailViewModel.getDetail(_pikachuURl)

        val dataResult = pokemonDetailViewModel.pokemonDetail.getOrAwaitValue()

        dataResult.status shouldBeEqualTo DataResult.Status.SUCCESS
        dataResult.data shouldBeEqualTo _pikachuDetail
    }

    @Test
    fun `getDetail error should not me modified`() {

        pokemonDetailViewModel.getDetail(_randomUrl)

        val dataResult = pokemonDetailViewModel.pokemonDetail.getOrAwaitValue()

        dataResult.status shouldBeEqualTo DataResult.Status.ERROR
        dataResult.message shouldBeEqualTo _randomUrlErrorMessage
    }


    @Test
    fun `pokemonDetail should not receive value until getDetail is not called`() {
        // Keep observing pokemonDetail
        pokemonDetailViewModel.pokemonDetail.observeForTesting {
            // Verify that there is no emission if getDetail isn't called
            pokemonDetailViewModel.pokemonDetail.value shouldBeEqualTo null
        }
    }

    @Test
    fun `pokemonDetail should emit loading after getDetail is called`() {
        val url = _pikachuURl

        // Introduce delay to test loading
        given { getPokemonDetail(_pikachuURl) }.willReturn(
            Single.just(
                _pikachuDetail
            ).delay(1, TimeUnit.SECONDS)
        )
        // Keep observing pokemonDetail
        pokemonDetailViewModel.pokemonDetail.observeForTesting {
            pokemonDetailViewModel.getDetail(url)

            // Verify that the first value is loading
            pokemonDetailViewModel.pokemonDetail.value shouldNotBe null
            pokemonDetailViewModel.pokemonDetail.value?.status shouldBe DataResult.Status.LOADING

        }
    }

    @Test
    fun `getDetail error should not break the flow`() {
        // Keep observing pokemonDetail
        pokemonDetailViewModel.pokemonDetail.observeForTesting {
            pokemonDetailViewModel.getDetail(_randomUrl)

            // Verify that the first value is error
            pokemonDetailViewModel.pokemonDetail.value shouldNotBe null
            pokemonDetailViewModel.pokemonDetail.value?.status shouldBe DataResult.Status.ERROR
            pokemonDetailViewModel.pokemonDetail.value?.message shouldBe _randomUrlErrorMessage

            // Try fetching again
            pokemonDetailViewModel.getDetail(_pikachuURl)

            // Verify that the result is now a success
            pokemonDetailViewModel.pokemonDetail.value shouldNotBe null
            pokemonDetailViewModel.pokemonDetail.value?.status shouldBe DataResult.Status.SUCCESS
            pokemonDetailViewModel.pokemonDetail.value?.data shouldBe _pikachuDetail
        }
    }
}