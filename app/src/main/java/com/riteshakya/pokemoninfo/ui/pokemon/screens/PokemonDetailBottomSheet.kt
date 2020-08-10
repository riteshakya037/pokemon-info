package com.riteshakya.pokemoninfo.ui.pokemon.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.riteshakya.pokemoninfo.R
import com.riteshakya.pokemoninfo.core.DataResult
import com.riteshakya.pokemoninfo.core.base.BaseBottomSheet
import com.riteshakya.pokemoninfo.core.getFormattedString
import com.riteshakya.pokemoninfo.core.isVisible
import com.riteshakya.pokemoninfo.di.Injectable
import com.riteshakya.pokemoninfo.repository.pokemon.models.PokemonDetail
import com.riteshakya.pokemoninfo.ui.pokemon.viewmodel.PokemonDetailViewModel
import kotlinx.android.synthetic.main.bottom_sheet_pokemon_detail.view.*
import javax.inject.Inject

class PokemonDetailBottomSheet : BaseBottomSheet(), Injectable {
    override val layoutRes: Int = R.layout.bottom_sheet_pokemon_detail

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val pokemonViewModel: PokemonDetailViewModel by viewModels { viewModelFactory }

    override fun initialize(contentView: View) {
        with(contentView) {
            titleTxt.text = getName()
            retryButton.setOnClickListener {
                pokemonViewModel.getDetail(getUrl())
            }
        }

        pokemonViewModel.getDetail(getUrl())

        pokemonViewModel.pokemonDetail.observe(this, Observer {
            when (it.status) {
                DataResult.Status.SUCCESS -> {
                    hideLoading()
                    it.data?.also { data -> setDetails(data) }
                }
                DataResult.Status.ERROR -> {
                    hideLoading()
                }
                DataResult.Status.LOADING -> {
                    setLoading()
                }
            }
            contentView.retryButton.isVisible = it.status == DataResult.Status.ERROR
        })
    }

    private fun setLoading() {
        contentView.progressBar.isVisible = true
    }

    private fun hideLoading() {
        contentView.progressBar.isVisible = false
    }

    private fun setDetails(pokemonDetail: PokemonDetail) {
        with(contentView) {
            heightTxt.text = getFormattedString(R.string.height_format, pokemonDetail.height)
            weightText.text = getFormattedString(R.string.weight_format, pokemonDetail.weight)
            baseExpText.text = pokemonDetail.baseExperience.toString()
        }
    }

    private fun getName(): String {
        if (arguments != null && arguments!!.containsKey(ARGS_TITLE)) {
            return arguments!!.getString(ARGS_TITLE)!!
        }
        return ""
    }

    private fun getUrl(): String {
        if (arguments != null && arguments!!.containsKey(ARGS_URL)) {
            return arguments!!.getString(ARGS_URL)!!
        }
        throw RuntimeException("You need to provide url")
    }


    companion object {
        private const val ARGS_TITLE: String = "args:title"
        private const val ARGS_URL: String = "args:url"

        fun create(title: String, url: String): PokemonDetailBottomSheet =
            Bundle().run {
                putString(ARGS_TITLE, title)
                putString(ARGS_URL, url)
                PokemonDetailBottomSheet().also {
                    it.arguments = this
                }
            }
    }
}