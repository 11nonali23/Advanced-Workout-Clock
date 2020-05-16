package com.example.advanced_chrono2.view.custom_adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.advanced_chrono2.R

class DialogViewAdapter(private val timings: ArrayList<String>) : RecyclerView.Adapter<DialogViewAdapter.ItemViewHolder>()
{

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ItemViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.dialog_item_view ,parent,false)
        // set the view's size, margins, paddings and layout parameters
        return ItemViewHolder(v)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        Log.e("adapter dialog", "bind view holder")
        holder.textView.text = timings[position]
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        Log.e("adap dialog", "timing size ${timings.size}")
        return timings.size
    }


    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val textView: TextView = itemView.findViewById(R.id.timingText)
    }
}
