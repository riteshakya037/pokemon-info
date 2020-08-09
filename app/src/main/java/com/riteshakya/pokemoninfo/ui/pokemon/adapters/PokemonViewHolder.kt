package com.riteshakya.pokemoninfo.ui.pokemon.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.riteshakya.pokemoninfo.R
import com.riteshakya.pokemoninfo.repository.pokemon.models.Pokemon
import kotlinx.android.synthetic.main.list_item_pokemon.view.*

class PokemonViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(post: Pokemon?) {
        with(view) {
            itemTitle.text = post?.name ?: ""
        }
    }

    companion object {
        fun create(parent: ViewGroup): PokemonViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_pokemon, parent, false)
            return PokemonViewHolder(view)
        }
    }
}