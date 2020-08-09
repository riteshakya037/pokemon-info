package com.riteshakya.pokemoninfo.ui.pokemon.adapters

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.riteshakya.pokemoninfo.core.DataResult
import com.riteshakya.pokemoninfo.repository.pokemon.models.Pokemon
import com.riteshakya.pokemoninfo.ui.components.helpers.NetworkStateItemViewHolder

class PokemonAdapter : PagedListAdapter<Pokemon, RecyclerView.ViewHolder>(POKEMON_COMPARATOR) {
    private var result: DataResult<*> = DataResult.loading<Any>()
    private var retryCallback: () -> Unit = {}
    private var onClickCallback: (Pokemon) -> Unit = {}

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PokemonViewHolder) {
            val pokemon = getItem(position)
            holder.bind(pokemon)
            holder.itemView.setOnClickListener {
                pokemon?.also { onClickCallback(it) }
            }
        } else if (holder is NetworkStateItemViewHolder) {
            holder.bindTo(result)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_PROGRESS) {
            NetworkStateItemViewHolder.create(
                parent,
                retryCallback
            )
        } else PokemonViewHolder.create(parent)
    }

    fun setRetryCallback(retryCallback: () -> Unit = {}) {
        this.retryCallback = retryCallback
    }

    fun setOnClickCallback(onClickCallback: (Pokemon) -> Unit = {}) {
        this.onClickCallback = onClickCallback
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            TYPE_PROGRESS
        } else {
            TYPE_ITEM
        }
    }

    private fun hasExtraRow(): Boolean {
        return result.status != DataResult.Status.SUCCESS
    }

    fun setResultState(newDataResult: DataResult<*>) {
        val previousState = this.result
        val previousExtraRow = hasExtraRow()
        this.result = newDataResult
        val newExtraRow = hasExtraRow()
        if (previousExtraRow != newExtraRow) {
            if (previousExtraRow) {
                notifyItemRemoved(itemCount)
            } else {
                notifyItemInserted(itemCount)
            }
        } else if (newExtraRow && previousState !== newDataResult) {
            notifyItemChanged(itemCount - 1)
        }
    }

    companion object {
        private const val TYPE_PROGRESS = 0
        private const val TYPE_ITEM = 1

        val POKEMON_COMPARATOR = object : DiffUtil.ItemCallback<Pokemon>() {
            override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean =
                oldItem.name == newItem.name
        }
    }
}
