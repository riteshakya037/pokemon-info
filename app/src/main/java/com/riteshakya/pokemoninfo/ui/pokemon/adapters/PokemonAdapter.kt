package com.riteshakya.pokemoninfo.ui.pokemon.adapters

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.riteshakya.pokemoninfo.repository.pokemon.models.Pokemon

class PokemonAdapter : PagedListAdapter<Pokemon, PokemonViewHolder>(POKEMON_COMPARATOR) {
    private var onClickCallback: (Pokemon) -> Unit = {}

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = getItem(position)
        holder.bind(pokemon)
        holder.itemView.setOnClickListener {
            pokemon?.also { onClickCallback(it) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        return PokemonViewHolder.create(parent)
    }

    fun setOnClickCallback(onClickCallback: (Pokemon) -> Unit = {}) {
        this.onClickCallback = onClickCallback
    }

    companion object {

        val POKEMON_COMPARATOR = object : DiffUtil.ItemCallback<Pokemon>() {
            override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean =
                oldItem.name == newItem.name
        }
    }
}
