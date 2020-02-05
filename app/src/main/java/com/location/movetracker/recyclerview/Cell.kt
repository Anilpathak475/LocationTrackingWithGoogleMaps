package de.savedroid.ui.recyclerview

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import java.lang.ref.WeakReference

/**
 * @author Timo Drick
 */

/**
 * Cell represents a ui component that can be shown as part of a list inside of the Recyclerview
 * Here we can put the layout definition and layout update methods together.
 */
interface Cell<TVIEWHOLDER : CellViewHolder> {
    val viewHolderFactory: (ViewGroup) -> TVIEWHOLDER
    fun onViewAttached(vh: TVIEWHOLDER)
    fun onViewDetached(vh: TVIEWHOLDER)
}

/**
 * ViewHolder of the cells just need to export the rootView
 * You can use this class to store references to the views
 */
interface CellViewHolder {
    val rootView: View
}

abstract class BaseCell<TVIEWHOLDER : CellViewHolder> : Cell<TVIEWHOLDER> {
    private var attachedViewHolder: WeakReference<TVIEWHOLDER>? = null


    override fun onViewAttached(vh: TVIEWHOLDER) {
        attachedViewHolder = WeakReference(vh)
    }

    override fun onViewDetached(vh: TVIEWHOLDER) {
        attachedViewHolder = null
    }

    fun update() {
        attachedViewHolder?.get()?.let { onViewAttached(it) }
    }

    fun whenAttached(invoke: (TVIEWHOLDER).() -> Unit) {
        attachedViewHolder?.get()?.let {
            invoke(it)
        }
    }
}

abstract class LayoutViewHolder(parent: ViewGroup, @LayoutRes resLayout: Int) : CellViewHolder {
    override val rootView: View = with(parent.context) {
        val v = android.view.LayoutInflater.from(parent.context).inflate(resLayout, parent, false)
        bind(v)
        v
    }

    abstract fun bind(rootView: View)
}
