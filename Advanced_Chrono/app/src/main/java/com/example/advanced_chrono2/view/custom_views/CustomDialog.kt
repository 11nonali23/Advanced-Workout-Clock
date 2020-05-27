package com.example.advanced_chrono2.view.custom_views

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.advanced_chrono2.R
import com.example.advanced_chrono2.view.custom_adapters.ChronometerTimingsAdapter
import com.example.advanced_chrono2.view.custom_adapters.OnlySwipeHelper
import com.example.advanced_chrono2.view.fragments.ChronometerFragment


class CustomDialog(val parent: ChronometerFragment) : Dialog(parent.context!!)
{
    private lateinit var timingsList: RecyclerView
    private lateinit var viewAdapter: ChronometerTimingsAdapter
    //properties for swipe
    private lateinit var onlySwipeHelper: OnlySwipeHelper
    private lateinit var itemTouchHelper: ItemTouchHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.custom_dialog_layout)
        setCanceledOnTouchOutside(true)
        //setting rounded background
        window?.setBackgroundDrawableResource(R.drawable.rounded_dialog)

        //setting transparent background
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        viewAdapter = ChronometerTimingsAdapter(this)

        timingsList = findViewById(R.id.itemsList)
        timingsList.setHasFixedSize(true)
        timingsList.adapter = viewAdapter
        timingsList.layoutManager = LinearLayoutManager(this.context)

        //TODO set to match parent also height but make setCanceledOnTouchOutside work
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        //setting the dialog to match parent. Margins wil be set up with inset
        /*margins and color of the dialog*/
        val back = ColorDrawable(Color.WHITE)
        val inset = InsetDrawable(back, 120)
        window?.setBackgroundDrawable(inset)

        //itemMovementHelper defines behaviour of recyclerView on user inputs
        onlySwipeHelper =
            OnlySwipeHelper(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
                viewAdapter
            )

        itemTouchHelper = ItemTouchHelper(onlySwipeHelper)

        //Set the touch helper after beacuse the swipeDragCallbackHelper needs to know the adpater
        viewAdapter.setItemTouchHelper(itemTouchHelper)

        itemTouchHelper.attachToRecyclerView(timingsList)


    }

    fun updateAdapter()
    {
        Log.e("dialog", "received update request")
        viewAdapter.autoUpdateCurrentTimings()
        viewAdapter.notifyDataSetChanged()
    }

    fun notifyAdapterItemRemoved(itemPosition: Int) { viewAdapter.notifyItemRemoved(itemPosition)}

    fun removeTiming(timingPosition: Int) {parent.deleteTiming(timingPosition)}
}
