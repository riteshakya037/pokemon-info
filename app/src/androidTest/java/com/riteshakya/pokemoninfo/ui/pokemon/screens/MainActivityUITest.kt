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
import com.riteshakya.pokemoninfo.repository.pokemon.models.Pokemon
import com.riteshakya.pokemoninfo.ui.pokemon.helpers.disableProgressBarAnimations
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


@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityUITest {

    @Rule
    @JvmField
    val executorRule = TaskExecutorWithIdlingResourceRule()

    private lateinit var viewModel: PokemonViewModel

    private val navController = mock<NavController>()

    private val initialState = MutableLiveData<DataResult<Unit>>()
    private val loadingState = MutableLiveData<DataResult<Unit>>()
    private val pokemonLiveData = MutableLiveData<PagedList<Pokemon>>()

    @Before
    fun init() {
        viewModel = mock(PokemonViewModel::class.java)
        whenever(viewModel.initialState).thenReturn(initialState)
        whenever(viewModel.loadingState).thenReturn(loadingState)
        whenever(viewModel.pokemonLiveData).thenReturn(pokemonLiveData)

        val scenario = launchFragmentInContainer(themeResId = R.style.AppTheme) {
            PokemonListFragment().apply {
                viewModelFactory = createFor(viewModel)
            }
        }
        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
            fragment.pokemonList.itemAnimator = null
            fragment.disableProgressBarAnimations()
        }
    }

    @Test
    fun loading_should_showProgressAndHideError() {
        initialState.postValue(DataResult.loading())

        onView(withId(R.id.progressBar))
            .check(matches(isDisplayed()))
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(doesNotExist())
    }

    @Test
    fun error_should_showProgressAndHideError() {
        val message = "Error"
        initialState.postValue(DataResult.error(message))

        onView(withId(R.id.progressBar))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))

        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(message)))
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


}