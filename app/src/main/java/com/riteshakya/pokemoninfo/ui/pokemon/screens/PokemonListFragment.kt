package com.riteshakya.pokemoninfo.ui.pokemon.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.riteshakya.pokemoninfo.R
import com.riteshakya.pokemoninfo.core.DataResult
import com.riteshakya.pokemoninfo.core.isVisible
import com.riteshakya.pokemoninfo.di.Injectable
import com.riteshakya.pokemoninfo.navigator.Navigator
import com.riteshakya.pokemoninfo.repository.pokemon.models.Pokemon
import com.riteshakya.pokemoninfo.ui.components.helpers.decorators.UniformPaddingDecoration
import com.riteshakya.pokemoninfo.ui.pokemon.adapters.PokemonAdapter
import com.riteshakya.pokemoninfo.ui.pokemon.viewmodel.PokemonViewModel
import kotlinx.android.synthetic.main.fragment_pokemon_list.*
import javax.inject.Inject

class PokemonListFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var navigator: Navigator

    private val pokemonViewModel: PokemonViewModel by viewModels {
        viewModelFactory
    }

    private var snackBar: Snackbar? = null

    private val pokemonAdapter by lazy { PokemonAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pokemon_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()

        pokemonViewModel.pokemonLiveData.observe(viewLifecycleOwner, Observer {
            pokemonAdapter.submitList(it)
        })

        pokemonViewModel.loadingState.observe(viewLifecycleOwner, Observer {
            pokemonAdapter.setResultState(it)
        })

        pokemonViewModel.initialState.observe(viewLifecycleOwner, Observer {
            progressLoadingInitial.isVisible = it.status == DataResult.Status.LOADING
            if (it.status == DataResult.Status.ERROR) {
                showErrorSnackBar(it.message ?: "")
            } else {
                hideSnackBar()
            }
        })
    }

    private fun hideSnackBar() {
        snackBar?.dismiss()
    }

    private fun showErrorSnackBar(message: String) {
        snackBar =
            Snackbar.make(view!!, message, Snackbar.LENGTH_INDEFINITE)
                .apply {
                    setAction(R.string.retry_text) {
                        pokemonViewModel.refresh()
                    }
                    show()
                }
    }

    private fun initAdapter() {
        with(pokemonList) {
            layoutManager = LinearLayoutManager(context)

            val padding = resources.getDimensionPixelSize(R.dimen.vertical_margin_small)
            val paddingEdges = resources.getDimensionPixelSize(R.dimen.vertical_margin)
            addItemDecoration(UniformPaddingDecoration(padding, paddingEdges))

            adapter = pokemonAdapter

            pokemonAdapter.setRetryCallback {
                pokemonViewModel.retry()
            }

            pokemonAdapter.setOnClickCallback {
                showPokemonDetail(it)
            }
        }
    }

    private fun showPokemonDetail(pokemon: Pokemon) {
        navigator.showPokemonDetail(this, pokemon.name, pokemon.url)
    }

}