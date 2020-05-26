package com.example.advanced_chrono2.view.custom_adapters

import com.example.advanced_chrono2.view.fragments.TimerActivitiesFragment
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.advanced_chrono2.R
import java.util.*


//The field activitiesList of the companion object of ITimerActivitiesPresenter wil be renamed as presenterList
import com.example.advanced_chrono2.contract.TimerActivitiesContract.ITimerActivitiesPresenter.Companion.activitiesList as presenterList

/*This adapter is used to manage the items of the activities.
 They can be swiped to be deleted and also dragged into another position*/


class TimerItemsAdapter(private val parent: TimerActivitiesFragment,
                        private var currentActivityPosition: Int?
) : Adapter<TimerItemsAdapter.ItemsViewHolder>(), ItemTouchHelperAdapter

{

    companion object
    {
        private const val MIN_SYMBOL = "\'"
        private const val SEC_SYMBOL = "\'\'"
    }

    private lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder
    {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.timer_item_layout ,parent,false)
        return ItemsViewHolder(v)
    }

    override fun getItemCount(): Int
    {
        return if (currentActivityPosition != null)
            presenterList[currentActivityPosition!!].timerItems.size
        else
            0
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int)
    {
        if (currentActivityPosition != null)
        {
            val curr = presenterList[currentActivityPosition!!].timerItems[position]

            Log.e(
                "TIMER ITEM ADAP",
                "size of list ${presenterList[currentActivityPosition!!].timerItems.size}"
            )

            Log.e(
                "TIMER ITEM ADAP",
                "item at $position : id = ${curr.id}         work = ${curr.workoutSeconds}     rest = ${curr.restSeconds}"
            )


            holder.imageView.setImageResource(curr.imageResource)

            //setting timer item view text with minutes (if seconds > 60) and seconds
            //if seconds > 60: minutes = seconds/60, seconds = seconds%60
            //MINUTES
            if (curr.workoutSeconds > 60)
                holder.workoutTextView.text =
                    parent.context?.getString(R.string.WORKOUT_MINUTE_AND_SECS, "${curr.workoutSeconds / 60}$MIN_SYMBOL", "${curr.workoutSeconds % 60}$SEC_SYMBOL")

            else if (curr.workoutSeconds == 60)
                holder.workoutTextView.text = parent.context?.getString(R.string.WORKOUT_ONE_MINUTE, MIN_SYMBOL)
            else
                holder.workoutTextView.text = parent.context?.getString(R.string.WORKOUT_SEC_ONLY,"${curr.workoutSeconds}$SEC_SYMBOL")

            //SECONDS
            if (curr.restSeconds >= 60)
                holder.restTextView.text =
                    parent.context?.getString(R.string.REST_MINUTE_AND_SECS, "${curr.restSeconds / 60}$MIN_SYMBOL", "${curr.restSeconds % 60}$SEC_SYMBOL")
            else if (curr.restSeconds == 60)
                holder.restTextView.text = parent.context?.getString(R.string.REST_ONE_MINUTE, MIN_SYMBOL)
            else
                holder.restTextView.text = parent.context?.getString(R.string.REST_SEC_ONLY,"${curr.restSeconds}$SEC_SYMBOL")
        }
    }


    //GETTER AND SETTER
    //This method is used to tell the Adapter which item touch helper is attached to him.
    //I can' t make this a field beacuse the touch helper needs the adapter to call his metods
    fun setItemTouchHelper(itemTouchHelper: ItemTouchHelper)
    {
        this.itemTouchHelper = itemTouchHelper
    }

    fun changeCurrentActivityPosition(position: Int?) {currentActivityPosition = position}


    //INTERFACE FUNCTIONS-----------------------------------------------------------------------------------------------

    //when the user swaps to items, since the list is the one passed by reference from the presenter, I don't need to advise the presenter directly! ==>TODO it' wrong!!!!!
    override fun onItemMove(fromPosition: Int, toPosition: Int)
    {
        Log.e("TIMER ITEM ADAPTER", "from : $fromPosition , to: $toPosition")

        //If an item is moved it is impossible that no items are showing
        Collections.swap(presenterList[currentActivityPosition!!].timerItems, fromPosition, toPosition)

        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int)
    {
        //If an item is deleted it is impossible that no items are showing
        parent.timerActivitiesPresenter.deleteItem(currentActivityPosition, position)
    }
    //END INTERFACE FUNCTIONS-----------------------------------------------------------------------------------------------

    inner class ItemsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnTouchListener,GestureDetector.OnGestureListener
    {
        var imageView: ImageView = itemView.findViewById(R.id.timer_item_image)
        var workoutTextView: TextView = itemView.findViewById(R.id.timer_item_text)
        var restTextView: TextView
        private var mGestureDetector: GestureDetector? = null

        init
        {
            this.restTextView = itemView.findViewById(R.id.timer_item_text_rest)
            this.mGestureDetector = GestureDetector(itemView.context, this)
            itemView.setOnTouchListener(this)

        }

        override fun onShowPress(e: MotionEvent?)
        {
            //do nothing
        }

        override fun onSingleTapUp(e: MotionEvent?): Boolean
        {
            return true
        }

        override fun onDown(e: MotionEvent?): Boolean
        {
            return false
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean
        {
            return false
        }

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean
        {
            return true
        }

        //if user long press an item the item touch helper should start dragging that
        override fun onLongPress(e: MotionEvent?)
        {
            itemTouchHelper.startDrag(this)
        }

        override fun onTouch(v: View?, event: MotionEvent?): Boolean
        {
            mGestureDetector?.onTouchEvent(event)
            v?.performClick()
            return true
        }
    }
}
