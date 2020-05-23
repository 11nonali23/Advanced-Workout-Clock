package com.example.advanced_chrono2.presenter

import android.content.Context
import android.util.Log
import com.example.advanced_chrono2.R
import com.example.advanced_chrono2.contract.HomeChronometerContract
import com.example.advanced_chrono2.model.ChronometerActivitiesDB
import java.util.*


import com.example.advanced_chrono2.contract.HomeChronometerContract.IHomePresenter.Companion.activities
import com.example.advanced_chrono2.contract.HomeChronometerContract.IHomePresenter.Companion.currentSelectedActivity


class HomePresenter(val view: HomeChronometerContract.IHomeChronometerView) : HomeChronometerContract.IHomePresenter
{
    private var model: ChronometerActivitiesDB? = null

    companion object
    {
        private const val TAG = "HOME PRESENTER"
    }

    private lateinit var viewContext: Context


    override fun onViewCreated(context: Context?, currentActivityId: Int)
    {
        if(context != null)
        {
            viewContext = context

            model = ChronometerActivitiesDB(context)
            activities = model!!.getAllActivities()
            view.setUpSpinnerView(activities)
            //setting the current selected activity to be the first
            if(activities.size > 0)
                currentSelectedActivity = 0
        }
    }

    //Add new activity from model. It creates a new ActividyData provided a string and pass to the database
    //If the database add it successfully it will be addedd also in the local list
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
                    view.displayResult(viewContext.getString(R.string.ADD_ACTIVITY_SUCCESS))
                    //Todo check this passage
                    view.updateActivitiesView()
                    view.setNewItemAsSelected()
                    //Last element added is the new current selected activity
                    currentSelectedActivity = activities.size - 1
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

    override fun deleteActivity(activityName: String)
    {
        /*If an activity isn't deleted it can't be an error of the user:
        the only deletable activities are the one already added!*/
        if (model != null)
        {
            if(model!!.deleteActivity(activityName))
            {
                activities.removeAll{it.name == activityName}
                view.displayResult(viewContext.getString(R.string.DEL_ACTIVITY_SUCCESS))
                view.updateActivitiesView()
                return
            }
        }
        view.displayResult(viewContext.getString(R.string.INTERNAL_ERROR))
    }


    override fun saveTempo(tempo: Long, activityId: Int?)
    {
        if (activityId == null)
        {
            view.displayResult(viewContext.getString(R.string.NO_SELECTED_ACTIVITY))
            return
        }
        if (model != null)
        {
            val newItem = model!!.addNewTiming(tempo, System.currentTimeMillis(), activityId)
            if(newItem != null)
            {
                if (currentSelectedActivity != null)
                    activities[currentSelectedActivity!!].timings_timestamp!!.add(newItem)
                view.displayResult(viewContext.getString(R.string.SAVE_TIMING_SUCCES))
                return
            }
        }
        view.displayResult(viewContext.getString(R.string.INTERNAL_ERROR))
    }

    //updates the current selected activity and add timings if weren't already
    override fun handleNewSelectedActivity(activityId: Int)
    {
        if (model == null)
        {
            view.displayResult(viewContext.getString(R.string.INTERNAL_ERROR))
            return
        }

        Log.e(TAG, "retriving new activity")
        var activityPosition: Int? = null

        //finding the position of the selected activity on the list
        for (i in activities.indices)
            if (activities[i].id == activityId)
                activityPosition = i

        if (activityPosition != null)
        {
            //if activity does not have the timings
            if (activities == activities[activityPosition].timings_timestamp)
                activities[activityPosition].timings_timestamp = model!!.getTimings(activityId)

            //updating the current selected activity
            currentSelectedActivity = activityPosition

            view.updateTimingsView()
        }
        //user can't select an activity with an id that is out of the activity list. If this case occurs it can only be an internal error
        else
        {
            view.displayResult(viewContext.getString(R.string.INTERNAL_ERROR))
            return
        }

    }
}