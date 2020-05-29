package com.andrea.advanced_workout_clock.view.fragments

import android.app.AlertDialog
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andrea.advanced_workout_clock.R
import com.andrea.advanced_workout_clock.view.custom_adapters.TimerActivitiesAdapter
import com.andrea.advanced_workout_clock.view.custom_adapters.SwipeDragCallbackHelper
import com.andrea.advanced_workout_clock.contract.TimerActivitiesContract
import com.andrea.advanced_workout_clock.presenter.TimerActivitiesPresenter
import com.andrea.advanced_workout_clock.view.custom_adapters.TimerItemsAdapter
import kotlinx.android.synthetic.main.timer_personalize_activities_layout.*

class TimerActivitiesFragment : Fragment(), TimerActivitiesContract.ITimerActivitiesView
{
    companion object
    {
        private const val TAG = "TIMER FRAGMENT CYCLE"
    }

    val timerActivitiesPresenter:
            TimerActivitiesContract.ITimerActivitiesPresenter = TimerActivitiesPresenter(this)

    private var settedUp = false

    private lateinit var activityItemList: RecyclerView
    private lateinit var activityList: RecyclerView

    private lateinit var activityItemAdapter: TimerActivitiesAdapter // this presenter handles the activity list.
    private lateinit var timerItemAdapter: TimerItemsAdapter         // this presenter hanldes the item list related to an activity

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        when(item.itemId)
        {
            R.id.addItem ->
            {
                showAddActivityDialogBuilder()
                return true
            }

            R.id.delItem ->
            {
                showDeleteActivityDialogBuilder()
                return true
            }
        }
        return false
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

        timerActivitiesPresenter.onViewCreated(this.lendContext())

        timer_items_button.setOnClickListener {
            showAddTimerItemDialog()
        }
    }

    //LISTENERS-----------------------------------------------------------------------------------------------------------------------------------
    override fun onResume()
    {
        super.onResume()
        if (context != null)
            activity?.window?.navigationBarColor = ContextCompat.getColor(this.context!!, R.color.lightPrimary)
    }

    //INTERFACE FUNCTIONS------------------------------------------------------------------------------------------

    override fun setUpView()
    {
        createActivityList()

        createItemList()

        setTimerItemListMovementBehaviour()

        //hiding the add item button because at startup no activity is selected
        setAddItemButtonVisible(false)

        this.settedUp = true

    }

    override fun isViewSetUp(): Boolean = this.settedUp

    override fun activitiesDataSetChanged()
    {
        activityItemAdapter.notifyDataSetChanged()
    }

    override fun activityRemovedFromDataSet(position: Int)
    {
        activityItemAdapter.notifyItemRemoved(position)
    }

    override fun itemDataSetChanged()
    {
        timerItemAdapter.notifyDataSetChanged()
    }

    override fun itemRemovedFromDataSet(position: Int) {
        activity_item_recycler.removeViewAt(position)
        timerItemAdapter.notifyItemRemoved(position)
    }

    override fun changeTimerItemListView(position: Int?)
    {
        this.timerItemAdapter.changeCurrentActivityPosition(position)
    }

    override fun displayResult(message: String) = Toast.makeText(this.lendContext(), message, Toast.LENGTH_LONG).show()

    override fun lendContext(): Context? {return context}

    //END INTERFACE FUNCTIONS------------------------------------------------------------------------------------------

    fun setAddItemButtonVisible (setVisible: Boolean)
    {
        if (setVisible)
            timer_items_button.visibility = View.VISIBLE
        else
            timer_items_button.visibility = View.INVISIBLE
    }


    //HELPER FUNCTIONS---------------------------------------------------------------------------------------------------
    private fun createActivityList()
    {
        this.activityList = activity_recycle

        activityList.setHasFixedSize(true)

        activityList.layoutManager = LinearLayoutManager(this.lendContext(), LinearLayoutManager.HORIZONTAL, false)

        activityItemAdapter = TimerActivitiesAdapter(this)
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
                val topBottomPadding = (containerHeight/2 - cardHeight)/2
                outRect.set(10, topBottomPadding,10,topBottomPadding)
            }
        })
    }


    //Create the list of items view connected to an activity
    private fun createItemList()
    {
        activityItemList = activity_item_recycler

        //TODO mak a better showing position
        timerItemAdapter = TimerItemsAdapter(this,null)

        activityItemList.layoutManager = LinearLayoutManager(this.lendContext())
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

    private fun setTimerItemListMovementBehaviour()
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
        val dialogBuilder = AlertDialog.Builder(this.lendContext(), R.style.AlertDialogCustom)
        val dialogView = layoutInflater.inflate(R.layout.add_timer_item_layout, null)
        dialogBuilder.setView(dialogView)

        dialogBuilder.setPositiveButton(context?.getString(R.string.ADD_ITEM_CONFIRM)) { _, _ ->
            val workoutMinutesText = dialogView.findViewById<EditText>(R.id.workout_minutes).text.toString()
            val workoutSecondsText = dialogView.findViewById<EditText>(R.id.workout_seconds).text.toString()
            val restMinutesText = dialogView.findViewById<EditText>(R.id.rest_minutes).text.toString()
            val restSecondsText = dialogView.findViewById<EditText>(R.id.rest_seconds).text.toString()

            val id = (activity_recycle.adapter as TimerActivitiesAdapter).getSelectedActivityPosition()
            val times = dialogView.findViewById<NumberPicker>(R.id.times_picker).value

            for (i in 1.. times)
                timerActivitiesPresenter.addNewTimerItem(id, workoutMinutesText, workoutSecondsText, restMinutesText, restSecondsText)
        }

        dialogBuilder.setNegativeButton(context?.getString(R.string.DISMISS_DIALOG)) { dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        dialogView.findViewById<NumberPicker>(R.id.times_picker).minValue = 1
        dialogView.findViewById<NumberPicker>(R.id.times_picker).maxValue = 30
        dialogView.findViewById<NumberPicker>(R.id.times_picker).wrapSelectorWheel = true

        dialogBuilder.show()
    }

    private fun showAddActivityDialogBuilder()
    {
        val dialogBuilder = AlertDialog.Builder(this.lendContext(), R.style.AlertDialogCustom)
        val dialogView = layoutInflater.inflate(R.layout.add_activity_layout, null)
        dialogBuilder.setView(dialogView)

        dialogBuilder.setPositiveButton(context?.getString(R.string.ADD_ACTIVITY_CONFIRM)) { _, _ ->
            Toast.makeText(this.lendContext(), "ADDED", Toast.LENGTH_LONG).show()

            val editText = dialogView.findViewById<EditText>(R.id.insertActivity)

            timerActivitiesPresenter.addNewActivity(editText.text.toString())

        }

        dialogBuilder.setNegativeButton(context?.getString(R.string.DISMISS_DIALOG)) { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        dialogBuilder.show()

    }

    //TODO center the buttons
    private fun showDeleteActivityDialogBuilder()
    {
        val dialogBuilder = AlertDialog.Builder(this.lendContext(), R.style.AlertDialogCustom)
        dialogBuilder.setTitle(context?.getString(R.string.DEL_ACTIVITY_TITLE))

        dialogBuilder.setPositiveButton(context?.getString(R.string.DEL_ACTIVITY_CONFIRM)) { _, _->
            timerActivitiesPresenter.deleteActivity(activityItemAdapter.getSelectedActivityPosition())
        }

        dialogBuilder.setNegativeButton(context?.getString(R.string.DISMISS_DIALOG)) { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        dialogBuilder.show()
    }
}
