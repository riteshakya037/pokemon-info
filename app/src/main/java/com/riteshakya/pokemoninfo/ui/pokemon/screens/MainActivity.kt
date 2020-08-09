package com.riteshakya.pokemoninfo.ui.pokemon.screens

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.riteshakya.pokemoninfo.R
import com.riteshakya.pokemoninfo.core.DataResult
import com.riteshakya.pokemoninfo.core.isVisible
import com.riteshakya.pokemoninfo.repository.pokemon.models.Pokemon
import com.riteshakya.pokemoninfo.ui.components.helpers.decorators.UniformPaddingDecoration
import com.riteshakya.pokemoninfo.ui.pokemon.adapters.PokemonAdapter
import com.riteshakya.pokemoninfo.ui.pokemon.viewmodel.PokemonViewModel
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    private var snackBar: Snackbar? = null

    private val pokemonAdapter by lazy { PokemonAdapter() }

    @Inject
    lateinit var pokemonViewModel: PokemonViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initAdapter()

        pokemonViewModel.pokemonLiveData.observe(this, Observer {
            pokemonAdapter.submitList(it)
        })

        pokemonViewModel.loadingState.observe(this, Observer {
            pokemonAdapter.setResultState(it)
        })

        pokemonViewModel.initialState.observe(this, Observer {
            progressBar.isVisible = it.status == DataResult.Status.LOADING
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
        snackBar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE)
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

    }
}