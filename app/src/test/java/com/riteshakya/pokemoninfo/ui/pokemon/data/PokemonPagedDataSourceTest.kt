package com.riteshakya.pokemoninfo.ui.pokemon.data

import androidx.paging.PageKeyedDataSource
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.verify
import com.riteshakya.pokemoninfo.AndroidTest
import com.riteshakya.pokemoninfo.constants.Constants
import com.riteshakya.pokemoninfo.core.DataResult
import com.riteshakya.pokemoninfo.helpers.LoadCallback
import com.riteshakya.pokemoninfo.helpers.LoadInitialCallback
import com.riteshakya.pokemoninfo.helpers.PokemonResultHelper
import com.riteshakya.pokemoninfo.helpers.getOrAwaitValue
import com.riteshakya.pokemoninfo.interactor.pokemon.GetPokemons
import com.riteshakya.pokemoninfo.repository.pokemon.models.Pokemon
import com.riteshakya.pokemoninfo.repository.pokemon.models.PokemonResult
import io.reactivex.Single
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.internal.verification.Times
import java.util.concurrent.TimeUnit

class PokemonPagedDataSourceTest : AndroidTest() {
    private lateinit var pokemonPagedDataSource: PokemonPagedDataSource

    @Mock
    private lateinit var getPokemons: GetPokemons

    @Mock
    private lateinit var loadInitialCallback: PageKeyedDataSource.LoadInitialCallback<String, Pokemon>

    @Mock
    private lateinit var loadCallback: PageKeyedDataSource.LoadCallback<String, Pokemon>

    private val _pikachuItem = Pokemon("pikachu", "https://pokeapi.co/api/v2/pokemon/25/")
    private val _bulbasaurItem = Pokemon("bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/")
    private val _nextUrl = "https://pokeapi.co/api/v2/pokemon?offset=20&limit=20"

    private val _initialLoadParams = PageKeyedDataSource.LoadInitialParams<String>(
        Constants.INITIAL_POKEMON_LOAD,
        false
    )

    private val _loadParams = PageKeyedDataSource.LoadParams(
        _nextUrl,
        20
    )

    private val _initialResponse = PokemonResult(
        count = 20,
        next = _nextUrl,
        previous = "",
        data = listOf(_pikachuItem, _bulbasaurItem)
    )

    private val _afterResponse = PokemonResult(
        count = 20,
        next = "",
        previous = "",
        data = listOf(_pikachuItem, _bulbasaurItem)
    )

    @Before
    fun setUp() {
        pokemonPagedDataSource = PokemonPagedDataSource(getPokemons)

        given { getPokemons() }.willReturn(
            Single.just(_initialResponse)
        )

        given { getPokemons(_nextUrl) }.willReturn(
            Single.just(_afterResponse)
        )
    }

    @Test
    fun `loadInitial should trigger the correct argument for interactor`() {
        pokemonPagedDataSource.loadInitial(_initialLoadParams, loadInitialCallback)

        verify(getPokemons).invoke()
    }

    @Test
    fun `loadAfter should trigger the correct argument for interactor`() {
        pokemonPagedDataSource.loadAfter(_loadParams, loadCallback)

        verify(getPokemons).invoke(_nextUrl)
    }

    @Test
    fun `loadInitial should not change the data received from interactor`() {

        pokemonPagedDataSource.loadInitial(
            _initialLoadParams,
            object : LoadInitialCallback<String, Pokemon>() {
                override fun onResult(
                    data: MutableList<Pokemon>,
                    previousPageKey: String?,
                    nextPageKey: String?
                ) {
                    data shouldBe _initialResponse.data
                    previousPageKey shouldBe _initialResponse.previous
                    nextPageKey shouldBe _initialResponse.next
                }
            })
    }

    @Test
    fun `loadAfter should not change the data received from interactor`() {

        pokemonPagedDataSource.loadAfter(
            _loadParams,
            object : LoadCallback<String, Pokemon>() {
                override fun onResult(data: MutableList<Pokemon>, adjacentPageKey: String?) {
                    data shouldBeEqualTo _initialResponse.data
                    adjacentPageKey shouldBe ""
                }
            })
    }

    @Test
    fun `refresh should call the same args as loadInitial`() {
        pokemonPagedDataSource.loadInitial(_initialLoadParams, loadInitialCallback)

        verify(getPokemons).invoke()

        pokemonPagedDataSource.refresh()

        verify(getPokemons, Times(2)).invoke()

    }

    @Test
    fun `retryPagination should call the same args as loadAfter`() {

        val key = "testurl"
        val testParam = PageKeyedDataSource.LoadParams(
            key,
            20
        )

        given { getPokemons(key) }.willReturn(PokemonResultHelper.emptySingle())

        pokemonPagedDataSource.loadAfter(testParam, loadCallback)

        verify(getPokemons).invoke(key)

        pokemonPagedDataSource.retryPagination()

        verify(getPokemons, Times(2)).invoke(key)
    }


    @Test
    fun `loadInitial should emit success`() {

        pokemonPagedDataSource.loadInitial(_initialLoadParams, loadInitialCallback)

        val dataResult = pokemonPagedDataSource.initialLoadStateLiveData.getOrAwaitValue()

        dataResult.status shouldBe DataResult.Status.SUCCESS
    }

    @Test
    fun `loadInitial should complete network state if next isn't available`() {

        given { getPokemons() }.willReturn(
            Single.just(_afterResponse)
        )

        pokemonPagedDataSource.loadInitial(_initialLoadParams, loadInitialCallback)

        val dataResult = pokemonPagedDataSource.initialLoadStateLiveData.getOrAwaitValue()

        dataResult.status shouldBe DataResult.Status.SUCCESS

        val networkDataState = pokemonPagedDataSource.paginatedNetworkStateLiveData.getOrAwaitValue()

        networkDataState.status shouldBe DataResult.Status.SUCCESS
    }


    @Test
    fun `loadInitial should emit loading if delay is introduced`() {
        given { getPokemons() }.willReturn(
            PokemonResultHelper.emptySingle().delay(2, TimeUnit.SECONDS)
        )

        pokemonPagedDataSource.loadInitial(_initialLoadParams, loadInitialCallback)

        val dataResult = pokemonPagedDataSource.initialLoadStateLiveData.getOrAwaitValue()

        dataResult.status shouldBe DataResult.Status.LOADING
    }


    @Test
    fun `loadInitial should emit error with appropriate message on interactor error`() {
        val errorMessage = "random error"
        given { getPokemons() }.willReturn(
            Single.error(RuntimeException(errorMessage))
        )

        pokemonPagedDataSource.loadInitial(_initialLoadParams, loadInitialCallback)

        val dataResult = pokemonPagedDataSource.initialLoadStateLiveData.getOrAwaitValue()

        dataResult.status shouldBe DataResult.Status.ERROR
        dataResult.message shouldBe errorMessage
    }


    @Test
    fun `loadAfter should emit success`() {

        pokemonPagedDataSource.loadAfter(_loadParams, loadCallback)

        val dataResult = pokemonPagedDataSource.paginatedNetworkStateLiveData.getOrAwaitValue()

        dataResult.status shouldBe DataResult.Status.SUCCESS
    }

    @Test
    fun `loadAfter should emit loading if delay is introduced`() {
        given { getPokemons(_nextUrl) }.willReturn(
            PokemonResultHelper.emptySingle().delay(2, TimeUnit.SECONDS)
        )

        pokemonPagedDataSource.loadAfter(_loadParams, loadCallback)

        val dataResult = pokemonPagedDataSource.paginatedNetworkStateLiveData.getOrAwaitValue()

        dataResult.status shouldBe DataResult.Status.LOADING
    }

    @Test
    fun `loadAfter should emit error with appropriate message on interactor error`() {
        val errorMessage = "random error"
        given { getPokemons(_nextUrl) }.willReturn(
            Single.error(RuntimeException(errorMessage))
        )

        pokemonPagedDataSource.loadAfter(_loadParams, loadCallback)

        val dataResult = pokemonPagedDataSource.paginatedNetworkStateLiveData.getOrAwaitValue()

        dataResult.status shouldBe DataResult.Status.ERROR
        dataResult.message shouldBe errorMessage
    }
}