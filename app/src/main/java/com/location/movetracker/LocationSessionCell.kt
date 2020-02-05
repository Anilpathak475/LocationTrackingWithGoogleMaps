package com.location.movetracker

import android.view.View
import android.view.ViewGroup
import com.location.movetracker.database.LocationHistory

import de.savedroid.ui.recyclerview.BaseCell
import de.savedroid.ui.recyclerview.LayoutViewHolder

class LocationSessionCell(
    val smoovePackage: LocationHistory,
    onSelected: (LocationSessionCell) -> Unit
) : BaseCell<LocationSessionCell.ViewHolder>() {

    override val viewHolderFactory: (ViewGroup) -> ViewHolder = { ViewHolder(it) }

    private val selectListener = View.OnClickListener {
        onSelected(this@LocationSessionCell)
    }

    override fun onViewAttached(vh: ViewHolder) {
        super.onViewAttached(vh)

    }

    class ViewHolder(parent: ViewGroup) : LayoutViewHolder(parent, R.layout.activity_easy_maps) {

        override fun bind(rootView: View) {

        }
    }
}
