package com.example.advanced_chrono2.view.custom_adapters

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class OnlySwipeHelper(
    dragDirs: Int,
    swipeDirs: Int,
    private val adapter: ChronometerTimingsAdapter) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs)


{
    //user cannot move the items because are sorted by date
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {

        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int)
        {   adapter.onItemDismiss(viewHolder.layoutPosition)    }
}