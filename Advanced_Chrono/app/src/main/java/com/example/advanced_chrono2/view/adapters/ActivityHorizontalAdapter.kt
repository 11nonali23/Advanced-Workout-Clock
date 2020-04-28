package com.example.advanced_chrono2.view.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.advanced_chrono2.R
import com.example.advanced_chrono2.TimerActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton


//This adapter is used to control the horizontal activities of the timer

class ActivityHorizontalAdapter (private val itemList: ArrayList<String>) : RecyclerView.Adapter<ActivityHorizontalAdapter.HorizontalItemsViewHolder>()
{
    /*Only one ViewHolder card can be selected. Done due to the proper work of the animation that lift the selected card
    By default the selected card is the an at 0 postion*/
    //TODO I can add in the arraylist only two old item selected
    private lateinit var selectedCard: CardView //init in OnBindViewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalItemsViewHolder
    {
        var v = LayoutInflater.from(parent.context).inflate(R.layout.timer_activity_layout,parent,false)

        return HorizontalItemsViewHolder(v)
    }

    override fun getItemCount(): Int
    {
        return itemList.size
    }

    override fun onBindViewHolder(holder: HorizontalItemsViewHolder, position: Int)
    {
        holder.textView.text = this.itemList[position]

        //By default the selected card is the an at 0 postion
        if (position == 0)
            this.selectedCard = holder.card
    }

    inner class HorizontalItemsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener
    {
        var textView: TextView
        private val fab = itemView.findViewById<FloatingActionButton>(R.id.start_timerActivity_button)
        val card = itemView.findViewById<CardView>(R.id.activityCard)

        init
        {
            this.textView = itemView.findViewById(R.id.text_activity)
            itemView.setOnClickListener(this)
            fab.setOnClickListener(this)
            card.setOnClickListener(this)
        }

        //if the start button is clicked i need to start a new activity
        /*The ID is the same for every card. To affect only one card I have to use equals operator*/
        override fun onClick(v: View?)
        {
            //TODO implement an explicit intent for timer activity passing the data of the current position item
            if (v?.id == fab.id)
            {
                val intent = Intent(v.context, TimerActivity::class.java)
                v.context.startActivity(intent)
            }
            else if (v?.id == card.id)
            {
                if (v != selectedCard)
                {
                    selectedCard.isSelected = false
                    selectedCard = v as CardView
                    selectedCard.isSelected = true
                }
                else
                    selectedCard.isSelected = !selectedCard.isSelected
            }
        }
    }
}