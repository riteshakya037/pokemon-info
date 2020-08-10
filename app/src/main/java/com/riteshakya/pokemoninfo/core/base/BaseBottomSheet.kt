package com.riteshakya.pokemoninfo.core.base


import android.app.Dialog
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.NonNull
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheet : BottomSheetDialogFragment() {
    abstract val layoutRes: Int

    private val mBottomSheetBehaviorCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(@NonNull bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismissAllowingStateLoss()
            }

        }

        override fun onSlide(@NonNull bottomSheet: View, slideOffset: Float) {
        }
    }

    lateinit var contentView: View

    @CallSuper
    override fun setupDialog(dialog: Dialog, style: Int) {
        contentView = View.inflate(context, layoutRes, null)
        dialog.setContentView(contentView)
        val bottomSheet = contentView.parent as View
        val layoutParams =
            bottomSheet.layoutParams as CoordinatorLayout.LayoutParams
        val behavior = layoutParams.behavior

        if (behavior is BottomSheetBehavior<*>) {
            behavior.setBottomSheetCallback(mBottomSheetBehaviorCallback)
            behavior.isHideable = true
            behavior.skipCollapsed = true
            bottomSheet.post {
                behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }
        }
        bottomSheet.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        initialize(contentView)
    }

    abstract fun initialize(contentView: View)

    fun show(fragmentManager: FragmentManager?) {
        if (!isAdded && fragmentManager != null) {
            val ft = fragmentManager.beginTransaction()
            ft.add(this, tag)
            ft.commitAllowingStateLoss()
        }
    }
}
