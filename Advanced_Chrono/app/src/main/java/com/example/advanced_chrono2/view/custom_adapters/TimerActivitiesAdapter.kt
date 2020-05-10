package com.example.advanced_chrono2.view.custom_adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.advanced_chrono2.IntervalTimerActivity
import com.example.advanced_chrono2.R
import com.example.advanced_chrono2.view.fragments.TimerActivitiesFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton


//This adapter is used to control the horizontal activities of the timer

//This adapter is responsible of the control of the current selected activity

/*The list itemList field is passed as reference from the TimerFragment to the model it self and so,
when a change is done to the timer items in the activity also this list will change
 */

//TODO when I selected an activity I have to change the list of items to the one related to the selected one


//The field activitiesList of the companion object of ITimerActivitiesPresenter wil be renamed as presenterList
import com.example.advanced_chrono2.contract.TimerActivitiesContract.ITimerActivitiesPresenter.Companion.activitiesList as presenterList

class TimerActivitiesAdapter (private val parentView: TimerActivitiesFragment
) : RecyclerView.Adapter<TimerActivitiesAdapter.ActivityViewHolder>()

{

    /*Only one ViewHolder card can be selected. Done due to the proper work of the animation that lift the selected card*/
    //TODO When i will implement the delete I need to change this field to a default value if the selected card is deleted
    private var selectedCard: CardView? = null  //null when no card is selected

    private var selectedActivityPosition: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder
    {
        var v = LayoutInflater.from(parent.context).inflate(R.layout.timer_activity_layout,parent,false)

        return ActivityViewHolder(v)
    }

    override fun getItemCount(): Int
    {
        return presenterList.size
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int)
    {
        holder.textView.text = presenterList[position].name

        //TODO activity does not go up!!!
        /*By default the selected card is the an at 0 postion
        if (position == DEFAULT_ITEM_POS)
        {
            this.selectedCard = holder.card
            this.selectedItemPosition = DEFAULT_ITEM_POS
        }*/
    }

    fun getSelectedActivityPosition(): Int?
    {
        return if (selectedActivityPosition != null)
            selectedActivityPosition
        else
            null
    }


    inner class ActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener
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

        /*The ID is the same for every card and button. To affect only one card I have to use equals operator*/
        override fun onClick(v: View?)
        {
            //if the start fab button is clicked i need to start a new activity
            if (v?.id == fab.id)
            {
                //explicit intent for timer activity passing the data of the current position item
                val intent = Intent(v.context, IntervalTimerActivity::class.java)

                val activityInfo = parentView.timerActivitiesPresenter.onActivityStart(absoluteAdapterPosition)
                if(activityInfo != null)
                {
                    intent.putExtra("ACTIVITY_NAME", activityInfo.first)
                    intent.putExtra("TIMER_ITEMS", activityInfo.second)
                    v.context.startActivity(intent)
                }
            }

            //check if the view selected is the card itself
            else if (v?.id == card.id)
            {
                //If there is another selected card
                if (selectedCard != null)
                {
                    if (v != selectedCard)
                    {
                        selectedCard?.isSelected = false
                        selectedCard = v as CardView
                        selectedCard?.isSelected = true
                        //Setting the new selected item position
                        selectedActivityPosition = absoluteAdapterPosition
                    } else
                    {
                        //if i selected a card already selected no one will be selected
                        selectedCard!!.isSelected = !selectedCard!!.isSelected      //setting the current card as not selected anymore
                        selectedCard = null                                         //setting the current selected card as none
                        selectedActivityPosition = null                                 //setting the current selected position as none

                    }
                }
                //if selected card is null I have to initialize it with the one selected
                else
                {
                    selectedCard = v as CardView
                    selectedCard!!.isSelected = true
                    //Setting the new selected item position
                    selectedActivityPosition = absoluteAdapterPosition
                }
                //update the item list
                parentView.timerActivitiesPresenter.onSelectedActivityChange(selectedActivityPosition)
            }
        }
    }
}