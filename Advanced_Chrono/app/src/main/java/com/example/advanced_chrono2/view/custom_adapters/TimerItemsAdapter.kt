package com.example.advanced_chrono2.view.custom_adapters


import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.advanced_chrono2.R
import com.example.advanced_chrono2.model.TimerItem
import com.example.advanced_chrono2.view.fragments.TimerActivitiesFragment
import java.util.*
import kotlin.collections.ArrayList

/*This adapter is used to manage the items of the activities.
 They can be swiped to be deleted and also dragged into another position*/

//Interface for behaviour on item swiped and dragged
interface ItemTouchHelperAdapter
{
    fun onItemMove(fromPosition: Int, toPosition: Int)
    fun onItemDismiss(position: Int)
}

class TimerItemsAdapter(private val parent: TimerActivitiesFragment,
                        private val itemList: ArrayList<TimerItem>)

    : Adapter<TimerItemsAdapter.SwipableItemsViewHolder>(), ItemTouchHelperAdapter

{

    companion object
    {
        private const val MIN_SYMBOL = "\'"
        private const val SEC_SYMBOL = "\'\'"
    }

    private lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwipableItemsViewHolder
    {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.timer_item_layout ,parent,false)
        return SwipableItemsViewHolder(v)
    }

    override fun getItemCount(): Int
    {
        return itemList.size
    }

    override fun onBindViewHolder(holder: SwipableItemsViewHolder, position: Int)
    {
        val curr = itemList.get(position)

        holder.imageView.setImageResource(curr.imageResource)

        //setting timer item view text with minutes (if seconds > 60) and seconds
        //if seconds > 60: minutes = seconds/60, seconds = seconds%60
        //MINUTES
        if(curr.workoutSeconds > 60)
            holder.workoutTextView.text = "work ${curr.workoutSeconds/60}$MIN_SYMBOL ${curr.workoutSeconds%60}$SEC_SYMBOL"
        else if (curr.workoutSeconds == 60)
            holder.workoutTextView.text = "work 1$MIN_SYMBOL"
        else
            holder.workoutTextView.text = "work ${curr.workoutSeconds}$SEC_SYMBOL"

        //SECONDS
        if(curr.restSeconds >= 60)
            holder.restTextView.text = "rest ${curr.restSeconds/60}$MIN_SYMBOL ${curr.restSeconds%60}$SEC_SYMBOL"
        else if (curr.restSeconds == 60)
            holder.restTextView.text = "rest 1$MIN_SYMBOL"
        else
            holder.restTextView.text = "rest ${curr.restSeconds}$SEC_SYMBOL"
    }


    //GETTER AND SETTER
    //This method is used to tell the Adapter which item touch helper is attached to him.
    //I can' t make this a field beacuse the touch helper needs the adapter to call his metods
    fun setItemTouchHelper(itemTouchHelper: ItemTouchHelper)
    {
        this.itemTouchHelper = itemTouchHelper
    }


    //INTERFACE FUNCTIONS-----------------------------------------------------------------------------------------------

    //when the user swaps to items, since the list is the one passed by reference from the presenter, I don't need to advise the presenter directly!
    override fun onItemMove(fromPosition: Int, toPosition: Int)
    {
        Log.e("TIMER ITEM ADAPTER", "from : $fromPosition , to: $toPosition")

        Collections.swap(this.itemList, fromPosition, toPosition)

        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int)
    {
        parent.informPresenterItemDismissed(position)
    }
    //END INTERFACE FUNCTIONS-----------------------------------------------------------------------------------------------

    inner class SwipableItemsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnTouchListener,GestureDetector.OnGestureListener
    {
        var imageView: ImageView
        var workoutTextView: TextView
        var restTextView: TextView
        var mGestureDetector: GestureDetector? = null

        init
        {
            this.imageView = itemView.findViewById(R.id.timer_item_image)
            this.workoutTextView = itemView.findViewById(R.id.timer_item_text)
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
