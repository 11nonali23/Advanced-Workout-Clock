package com.example.advanced_chrono2.presenter

import android.content.Context
import com.example.advanced_chrono2.contract.TimerActivitiesContract
import com.example.advanced_chrono2.model.TimerActivitiesDB
import com.example.advanced_chrono2.model.TimerActivity
import java.lang.NumberFormatException

class TimerActivitiesPresenter(val view: TimerActivitiesContract.ITimerActivitiesView) : TimerActivitiesContract.ITimerActivitiesPresenter
{

    private var model: TimerActivitiesDB? = null
    private var activities: ArrayList<TimerActivity>? = null

    companion object
    {
        private const val INTERNAL_ERROR = "Internal Error: activity not added"

        //add activity logs to the user
        private const val ADD_ACTIVITY_SUCCES = "Succesfully added!"
        private const val ADD_ACTIVITY_DB_ERROR = "Error: this name already exists!"
        private const val ADD_EMPTY_ACTIVITY = "Error: activity can' t be empty"

        private const val DEL_ACTIVITY_SUCCES = "Succesfully deleted!"

        //add item logs to the user
        private const val NUMBER_FORMAT_ERROR = "Error: only numbers are accepted"
        private const val EMPTY_ITEM_FIELDS = "Error. Use at least one field for both rest e workout"
        private const val ADD_ITEM_NO_SELECTED_ACTIVITY = "No activity is selected"

        //TODO implement an example activity
        private const val EXAMPLE_ACTIVITY_NAME = "EXAMPLE ACTIVITY"
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
        parentId: Int?,
        workoutMinutesText: String,
        workoutSecondsText: String,
        restMinutesText: String,
        restSecondsText: String
    ) {

        if (model == null)
        {
            view.displayResult(INTERNAL_ERROR)
            return
        }

        //if the parent activity id is null it means that no element is selected
        if (parentId == null)
        {
            view.displayResult(ADD_ITEM_NO_SELECTED_ACTIVITY)
            return
        }
        //Checking if at least one value is not empty for seconds e minutes
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

        //creating seconds value for each string passed. If i have NumberFormatException i will advise view and end
        //Converting minutes in seconds
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

        val newItem = model!!.addTimerItem(parentId, workoutMinutesInSeconds + workoutSeconds, restMinutesInSeconds + restSeconds)

        if (newItem != null)
        {
            activities!!.forEach { if (it.id == parentId) it.timerItems?.add(newItem) }
            view.updateTimerItemsView()
            return
        }

        view.displayResult(INTERNAL_ERROR)

    }

    override fun deleteItem() {
        TODO("Not yet implemented")
    }

}