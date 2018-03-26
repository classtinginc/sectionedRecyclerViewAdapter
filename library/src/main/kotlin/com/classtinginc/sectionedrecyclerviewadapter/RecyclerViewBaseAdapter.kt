package com.classtinginc.sectionedrecyclerviewadapter

import android.view.View

/**
 * Created by BN on 2015. 11. 18..
 */
abstract class RecyclerViewBaseAdapter<T>(protected val context: android.content.Context) : android.support.v7.widget.RecyclerView.Adapter<ViewWrapper<View>>() {

    companion object {

        val TYPE_HEADER = 0
        val TYPE_FOOTER = 1
        val TYPE_DEFAULT = 2
    }

    var listener: OnItemClickListener? = null
    var listItems: MutableList<T> = mutableListOf()

    abstract fun onCreateItemView(parent: android.view.ViewGroup, viewType: Int): android.view.View

    abstract fun onCreateHeaderView(parent: android.view.ViewGroup, viewType: Int): android.view.View

    abstract fun onCreateFooterView(parent: android.view.ViewGroup, viewType: Int): android.view.View

    abstract fun useHeader(): Boolean

    abstract fun useFooter(): Boolean

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ViewWrapper<View> =
            when (viewType) {
                RecyclerViewBaseAdapter.Companion.TYPE_HEADER -> ViewWrapper(onCreateHeaderView(parent, viewType))
                RecyclerViewBaseAdapter.Companion.TYPE_FOOTER -> ViewWrapper(onCreateFooterView(parent, viewType))
                else -> ViewWrapper(onCreateItemView(parent, viewType))
            }

    override fun getItemCount(): Int {
        var count = listItems.size
        if (useHeader()) {
            count += 1
        }
        if (useFooter()) {
            count += 1
        }
        return count
    }

    fun getItem(position: Int): T? {
        if (getItemViewType(position) == RecyclerViewBaseAdapter.Companion.TYPE_HEADER) {
            return null
        }
        if (getItemViewType(position) == RecyclerViewBaseAdapter.Companion.TYPE_FOOTER) {
            return null
        }
        if (useHeader()) {
            return listItems[position - 1]
        }
        return listItems[position]
    }

    override fun getItemViewType(position: Int): Int = when {
        position == 0 && useHeader() -> RecyclerViewBaseAdapter.Companion.TYPE_HEADER
        position == itemCount - 1 && useFooter() -> RecyclerViewBaseAdapter.Companion.TYPE_FOOTER
        else -> RecyclerViewBaseAdapter.Companion.TYPE_DEFAULT
    }

    open fun notifyDataSetChanged(items: MutableList<T>) {
        listItems.clear()
        listItems.addAll(items)
        notifyDataSetChanged()
    }

    open fun notifyItemRangeInserted(addedItems: MutableList<T>) {
        val start = listItems.size
        listItems.addAll(addedItems)
        notifyItemsRangeInserted(start, addedItems.size)
    }

    open fun notifyItemsRangeInserted(position: Int, size: Int) =
            notifyItemRangeInserted(position + (if (useHeader()) 1 else 0), size)

    open fun notifyHeaderItemChanged() = notifyItemChanged(0)

    open fun notifyItemDataChanged(position: Int) =
            notifyItemChanged(position + (if (useHeader()) 1 else 0))

    open fun notifyAllItemsChanged() = notifyItemRangeChanged(0, itemCount)

    open fun notifyItemDataRemoved(position: Int) =
            notifyItemRemoved(position + (if (useHeader()) 1 else 0))

    open fun notifyAllItemsRemoved() {
        listItems.clear()
        notifyDataSetChanged()
    }
}