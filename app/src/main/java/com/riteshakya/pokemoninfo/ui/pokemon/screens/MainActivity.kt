package com.riteshakya.pokemoninfo.ui.pokemon.screens

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.riteshakya.pokemoninfo.R
import com.riteshakya.pokemoninfo.repository.pokemon.models.Pokemon
import com.riteshakya.pokemoninfo.ui.components.helpers.UniformPaddingDecoration
import com.riteshakya.pokemoninfo.ui.pokemon.adapters.PokemonAdapter
import com.riteshakya.pokemoninfo.ui.pokemon.viewmodel.PokemonViewModel
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

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
    }

    private fun initAdapter() {
        with(pokemonList) {
            layoutManager = LinearLayoutManager(context)

            val padding = resources.getDimensionPixelSize(R.dimen.vertical_margin_small)
            val paddingEdges = resources.getDimensionPixelSize(R.dimen.vertical_margin)
            addItemDecoration(UniformPaddingDecoration(padding, paddingEdges))

            adapter = pokemonAdapter

            pokemonAdapter.setOnClickCallback {
                showPokemonDetail(it)
            }

        }
    }

    private fun showPokemonDetail(pokemon: Pokemon) {

    }
}