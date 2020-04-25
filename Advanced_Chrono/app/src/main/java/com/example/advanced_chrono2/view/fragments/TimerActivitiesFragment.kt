package com.example.advanced_chrono2.view.fragments

import android.app.AlertDialog
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.advanced_chrono2.R
import com.example.advanced_chrono2.adapters.ActivityHorizontalAdapter
import com.example.advanced_chrono2.adapters.ItemMovementHelper
import com.example.advanced_chrono2.adapters.SwipableItemsAdapter
import com.example.advanced_chrono2.model.TimerActivityData
import com.example.advanced_chrono2.model.TimerItemData
import kotlinx.android.synthetic.main.add_timer_item_layout.*
import kotlinx.android.synthetic.main.timer_items_layout.*


//Provvisorio per lista di attività

class TimerActivitiesFragment : Fragment()
{
    companion object
    {private const val TAG = "TIMER FRAGMENT CICLE"; private const val itemLogo = R.drawable.ic_timer_black }

    private val timerItems = ArrayList<TimerItemData>()
    private val activityItems =  ArrayList<TimerActivityData>()

    private lateinit var activityItemList: RecyclerView
    private lateinit var activityList: RecyclerView //NEW <=======

    private lateinit var timerItemAdapter: SwipableItemsAdapter
    private lateinit var activityItemAdapter: ActivityHorizontalAdapter

    private lateinit var itemTouchHelper: ItemTouchHelper
    private lateinit var itemHelperOnSwipe: ItemTouchHelper.SimpleCallback

    //This method shows when a new fragment is created
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "FRAGMENT TIMER CREATED")
    }

    //when creating the view inflating the chronometer layout
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        return inflater.inflate(R.layout.timer_items_layout, container, false)
    }

    //here I will init all my components
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        buildRecyclerView()

        createItemList()
        createActivityList()



        //TODO add items to db and to view
        timer_items_button.setOnClickListener {view

            val dialogBuilder = AlertDialog.Builder(this.context, R.style.AlertDialogCustom)
            val dialogView = layoutInflater.inflate(R.layout.add_timer_item_layout, null)
            dialogBuilder.setView(dialogView)

            dialogBuilder.setPositiveButton("SAVE ACTIVITY") { _, _ ->
                Toast.makeText(this.context, "ADDED", Toast.LENGTH_LONG).show()


                val workoutSecondsText = dialogView.findViewById<EditText>(R.id.workout_minutes).toString()
                val workoutSeconds: Long =
                    workoutSecondsText.toLong() * 60

                val restSeconds: Long =
                    rest_minutes.text.toString().toLong() * 60 + rest_seconds.text.toString().toLong()

                //TODO aggiungere anche al database
                val newItem = TimerItemData(itemLogo, workoutSeconds, restSeconds)
                timerItems.add(newItem)
                timerItemAdapter.notifyDataSetChanged()
            }

            dialogBuilder.setNegativeButton("CANCEL") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            dialogBuilder.show()
        }
    }

    //FUNZIONI PROVVISORIE

    private fun buildRecyclerView()
    {
        timerItems.add(TimerItemData(itemLogo, 20, 5))
        timerItems.add(TimerItemData(itemLogo, 30, 15))
        timerItems.add(TimerItemData(itemLogo, 40, 25))
        timerItems.add(TimerItemData(itemLogo, 50, 35))
        timerItems.add(TimerItemData(itemLogo, 60, 45))
        timerItems.add(TimerItemData(itemLogo, 70, 55))


        activityItems.add(TimerActivityData("Activity 1"))
        activityItems.add(TimerActivityData("Activity 2"))
        activityItems.add(TimerActivityData("Activity 3"))
        activityItems.add(TimerActivityData("Activity 4"))
        activityItems.add(TimerActivityData("Activity 5"))

    }

    private fun createItemList()
    {
        activityItemList = activity_item_recycler

        activityItemList.setHasFixedSize(true)

        timerItemAdapter = SwipableItemsAdapter(timerItems)

        activityItemList.layoutManager = LinearLayoutManager(this.context)
        activityItemList.adapter = timerItemAdapter

        //NEW
        activityItemList.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.set(20, 0,20,0)
            }
        })

        setActivityListMoventBehaviour()
    }

    private fun setActivityListMoventBehaviour()
    {
        //itemMovementHelper defines behaviour of recyclerView on user inputs
        itemHelperOnSwipe =
            ItemMovementHelper(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
                timerItemAdapter
            )

        itemTouchHelper = ItemTouchHelper(itemHelperOnSwipe)

        timerItemAdapter.setTouchHelper(itemTouchHelper)

        itemTouchHelper.attachToRecyclerView(activityItemList)
    }

    private fun createActivityList()
    {
        activityList = activity_recycle

        activityList.setHasFixedSize(true)

        activityList.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        activityItemAdapter = ActivityHorizontalAdapter(activityItems)
        activityList.adapter = activityItemAdapter

        //setting a dynamic center for horizontal activity
        activityList.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val cardHeight = 200
                val containerHeight = resources.getDimensionPixelOffset(R.dimen.max_container_height)
                var topBottomPadding = (containerHeight/2 - cardHeight)/2
                outRect.set(10, topBottomPadding,10,topBottomPadding)
            }
        })
    }
}
