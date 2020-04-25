package com.example.advanced_chrono2.adapters


import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.advanced_chrono2.R
import com.example.advanced_chrono2.model.TimerItemData
import java.util.*

//This adapter is used to manage the items of the activities taht can be swiped to be deleted and also moved around them

//Interface for behaviour on item swiped and dragged
interface ItemTouchHelperAdapter {
    fun onItemMove(fromPosition: Int, toPosition: Int)
    fun onItemDismiss(position: Int)
}

class SwipableItemsAdapter() : Adapter<SwipableItemsAdapter.SwipableItemsViewHolder>(), ItemTouchHelperAdapter
{
    private lateinit var timerList: ArrayList<TimerItemData>
    private lateinit var itemTouchHelper: ItemTouchHelper      //the item touch helper will be itemMovementHelper

    constructor(exampleList: ArrayList<TimerItemData>): this()
    {
        this.timerList = exampleList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwipableItemsViewHolder
    {
        var v = LayoutInflater.from(parent.context).inflate(R.layout.timer_item_layout ,parent,false)
        return SwipableItemsViewHolder(v)
    }

    override fun getItemCount(): Int
    {
        return timerList.size
    }

    override fun onBindViewHolder(holder: SwipableItemsViewHolder, position: Int)
    {
        var curr = timerList.get(position)

        holder.imageView.setImageResource(curr.imageResource)

        holder.workoutTextView.text = "workout: ${curr.workoutSeconds}"
        holder.restTextView.text = "rest: ${curr.restSeconds}"
    }


    fun setTouchHelper(itemTouchHelper: ItemTouchHelper)
    {
        this.itemTouchHelper = itemTouchHelper
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int)
    {
        if (fromPosition < toPosition)
            for (i in fromPosition until toPosition)
                Collections.swap(timerList, i, i + 1)
        else
            for (i in fromPosition downTo toPosition + 1)
                Collections.swap(timerList, i, i - 1)

        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int)
    {
        timerList.removeAt(position)
        notifyItemRemoved(position)
    }


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
            return true
        }
    }
}