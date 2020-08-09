package com.riteshakya.pokemoninfo.ui.components.helpers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.riteshakya.pokemoninfo.R
import com.riteshakya.pokemoninfo.core.DataResult
import com.riteshakya.pokemoninfo.core.isVisible
import kotlinx.android.synthetic.main.network_state_item.view.*

class NetworkStateItemViewHolder(
    private val view: View,
    private val retryCallback: () -> Unit
) : RecyclerView.ViewHolder(view) {

    fun bindTo(dataResult: DataResult<*>) {
        with(view) {
            progressBar.isVisible = dataResult.status == DataResult.Status.LOADING
            errorLayout.isVisible = dataResult.status == DataResult.Status.ERROR
            errorMessage.text =  context.getString(R.string.generic_error_message)

            retryButton.setOnClickListener {
                retryCallback()
            }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup, retryCallback: () -> Unit
        ): NetworkStateItemViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.network_state_item, parent, false)
            return NetworkStateItemViewHolder(view, retryCallback)
        }
    }
}
