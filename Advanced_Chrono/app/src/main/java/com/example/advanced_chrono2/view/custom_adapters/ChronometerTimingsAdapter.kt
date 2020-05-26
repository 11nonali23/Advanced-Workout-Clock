package com.example.advanced_chrono2.view.custom_adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.advanced_chrono2.R

import com.example.advanced_chrono2.contract.HomeChronometerContract.IHomePresenter.Companion.activities //list of the activities
import com.example.advanced_chrono2.contract.HomeChronometerContract.IHomePresenter.Companion.currentSelectedActivity
import com.example.advanced_chrono2.model.ActivityTiming
import com.example.advanced_chrono2.view.custom_views.CustomDialog
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

//Adapter used for the recyclerView in the dialog that contains the timings.

class ChronometerTimingsAdapter(val parent: CustomDialog) : RecyclerView.Adapter<ChronometerTimingsAdapter.ItemViewHolder>(), ItemTouchHelperAdapter
{

    companion object {val SECONDS_INDICATOR = "''"; val MINUTES_INDICATOR = "'"}

    private var currentTimings: ArrayList<ActivityTiming>?
    private lateinit var itemTouchHelper: ItemTouchHelper


    init
    {
        if (activities.size == 0)
            currentTimings = null
        else
            currentTimings = activities[currentSelectedActivity!!].timings_timestamp //if the activities' size > 0 I am sure there is a selected activity
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ItemViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.dialog_item_view ,parent,false)
        // set the view's size, margins, paddings and layout parameters
        return ItemViewHolder(v)
    }

    //TODO get the current locale
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int)
    {
        //Setting the date text with Gregorian Calendar
        holder.dateText.text = SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN).format(currentTimings?.get(position)?.createOn?.time!!)

        //setting the timing. If timing is more than one minute the seconds will be the rest of division by 60 and the minutes will be the division with 60
        if (currentTimings?.get(position)?.timing!! < 6000L)
                holder.timingText.text = (((currentTimings?.get(position)?.timing)?.div(1000)) as Long).toString() + SECONDS_INDICATOR
        else if (currentTimings?.get(position)?.timing!! == 6000L)
            holder.timingText.text = "1$SECONDS_INDICATOR"
        else
        {
            val minutes = ((currentTimings?.get(position)?.timing!!).div(1000))/60
            val seconds = minutes%60
            holder.timingText.text = "$minutes$MINUTES_INDICATOR $seconds$SECONDS_INDICATOR"
        }
    }


    override fun getItemCount(): Int
    {
        if(currentTimings == null)
            return 0
        return currentTimings!!.size
    }

    //called before notifyDataSetChange to update the list of elements
    fun autoUpdateCurrentTimings()
    {
        Log.e("ADAP TIMINGS" ,"updating timings: current selected activity: $currentSelectedActivity")
        if (currentSelectedActivity != null)
            currentTimings = activities.get(currentSelectedActivity!!).timings_timestamp
        else
            currentTimings = null
    }

    fun setItemTouchHelper(itemTouchHelper: ItemTouchHelper) { this.itemTouchHelper = itemTouchHelper}

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val dateText: TextView = itemView.findViewById(R.id.daetText)
        val timingText: TextView = itemView.findViewById(R.id.timingText)
    }

    //I don't want to move items becuse they are sorted by date
    override fun onItemMove(fromPosition: Int, toPosition: Int) {}


    override fun onItemDismiss(position: Int)
    {
        parent.removeTiming(position)
    }
}
