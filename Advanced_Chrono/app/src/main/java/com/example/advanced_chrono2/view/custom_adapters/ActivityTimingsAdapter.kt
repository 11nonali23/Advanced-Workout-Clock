package com.example.advanced_chrono2.view.custom_adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.advanced_chrono2.R

import com.example.advanced_chrono2.contract.HomeChronometerContract.IHomePresenter.Companion.activities //list of the activities
import com.example.advanced_chrono2.contract.HomeChronometerContract.IHomePresenter.Companion.currentSelectedActivity

//Adapter used for the recyclerView in the dialog that contains the timings.

class ActivityTimingsAdapter : RecyclerView.Adapter<ActivityTimingsAdapter.ItemViewHolder>()
{
    private var currentTimings: ArrayList<Pair<Long, Int>>?

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

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.textView.text = "${currentTimings?.get(position)?.second} ===> ${currentTimings?.get(position)?.first}"
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

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val textView: TextView = itemView.findViewById(R.id.timingText)
    }
}
