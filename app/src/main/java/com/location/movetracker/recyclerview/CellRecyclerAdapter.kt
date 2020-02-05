package de.savedroid.ui.recyclerview

import android.util.Log
import android.util.SparseArray
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlin.reflect.KClass

/**
 * General Recyclerview adapter that can handle list with any kind of items.
 * The items must implement the Cell<*> interface
 * Never implement the adapter again
 */
class CellAdapterViewHolder(val cellLayout: CellViewHolder) :
    RecyclerView.ViewHolder(cellLayout.rootView) {
    var cell: Cell<*>? = null
}

internal data class CellType(val id: Int, val layoutFactory: (ViewGroup) -> CellViewHolder)

internal fun <T : CellViewHolder> Cell<T>.attach(vh: Any?) {
    @Suppress("UNCHECKED_CAST")
    onViewAttached(vh as T)
}

internal fun <T : CellViewHolder> Cell<T>.detach(vh: Any?) {
    @Suppress("UNCHECKED_CAST")
    onViewDetached(vh as T)
}

class CellRecyclerAdapter(private val mCellList: List<Cell<*>>) :
    RecyclerView.Adapter<CellAdapterViewHolder>() {
    private var typeIdCounter = 0
    //KClass -> CellType map (maybe this should be companion object.
    private val classTypeMap = mutableMapOf<KClass<*>, CellType>()
    private val mCellTypes = SparseArray<CellType>()


    override fun getItemViewType(position: Int): Int {
        val cell = mCellList[position]

        val viewType: CellType =
            classTypeMap[cell::class] ?: CellType(++typeIdCounter, cell.viewHolderFactory)
        classTypeMap[cell::class] = viewType
        mCellTypes.put(viewType.id, viewType)

        return viewType.id
    }

    override fun onCreateViewHolder(parent: ViewGroup, cellTypeId: Int): CellAdapterViewHolder {
        Log.d("Test", "onCreateViewHolder type id: $cellTypeId")
        val cellType = mCellTypes.get(cellTypeId)

        @Suppress("UNCHECKED_CAST")
        val cellLayout = cellType.layoutFactory(parent)
        return CellAdapterViewHolder(cellLayout)

    }

    override fun onBindViewHolder(holder: CellAdapterViewHolder, position: Int) {
        val cell = mCellList[position]
        if (cell != holder.cell) { // only bind if changed
            Log.d("Test", "onBindViewHolder pos: $position")
            //cell.bindViewHolder(holder.cellLayout)
            holder.cell = cell
        }
    }

    override fun onViewAttachedToWindow(holder: CellAdapterViewHolder) {
        holder.cell?.attach(holder.cellLayout)
    }

    override fun onViewDetachedFromWindow(holder: CellAdapterViewHolder) {
        holder.cell?.detach(holder.cellLayout)
    }

    override fun getItemCount(): Int {
        return mCellList.size
    }
}

