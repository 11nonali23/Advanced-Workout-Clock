package com.example.advanced_chrono2.view.fragments

import android.app.AlertDialog
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.advanced_chrono2.R
import com.example.advanced_chrono2.view.custom_adapters.TimerActivitiesAdapter
import com.example.advanced_chrono2.view.custom_adapters.SwipeDragCallbackHelper
import com.example.advanced_chrono2.contract.TimerActivitiesContract
import com.example.advanced_chrono2.model.TimerActivityData
import com.example.advanced_chrono2.model.TimerItemData
import com.example.advanced_chrono2.presenter.TimerActivitiesPresenter
import com.example.advanced_chrono2.view.custom_adapters.TimerItemsAdapter
import kotlinx.android.synthetic.main.timer_personalize_activities_layout.*


//Provvisorio per lista di attività

class TimerActivitiesFragment : Fragment(), TimerActivitiesContract.ITimerActivitiesView
{
    companion object
    {
        private const val TAG = "TIMER FRAGMENT CICLE"
    }

    private val timerActivitiesPresenter:
            TimerActivitiesContract.ITimerActivitiesPresenter = TimerActivitiesPresenter(this)

    private lateinit var activityItemList: RecyclerView
    private lateinit var activityList: RecyclerView

    private lateinit var timerItemAdapter: TimerItemsAdapter
    private lateinit var activityItemAdapter: TimerActivitiesAdapter

    private lateinit var itemTouchHelper: ItemTouchHelper
    private lateinit var swipeDragCallbackHelper: SwipeDragCallbackHelper

    //This method shows when a new fragment is created
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "FRAGMENT TIMER CREATED")
        setHasOptionsMenu(true)
    }

    //Setting the options menu
    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater)
    {
        menuInflater.inflate(R.menu.timer_activities_menu, menu)
    }

    //when creating the view inflating the timer layout
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        return inflater.inflate(R.layout.timer_personalize_activities_layout, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        timerActivitiesPresenter.onViewCreated()

        timer_items_button.setOnClickListener {
            showAddTimerItemDialog()
        }
    }



    //INTERFACE FUNCTIONS------------------------------------------------------------------------------------------

    override fun setUpView(activities: List<TimerActivityData>)
    {
        createActivityList(activities)

        //TODO make correct for every position
        //the activity list passes her list retrived from model via the presenter to the adapter.
        createItemList(activities[0].timerItems)

        setTimerItemsListMovementBehaviour()

    }

    override fun updateActivitiesView()
    {
        activityItemAdapter.notifyDataSetChanged()
    }

    override fun updateTimerItemsView()
    {
        timerItemAdapter.notifyDataSetChanged()
    }

    override fun displayResult(message: String) = Toast.makeText(this.context, message, Toast.LENGTH_LONG).show()

    //END INTERFACE FUNCTIONS------------------------------------------------------------------------------------------


    //HELPER FUNCTIONS---------------------------------------------------------------------------------------------------
    private fun createActivityList(activities: List<TimerActivityData>)
    {
        this.activityList = activity_recycle

        activityList.setHasFixedSize(true)

        activityList.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        activityItemAdapter = TimerActivitiesAdapter(activities as ArrayList<TimerActivityData>)
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


    //Create the list of items view connected to an activity
    private fun createItemList(timerItems: List<TimerItemData>)
    {
        activityItemList = activity_item_recycler

        activityItemList.setHasFixedSize(true)

        timerItemAdapter = TimerItemsAdapter(timerItems as ArrayList<TimerItemData>)

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

    private fun setTimerItemsListMovementBehaviour()
    {
        //itemMovementHelper defines behaviour of recyclerView on user inputs
        swipeDragCallbackHelper =
            SwipeDragCallbackHelper(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
                timerItemAdapter
            )

        itemTouchHelper = ItemTouchHelper(swipeDragCallbackHelper)

        //Set the touch helper after beacuse the swipeDragCallbackHelper needs to know the adpater
        timerItemAdapter.setItemTouchHelper(itemTouchHelper)

        itemTouchHelper.attachToRecyclerView(activityItemList)
    }


    private fun showAddTimerItemDialog()
    {
        val dialogBuilder = AlertDialog.Builder(this.context, R.style.AlertDialogCustom)
        val dialogView = layoutInflater.inflate(R.layout.add_timer_item_layout, null)
        dialogBuilder.setView(dialogView)

        dialogBuilder.setPositiveButton("SAVE ACTIVITY") { _, _ ->
            Toast.makeText(this.context, "ADDED", Toast.LENGTH_LONG).show()

            val workoutMinutesText = dialogView.findViewById<EditText>(R.id.workout_minutes).text.toString()
            val workoutSecondsText = dialogView.findViewById<EditText>(R.id.workout_seconds).text.toString()
            val restMinutesText = dialogView.findViewById<EditText>(R.id.rest_minutes).text.toString()
            val restSecondsText = dialogView.findViewById<EditText>(R.id.rest_seconds).text.toString()


            //TODO aggiungere tramite presenter
            timerActivitiesPresenter.addNewTimerItem(workoutMinutesText, workoutSecondsText, restMinutesText, restSecondsText)
        }

        dialogBuilder.setNegativeButton("CANCEL") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        dialogBuilder.show()
    }
    //END HELPER FUNCTIONS---------------------------------------------------------------------------------------------------
}
