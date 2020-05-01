package com.example.advanced_chrono2.presenter

import android.content.Context
import android.util.Log
import com.example.advanced_chrono2.contract.TimerActivitiesContract
import com.example.advanced_chrono2.model.TimerActivitiesDB
import com.example.advanced_chrono2.model.TimerActivity
import java.lang.Exception
import java.lang.NumberFormatException

class TimerActivitiesPresenter(val view: TimerActivitiesContract.ITimerActivitiesView) : TimerActivitiesContract.ITimerActivitiesPresenter
{

    private var model: TimerActivitiesDB? = null
    private var activities: ArrayList<TimerActivity>? = null

    companion object
    {
        private const val TAG = "TIMER PRESENTER"

        private const val INTERNAL_ERROR = "Internal Error"

        //add activity logs to the user
        private const val ADD_ACTIVITY_SUCCES = "Succesfully added!"
        private const val ADD_ACTIVITY_DB_ERROR = "Error: this name already exists!"
        private const val ADD_EMPTY_ACTIVITY = "Error: activity can' t be empty"

        private const val DEL_ACTIVITY_SUCCES = "Succesfully deleted!"

        private const val NO_SELECTED_ACTIVITY = "ERROR! Please select an activity"

        //add item logs to the user
        private const val NUMBER_FORMAT_ERROR = "Error: only numbers are accepted"
        private const val EMPTY_ITEM_FIELDS = "Error. Use at least one field for both rest e workout"

    }


    override fun onViewCreated(context: Context?)
    {
        if (context!= null)
        {
            model = TimerActivitiesDB(context)
            activities = model!!.getAllActivities()
            //If there is some activity i set up the views
            if(activities!!.size != 0)
                view.setUpView(activities!!)
        }
    }

    override fun addNewActivity(activityName: String)
    {
        if (model != null)
        {
            val activityNameTrimmed: String = activityName.trimEnd().trimStart() //trimeEnd and trimStart remove all special character at the end and the start
            if (activityNameTrimmed.isNotEmpty())
            {
                val newActivity = model!!.addNewActivity(activityNameTrimmed)

                if (newActivity != null)
                {
                    activities?.add(newActivity)
                    view.displayResult(ADD_ACTIVITY_SUCCES)

                    //If it is the first activitity added i have to set up the view
                    if(!view.isViewSettedUp())
                        view.setUpView(activities!!)
                    view.updateActivitiesView()
                    view.updateTimerItemsView()
                    return
                }
                else
                {
                    view.displayResult(ADD_ACTIVITY_DB_ERROR)
                    return
                }
            }
            else
            {
                view.displayResult(ADD_EMPTY_ACTIVITY)
                return
            }
        }
        view.displayResult(INTERNAL_ERROR)
    }

    override fun deleteActivity(activityName: String)
    {
        /*If an activity isn't deleted it can't be an error of the user:
       the only deletable activities are the one already added!*/
        if (model != null)
        {
            if(model!!.deleteActivity(activityName))
            {
                activities?.removeAll{it.name == activityName}
                view.displayResult(DEL_ACTIVITY_SUCCES)
                view.updateActivitiesView()
                return
            }
        }
        view.displayResult(INTERNAL_ERROR)
    }

    override fun addNewTimerItem(
        selectedActivityPosition: Int?,
        workoutMinutesText: String,
        workoutSecondsText: String,
        restMinutesText: String,
        restSecondsText: String
    ) {

        //checking if data structures and DB are initialized. If not it can't be a user error
        if (this.model == null || this.activities == null)
        {
            view.displayResult(INTERNAL_ERROR)
            return
        }

        //if the parent activity id is null it means that no element is selected
        if (selectedActivityPosition == null)
        {
            view.displayResult(NO_SELECTED_ACTIVITY)
            return
        }

        //If the parent ID isn't in the itemList it means i have an internal error! It can't be user fault.
        val parentActivityID: Int

        try {
            parentActivityID = this.activities!![selectedActivityPosition].id
        }
        catch (exc: ArrayIndexOutOfBoundsException){
            view.displayResult(INTERNAL_ERROR)
            return
        }

        //Checking if at least one value is not empty for seconds e minutes for both workout and rest
        if(workoutMinutesText.isEmpty() && workoutSecondsText.isEmpty())
        {
            view.displayResult(EMPTY_ITEM_FIELDS)
            return
        }
        if (restMinutesText.isEmpty() && restSecondsText.isEmpty())
        {
            view.displayResult(EMPTY_ITEM_FIELDS)
            return
        }

        /*creating seconds value for each string passed by converting minutes into seconds).
        If i have NumberFormatException i will advise view and end*/
        val workoutMinutesInSeconds: Int
        val workoutSeconds: Int
        val restMinutesInSeconds: Int
        val restSeconds: Int

        try {
            workoutMinutesInSeconds = if (workoutMinutesText.isNotEmpty()) workoutMinutesText.toInt() * 60  else 0
            workoutSeconds = if(workoutSecondsText.isNotEmpty()) workoutSecondsText.toInt() else 0
            restMinutesInSeconds = if (restMinutesText.isNotEmpty()) restMinutesText.toInt() * 60  else 0
            restSeconds = if(restSecondsText.isNotEmpty()) restSecondsText.toInt() else 0
        }
        catch (exc: NumberFormatException){
            view.displayResult(NUMBER_FORMAT_ERROR)
            return
        }

        val newItem = model!!.addTimerItem(parentActivityID, workoutMinutesInSeconds + workoutSeconds, restMinutesInSeconds + restSeconds)

        if (newItem != null)
        {
            activities!!.forEach { if (it.id == parentActivityID) it.timerItems?.add(newItem) }
            view.updateTimerItemsView()
            return
        }

        view.displayResult(INTERNAL_ERROR)

    }

    override fun deleteItem(selectedActivityPosition: Int?, position: Int)
    {

        Log.e(TAG, "PARENT ACTIVITY POSITION: $selectedActivityPosition ITEM POSITION: $position")

        //checking if data structures and DB are initialized. If not it can't be a user error
        if(model == null || activities == null)
        {
            view.displayResult(INTERNAL_ERROR)
            return
        }

        //Checking that an activity is selected. If not i will inform user via the view
        if (selectedActivityPosition == null)
        {
            view.displayResult(NO_SELECTED_ACTIVITY)
            return
        }

        //check if activity posisition exists. If it doesn't it can be only an internal error. User can't select an unexisting item
        if(selectedActivityPosition >= activities!!.size)
        {
            view.displayResult(INTERNAL_ERROR)
            return
        }
        //check if item posisition exists. If it doesn't it can be only an internal error. User can't select an unexisting item
        if(position >= activities!![selectedActivityPosition].timerItems!!.size)
        {
            view.displayResult(INTERNAL_ERROR)
            return
        }
        //if the timer items is null it is an internal error for the same reason of the first condition check
        if(activities!![position].timerItems == null)
        {
            view.displayResult(INTERNAL_ERROR)
            return
        }

        /*If the parent ID isn't in the itemList it means i have an internal error! It can't be user fault.
        *This is beacuse the user can't handle the id's of the items*/
        val parentActivityID: Int

        try {
            parentActivityID = this.activities!![selectedActivityPosition].id
        }
        catch (exc: ArrayIndexOutOfBoundsException){
            view.displayResult(INTERNAL_ERROR)
            Log.e(TAG, "OUT OF BOUND FOR PARENT ACTIVITY")
            return
        }

        Log.e(TAG, "PARENT ACTIVITY ID: $parentActivityID")


        //Same concepet of above for itemId
        val itemID: Int

        try {
            itemID = this.activities!![selectedActivityPosition].timerItems!![position].id
        }
        catch (exc: Exception ){
            view.displayResult(INTERNAL_ERROR)
            Log.e(TAG, "OUT OF BOUND FOR CHILD ITEM")
            return
        }

        Log.e(TAG, "ITEM ID: $itemID")

        //when i have the two IDs i am ready to call the DB.

        //If the model succesfully deletes the item i don't want to log nothing to the user. I just want to delete the item from the list
        if(model!!.delTimerItem(parentActivityID, itemID))
        {
            activities!![selectedActivityPosition].timerItems!!.removeAt(position)
            view.updateTimerItemsView()
            return
        }
        else
            view.displayResult(INTERNAL_ERROR)
    }

    override fun onSelectedActivityChange(position: Int)
    {
        //If activities is null there's an internal error since the db returns for each activity a non-empty list (see the contract package)
        if(activities == null)
        {
            view.displayResult(INTERNAL_ERROR)
            return
        }
        //check if posisition exists. If it doesn't it can be only an internal error. User can't select an unexisting item
        if(position >= activities!!.size)
        {
            view.displayResult(INTERNAL_ERROR)
            return
        }
        //if the timer items is null it is an internal error for the same reason of the first condition check
        if(activities!![position].timerItems == null)
        {
            view.displayResult(INTERNAL_ERROR)
            return
        }

        view.changeTimerItemListView(activities!![position].timerItems!!)
        view.updateTimerItemsView()
    }

}