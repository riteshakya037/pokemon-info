package com.riteshakya.pokemoninfo.data.pokemon

import com.riteshakya.pokemoninfo.AndroidTest
import com.riteshakya.pokemoninfo.data.pokemon.datasource.models.PokemonItem
import com.riteshakya.pokemoninfo.di.NetworkModule
import com.riteshakya.pokemoninfo.helpers.FileUtils.readTestResourceFile
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

class PokemonServiceTest : AndroidTest() {
    private var mockWebServer = MockWebServer()

    private lateinit var pokemonService: PokemonService
    private lateinit var retrofit: Retrofit

    private val networkModule = object : NetworkModule() {
        override val interceptor: HttpLoggingInterceptor
            get() = HttpLoggingInterceptor()
    }

    @Before
    fun setup() {
        mockWebServer.start()
        retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create(networkModule.providesGson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(networkModule.providesOkHttpClient(context()))
            .build()
        pokemonService = PokemonService(retrofit)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }


    @Test
    fun `getInitialPokemons should match the data in file`() {

        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(readTestResourceFile("list.json"))

        mockWebServer.enqueue(response)

        val pokemonResponse = pokemonService.getInitialPokemons().blockingGet()

        pokemonResponse.count shouldBeEqualTo 964
        pokemonResponse.next shouldBeEqualTo "https://pokeapi.co/api/v2/pokemon?offset=20&limit=20"
        pokemonResponse.previous shouldBeEqualTo null

        pokemonResponse.results.size shouldBeEqualTo 20

        pokemonResponse.results[3] shouldBeEqualTo PokemonItem(
            "charmander",
            "https://pokeapi.co/api/v2/pokemon/4/"
        )
    }

    @Test
    fun `getSequentialPokemons should match the data in file`() {

        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(readTestResourceFile("morelist.json"))

        mockWebServer.enqueue(response)

        val pokemonResponse =
            pokemonService.getSequentialPokemons("https://pokeapi.co/api/v2/pokemon?offset=20&limit=20")
                .blockingGet()

        pokemonResponse.count shouldBeEqualTo 964
        pokemonResponse.next shouldBeEqualTo "https://pokeapi.co/api/v2/pokemon?offset=40&limit=20"
        pokemonResponse.previous shouldBeEqualTo "https://pokeapi.co/api/v2/pokemon?offset=0&limit=20"

        pokemonResponse.results.size shouldBeEqualTo 20

        pokemonResponse.results[3] shouldBeEqualTo PokemonItem(
            "arbok",
            "https://pokeapi.co/api/v2/pokemon/24/"
        )
    }


    @Test
    fun `getInitialPokemons should throw appropriate http error for unauthorized`() {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_UNAUTHORIZED)

        mockWebServer.enqueue(response)

        pokemonService.getInitialPokemons().subscribe({}, {
            testForUnauthorized(it)
        })
    }

    @Test
    fun `getInitialPokemons should throw appropriate http error for bad request`() {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)

        mockWebServer.enqueue(response)

        pokemonService.getInitialPokemons().subscribe({}, {
            testForBadRequest(it)
        })
    }

    private fun testForUnauthorized(it: Throwable?) {
        it shouldBeInstanceOf HttpException::class.java

        val httpException = it as HttpException

        httpException.code() shouldBeEqualTo 401
    }

    private fun testForBadRequest(it: Throwable?) {
        it shouldBeInstanceOf HttpException::class.java

        val httpException = it as HttpException

        httpException.code() shouldBeEqualTo 400
    }
}