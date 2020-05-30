package com.andrea.advanced_workout_clock.presenter

import android.content.Context
import com.andrea.advanced_workout_clock.R
import com.andrea.advanced_workout_clock.contract.TimerActivitiesContract
import com.andrea.advanced_workout_clock.model.TimerActivitiesDB
import com.andrea.advanced_workout_clock.contract.TimerActivitiesContract.ITimerActivitiesPresenter
import com.andrea.advanced_workout_clock.model.TimerItem
import java.lang.Exception
import java.lang.NumberFormatException
import kotlin.collections.ArrayList

//The field activitiesList of the companion object of ITimerActivitiesPresenter wil be renamed as activities
import com.andrea.advanced_workout_clock.contract.TimerActivitiesContract.ITimerActivitiesPresenter.Companion.activitiesList as activities

import com.andrea.advanced_workout_clock.contract.TimerActivitiesContract.ITimerActivitiesPresenter.Companion.currentActivityPosition as selectedActivityPosition


class TimerActivitiesPresenter(val view: TimerActivitiesContract.ITimerActivitiesView) : ITimerActivitiesPresenter
{

    private var model: TimerActivitiesDB? = null

    companion object
    {
        private const val TAG = "TIMER PRESENTER"
    }

    private lateinit var viewContext: Context


    override fun onViewCreated(context: Context?)
    {
        if (context!= null)
        {
            viewContext = context

            model = TimerActivitiesDB(context)
            activities = model!!.getAllActivities()
            //If there is some activity i set up the views
            if(activities.size != 0)
                view.setUpView()
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
                    activities.add(newActivity)
                    selectedActivityPosition = activities.size - 1 //new activity will be selected

                    view.displayResult(viewContext.getString(R.string.ADD_ACTIVITY_SUCCESS))

                    //If it is the first activity added i have to set up the view
                    if(!view.isViewSetUp())
                        view.setUpView()

                    view.changeAddItemButtonVisibility(true) //set add item button as visible
                    view.activitiesDataSetChanged()
                    view.itemDataSetChanged()
                    return
                }
                else
                {
                    view.displayResult(viewContext.getString(R.string.ADD_ACTIVITY_DB_ERROR))
                    return
                }
            }
            else
            {
                view.displayResult(viewContext.getString(R.string.ADD_EMPTY_ACTIVITY))
                return
            }
        }
        view.displayResult(viewContext.getString(R.string.INTERNAL_ERROR))
    }

    //TODO refactor it
    override fun deleteActivity(position: Int?): Boolean
    {
        if (model == null)
        {
            view.displayResult(viewContext.getString(R.string.INTERNAL_ERROR))
            return false
        }
        //If position is null the user didn't select a possition
        if(position == null)
        {
            view.displayResult(viewContext.getString(R.string.NO_SELECTED_ACTIVITY))
            return false
        }
        //There's no such case that the user selects an out of bounds exception. It can only be a system error
        if(position >= activities.size)
        {
            view.displayResult(viewContext.getString(R.string.INTERNAL_ERROR))
            return false
        }

        if(model!!.deleteActivity(activities[position].name))
        {
            activities.removeAt(position)
            selectedActivityPosition = null
            view.displayResult(viewContext.getString(R.string.DEL_ACTIVITY_SUCCESS))
            view.activityRemovedFromDataSet(position)
            view.activitiesDataSetChanged()
            view.itemDataSetChanged()
            view.changeAddItemButtonVisibility(false)
            return true
        }

        view.displayResult(viewContext.getString(R.string.INTERNAL_ERROR))
        return false
    }

    override fun addNewTimerItem(
        selectedActivityPosition: Int?,
        workoutMinutesText: String,
        workoutSecondsText: String,
        restMinutesText: String,
        restSecondsText: String
    ) {

        //checking if data structures and DB are initialized. If not it can't be a user error
        if (this.model == null)
        {
            view.displayResult(viewContext.getString(R.string.INTERNAL_ERROR))
            return
        }

        //if the parent activity id is null it means that no element is selected
        if (selectedActivityPosition == null)
        {
            view.displayResult(viewContext.getString(R.string.NO_SELECTED_ACTIVITY))
            return
        }

        //If the parent ID isn't in the itemList it means i have an internal error! It can't be user fault.
        val parentActivityID: Int

        try {
            parentActivityID = activities[selectedActivityPosition].id
        }
        catch (exc: ArrayIndexOutOfBoundsException){
            view.displayResult(viewContext.getString(R.string.INTERNAL_ERROR))
            return
        }

        //Checking if at least one value is not empty for seconds e minutes for both workout and rest
        if(workoutMinutesText.isEmpty() && workoutSecondsText.isEmpty())
        {
            view.displayResult(viewContext.getString(R.string.EMPTY_ITEM_FIELDS))
            return
        }
        if (restMinutesText.isEmpty() && restSecondsText.isEmpty())
        {
            view.displayResult(viewContext.getString(R.string.EMPTY_ITEM_FIELDS))
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
            view.displayResult(viewContext.getString(R.string.NUMBER_FORMAT_ERROR))
            return
        }

        val newItem = model!!.addTimerItem(parentActivityID, workoutMinutesInSeconds + workoutSeconds, restMinutesInSeconds + restSeconds)

        if (newItem != null)
        {
            activities.forEach { if (it.id == parentActivityID) it.timerItems.add(newItem) }
            view.itemDataSetChanged()
            return
        }

        view.displayResult(viewContext.getString(R.string.INTERNAL_ERROR))

    }

    override fun deleteItem(selectedActivityPosition: Int?, itemPosition: Int)
    {
        //checking if data structures and DB are initialized. If not it can't be a user error
        if(model == null)
        {
            view.displayResult(viewContext.getString(R.string.INTERNAL_ERROR))
            return
        }

        //Checking that an activity is selected. If not i will inform user via the view
        if (selectedActivityPosition == null)
        {
            view.displayResult(viewContext.getString(R.string.NO_SELECTED_ACTIVITY))
            return
        }

        //check if activity posisition exists. If it doesn't it can be only an internal error. User can't select an unexisting item
        if(selectedActivityPosition >= activities.size)
        {
            view.displayResult(viewContext.getString(R.string.INTERNAL_ERROR))
            return
        }
        //check if item posisition exists. If it doesn't it can be only an internal error. User can't select an unexisting item
        if(itemPosition >= activities[selectedActivityPosition].timerItems.size)
        {
            view.displayResult(viewContext.getString(R.string.INTERNAL_ERROR))
            return
        }

        /*If the parent ID isn't in the itemList it means i have an internal error! It can't be user fault.
        *This is beacuse the user can't handle the id's of the items*/
        val parentActivityID: Int

        try {
            parentActivityID = activities[selectedActivityPosition].id
        }
        catch (exc: ArrayIndexOutOfBoundsException){
            view.displayResult(viewContext.getString(R.string.INTERNAL_ERROR))
            return
        }

        //Same concepet of above for itemId
        val itemID: Int

        try {
            itemID = activities[selectedActivityPosition].timerItems[itemPosition].id
        }
        catch (exc: Exception ){
            view.displayResult(viewContext.getString(R.string.INTERNAL_ERROR))
            return
        }

        //when i have the two IDs i am ready to call the DB.

        //If the model succesfully deletes the item i don't want to log nothing to the user. I just want to delete the item from the list
        if(model!!.delTimerItem(parentActivityID, itemID))
        {
            activities[selectedActivityPosition].timerItems.removeAt(itemPosition)
            view.itemRemovedFromDataSet(itemPosition)
            return
        }
        else
            view.displayResult(viewContext.getString(R.string.INTERNAL_ERROR))
    }

    override fun onSelectedActivityChange(position: Int?)
    {
        //If activities is null there's an internal error since the db returns for each activity a non-empty list (see the contract package)
        //check if posisition exists. If it doesn't it can be only an internal error. User can't select an unexisting item
        if(position != null && position >= activities.size)
        {
            view.displayResult(viewContext.getString(R.string.INTERNAL_ERROR))
            return
        }

        selectedActivityPosition = position
        if (position != null)
            view.changeAddItemButtonVisibility(true)
        view.itemDataSetChanged()
    }

    override fun onActivityStart(position: Int?): Pair<String, ArrayList<TimerItem>>?
    {
        //user can't select an unexisting item
        if (position == null)
        {
            view.displayResult(viewContext.getString(R.string.INTERNAL_ERROR))
            return null
        }

        if (activities[position].timerItems.size == 0)
        {
            view.displayResult(viewContext.getString(R.string.EMPTY_ACTIVITY_ON_START))
            return null
        }

        return Pair(activities[position].name, activities[position].timerItems)
    }

}