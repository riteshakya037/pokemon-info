package com.riteshakya.pokemoninfo.ui.components.helpers.decorators

import android.graphics.Rect
import android.view.View
import androidx.annotation.StringDef
import androidx.recyclerview.widget.RecyclerView

class UniformPaddingDecoration(
    private val padding: Int,
    private val paddingEdges: Int = 0,
    @Direction private val direction: String = VERTICAL
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        when ((view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition) {
            0 -> outRect.set(
                if (direction == HORIZONTAL) paddingEdges else 0,
                if (direction == VERTICAL) paddingEdges else 0,
                if (direction == HORIZONTAL) padding else 0,
                if (direction == VERTICAL) padding else 0
            )
            parent.adapter!!.itemCount - 1 -> outRect.set(
                0,
                0,
                if (direction == HORIZONTAL) paddingEdges else 0,
                if (direction == VERTICAL) paddingEdges else 0
            )
            else -> outRect.set(
                0,
                0,
                if (direction == HORIZONTAL) padding else 0,
                if (direction == VERTICAL) padding else 0
            )
        }
    }

    companion object {
        @StringDef(
            VERTICAL,
            HORIZONTAL
        )
        annotation class Direction

        const val VERTICAL = "vertical"
        const val HORIZONTAL = "horizontal"
    }
}