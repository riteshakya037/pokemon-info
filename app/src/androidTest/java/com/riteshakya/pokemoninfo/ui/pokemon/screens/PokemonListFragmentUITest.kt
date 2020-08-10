package com.riteshakya.pokemoninfo.ui.pokemon.screens

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.paging.PagedList
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.riteshakya.pokemoninfo.R
import com.riteshakya.pokemoninfo.core.DataResult
import com.riteshakya.pokemoninfo.navigator.Navigator
import com.riteshakya.pokemoninfo.repository.pokemon.models.Pokemon
import com.riteshakya.pokemoninfo.ui.pokemon.helpers.RecyclerViewItemCountAssertion
import com.riteshakya.pokemoninfo.ui.pokemon.helpers.RecyclerViewMatcher
import com.riteshakya.pokemoninfo.ui.pokemon.helpers.disableProgressBarAnimations
import com.riteshakya.pokemoninfo.ui.pokemon.helpers.mockPagedList
import com.riteshakya.pokemoninfo.ui.pokemon.viewmodel.PokemonViewModel
import com.riteshakya.pokemoninfo.util.TaskExecutorWithIdlingResourceRule
import com.riteshakya.pokemoninfo.util.ViewModelUtil.createFor
import com.riteshakya.pokemoninfo.util.mock
import kotlinx.android.synthetic.main.fragment_pokemon_list.*
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock

/**
 * Remember to turn off system animations on the virtual or physical devices used for testing. On your device, under Settings > Developer options.
 */

@RunWith(AndroidJUnit4::class)
@LargeTest
class PokemonListFragmentUITest {

    @Rule
    @JvmField
    val executorRule = TaskExecutorWithIdlingResourceRule()

    private lateinit var fragment: PokemonListFragment
    private lateinit var viewModel: PokemonViewModel

    private val navController = mock<NavController>()
    private val mockNavigator = mock<Navigator>()

    private val initialState = MutableLiveData<DataResult<Unit>>()
    private val loadingState = MutableLiveData<DataResult<Unit>>()
    private val pokemonLiveData = MutableLiveData<PagedList<Pokemon>>()

    private val _pikachuItem = Pokemon("pikachu", "https://pokeapi.co/api/v2/pokemon/25/")
    private val _bulbasaurItem = Pokemon("bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/")

    private val _mockList = listOf(
        _pikachuItem,
        _bulbasaurItem
    )

    @Before
    fun init() {
        viewModel = mock(PokemonViewModel::class.java)
        whenever(viewModel.initialState).thenReturn(initialState)
        whenever(viewModel.loadingState).thenReturn(loadingState)
        whenever(viewModel.pokemonLiveData).thenReturn(pokemonLiveData)

        val scenario = launchFragmentInContainer(themeResId = R.style.AppTheme) {
            PokemonListFragment().apply {
                viewModelFactory = createFor(viewModel)
                navigator = mockNavigator
            }
        }
        scenario.onFragment { fragment ->
            this.fragment = fragment
            Navigation.setViewNavController(fragment.requireView(), navController)
            fragment.pokemonList.itemAnimator = null
            fragment.disableProgressBarAnimations()
        }
    }

    @Test
    fun initialLoading_should_showProgressAndHideError() {
        initialState.postValue(DataResult.loading())

        onView(withId(R.id.progressLoadingInitial))
            .check(matches(isDisplayed()))
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(doesNotExist())
    }

    @Test
    fun initialError_should_showProgressAndHideError() {
        val message = "Error"
        initialState.postValue(DataResult.error(message))

        onView(withId(R.id.progressLoadingInitial))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))

        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(message)))
    }


    @Test
    fun initialSuccess_should_hideErrorAndProgress() {
        initialState.postValue(DataResult.success(Unit))

        onView(withId(R.id.progressLoadingInitial))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))

        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(doesNotExist())
    }

    @Test
    fun retry_should_callRelevantFunction() {
        val message = "Error"
        initialState.postValue(DataResult.error(message))

        onView(
            allOf(
                withId(com.google.android.material.R.id.snackbar_action),
                withText(R.string.retry_text)
            )
        )
            .perform(click())

        verify(viewModel).refresh()
    }

    @Test
    fun liveData_should_populateAdapter() {
        pokemonLiveData.postValue(_mockList.mockPagedList())
        loadingState.postValue(DataResult.success(Unit))

        onView(withId(R.id.pokemonList)).check(RecyclerViewItemCountAssertion(_mockList.size))
    }


    private fun listMatcher(): RecyclerViewMatcher {
        return RecyclerViewMatcher(R.id.pokemonList)
    }

    @Test
    fun loadingState_should_populateAdapterWithLoader() {
        pokemonLiveData.postValue(_mockList.mockPagedList())
        loadingState.postValue(DataResult.loading())

        onView(withId(R.id.pokemonList)).check(RecyclerViewItemCountAssertion(_mockList.size))

        onView(withId(R.id.progressBar))
            .check(matches(isDisplayed()))
    }

    @Test
    fun liveData_should_populateDisplayDataAppropriately() {
        pokemonLiveData.postValue(_mockList.mockPagedList())
        loadingState.postValue(DataResult.success(Unit))

        onView(withId(R.id.pokemonList)).check(RecyclerViewItemCountAssertion(_mockList.size))

        onView(listMatcher().atPosition(0))
            .check(matches(hasDescendant(withText(_pikachuItem.name))))
        onView(listMatcher().atPosition(1))
            .check(matches(hasDescendant(withText(_bulbasaurItem.name))))
    }

    @Test
    fun itemClick_should_callNavigateWithRightArguments() {
        pokemonLiveData.postValue(_mockList.mockPagedList())
        loadingState.postValue(DataResult.success(Unit))

        val position = 0

        onView(listMatcher().atPosition(position))
            .perform(click())

        verify(mockNavigator).showPokemonDetail(
            fragment,
            _mockList[position].name,
            _mockList[position].url
        )

    }

    @Test
    fun loadingErrorState_should_showRetry() {
        pokemonLiveData.postValue(_mockList.mockPagedList())
        loadingState.postValue(DataResult.error(""))

        onView(withId(R.id.retryButton))
            .check(matches(isDisplayed()))
            .check(matches(withText(R.string.retry_text)))

        onView(withId(R.id.errorMessage))
            .check(matches(isDisplayed()))
            .check(matches(withText(R.string.generic_error_message)))
    }

    @Test
    fun clickingReload_should_callRelevantFunction() {
        pokemonLiveData.postValue(_mockList.mockPagedList())
        loadingState.postValue(DataResult.error(""))

        onView(withId(R.id.retryButton))
            .perform(click())

        verify(viewModel).retry()
    }

}