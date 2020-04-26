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
import com.example.advanced_chrono2.view.adapters.ActivityHorizontalAdapter
import com.example.advanced_chrono2.view.adapters.ItemMovementHelper
import com.example.advanced_chrono2.view.adapters.SwipableItemsAdapter
import com.example.advanced_chrono2.contract.TimerActivitiesContract
import com.example.advanced_chrono2.model.TimerItemData
import com.example.advanced_chrono2.presenter.TimerActivitiesPresenter
import kotlinx.android.synthetic.main.timer_items_layout.*


//Provvisorio per lista di attività

class TimerActivitiesFragment : Fragment(), TimerActivitiesContract.ITimerActivitiesView
{
    companion object
    {
        private const val TAG = "TIMER FRAGMENT CICLE"
        const val itemLogo = R.drawable.ic_timer_black
    }

    private val timerActivitiesPresenter:
            TimerActivitiesContract.ITimerActivitiesPresenter = TimerActivitiesPresenter(this)

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

    //when creating the view inflating the timer layout
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        return inflater.inflate(R.layout.timer_items_layout, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        timerActivitiesPresenter.onViewCreated()

        //TODO add items to db and to view
        timer_items_button.setOnClickListener {
            showAddTimerItemDialog()
        }
    }



    //INTERFACE FUNCTIONS------------------------------------------------------------------------------------------

    override fun setUpView(activities: List<String>, timerItems: List<TimerItemData>)
    {
        createActivityList(activities)
        createItemList(timerItems)
        setListsMoventBehaviour()
    }

    override fun updateActivitiesView()
    {
        activityItemAdapter.notifyDataSetChanged()
    }

    override fun updateTimerItemsView()
    {
        timerItemAdapter.notifyDataSetChanged()
    }

    //END INTERFACE FUNCTIONS------------------------------------------------------------------------------------------


    //HELPER FUNCTIONS---------------------------------------------------------------------------------------------------
    private fun createActivityList(activities: List<String>)
    {
        this.activityList = activity_recycle

        activityList.setHasFixedSize(true)

        activityList.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        activityItemAdapter = ActivityHorizontalAdapter(activities as ArrayList<String>)
        activityList.adapter = activityItemAdapter

        setActivityListDecoration()
    }

    private fun setActivityListDecoration()
    {
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

    private fun createItemList(timerItems: List<TimerItemData>)
    {
        activityItemList = activity_item_recycler

        activityItemList.setHasFixedSize(true)

        timerItemAdapter = SwipableItemsAdapter(timerItems as ArrayList<TimerItemData>)

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
    }

    private fun setListsMoventBehaviour()
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

    private fun showAddTimerItemDialog()
    {
        val dialogBuilder = AlertDialog.Builder(this.context, R.style.AlertDialogCustom)
        val dialogView = layoutInflater.inflate(R.layout.add_timer_item_layout, null)
        dialogBuilder.setView(dialogView)

        dialogBuilder.setPositiveButton("SAVE ACTIVITY") { _, _ ->
            Toast.makeText(this.context, "ADDED", Toast.LENGTH_LONG).show()


            val workoutSecondsText = dialogView.findViewById<EditText>(R.id.workout_minutes).toString()
            val workoutSeconds: Long =
                workoutSecondsText.toLong() * 60

            val restSecondsText = dialogView.findViewById<EditText>(R.id.workout_minutes).toString()
            val restSeconds: Long =
                restSecondsText.toLong() * 60

            //TODO aggiungere tramite presenter
            timerActivitiesPresenter.addNewTimerItem(workoutSeconds, restSeconds)
        }

        dialogBuilder.setNegativeButton("CANCEL") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        dialogBuilder.show()
    }
    //END HELPER FUNCTIONS---------------------------------------------------------------------------------------------------
}
