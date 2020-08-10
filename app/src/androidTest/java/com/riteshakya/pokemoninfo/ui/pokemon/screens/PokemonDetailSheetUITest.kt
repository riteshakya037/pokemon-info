package com.riteshakya.pokemoninfo.ui.pokemon.screens

import androidx.fragment.app.testing.launchFragment
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.nhaarman.mockitokotlin2.whenever
import com.riteshakya.pokemoninfo.R
import com.riteshakya.pokemoninfo.core.DataResult
import com.riteshakya.pokemoninfo.core.getFormattedString
import com.riteshakya.pokemoninfo.repository.pokemon.models.PokemonDetail
import com.riteshakya.pokemoninfo.ui.pokemon.viewmodel.PokemonDetailViewModel
import com.riteshakya.pokemoninfo.util.TaskExecutorWithIdlingResourceRule
import com.riteshakya.pokemoninfo.util.ViewModelUtil.createFor
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
class PokemonDetailSheetUITest {

    @Rule
    @JvmField
    val executorRule = TaskExecutorWithIdlingResourceRule()

    private lateinit var viewModel: PokemonDetailViewModel

    private val _pikachuURl = "https://pokeapi.co/api/v2/pokemon/25"
    private val _pikachuName = "pikachu"
    private val _pikachuDetail = PokemonDetail("pikachu", 0.4f, 112, 6f)

    private val pokemonDetail = MutableLiveData<DataResult<PokemonDetail>>()
    private val context by lazy { InstrumentationRegistry.getInstrumentation().targetContext }

    @Before
    fun init() {
        viewModel = mock(PokemonDetailViewModel::class.java)
        whenever(viewModel.pokemonDetail).thenReturn(pokemonDetail)

        launchFragment(
            themeResId = R.style.AppTheme,
            fragmentArgs = PokemonDetailBottomSheet.createBundle(_pikachuName, _pikachuURl)
        ) {
            PokemonDetailBottomSheet().apply {
                viewModelFactory = createFor(viewModel)
            }
        }
    }

    @Test
    fun initialLoading_should_showProgressAndHideError() {
        pokemonDetail.postValue(DataResult.loading())

        onView(withId(R.id.progressLoadingDetail))
            .check(matches(isDisplayed()))

        onView(withId(R.id.retryButton))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun initialError_should_showError() {
        pokemonDetail.postValue(DataResult.error(""))

        onView(withId(R.id.retryButton))
            .check(matches(isDisplayed()))

        onView(withId(R.id.progressLoadingDetail))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
    }


    @Test
    fun initialSuccess_should_populateData() {
        pokemonDetail.postValue(DataResult.success(_pikachuDetail))

        onView(withId(R.id.titleTxt))
            .check(matches(withText(_pikachuName)))

        onView(withId(R.id.heightTxt))
            .check(
                matches(
                    withText(
                        context.getFormattedString(
                            R.string.height_format,
                            _pikachuDetail.height
                        )
                    )
                )
            )

        onView(withId(R.id.weightText))
            .check(
                matches(
                    withText(
                        context.getFormattedString(
                            R.string.weight_format,
                            _pikachuDetail.weight
                        )
                    )
                )
            )

        onView(withId(R.id.baseExpText))
            .check(matches(withText(_pikachuDetail.baseExperience.toString())))

        onView(withId(R.id.retryButton))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
    }
}