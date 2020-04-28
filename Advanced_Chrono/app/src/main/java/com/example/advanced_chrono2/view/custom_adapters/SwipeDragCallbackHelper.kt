package com.example.advanced_chrono2.view.custom_adapters

import android.graphics.Color
import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

//This class helps the timer items in Timer Fragment to be swipable left and right and ti be draggable up and down

class SwipeDragCallbackHelper(
    dragDirs: Int,
    swipeDirs: Int,
    private val adapter: TimerItemsAdapter

) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs)

{

    override fun isLongPressDragEnabled(): Boolean
    {
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean
    {
        return true
    }

    //https://stackoverflow.com/questions/39335214/rounded-corners-cardview-dont-work-in-recyclerview-android/44064045#44064045
    //CHANGING THE BACKGROUND COLOR MAKES THE CARDVIEW VANISH THE RADIUS PROPERTY SO I NEED TO IMPLEMENT MY OWN CARDVIEW

    //setting the new color in clear view as the same of the first time
    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder)
    {
        viewHolder.itemView.setBackgroundColor((Color.parseColor("#FFFFFF")))
    }

    //Setting the item color if user is dragging
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int)
    {
        super.onSelectedChanged(viewHolder, actionState)

        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG)
        {
            viewHolder?.itemView?.setBackgroundColor(Color.parseColor("#42000000"))
        }
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int
    {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END

        return ItemTouchHelper.Callback.makeMovementFlags(
            dragFlags,
            swipeFlags
        )
    }

    //Behaviour on movement of the item
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean
    {
        Log.e("CALLBACK", "I WILL HANDLE MOVEMENT")
        adapter.onItemMove(viewHolder.layoutPosition, target.layoutPosition)
        return true
    }

    //Behaviour on swipe element
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int)
    {
        adapter.onItemDismiss(viewHolder.layoutPosition)
    }

}