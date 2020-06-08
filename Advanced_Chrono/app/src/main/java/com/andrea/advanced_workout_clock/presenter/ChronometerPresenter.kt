package com.andrea.advanced_workout_clock.presenter

import android.content.Context
import com.andrea.advanced_workout_clock.R
import com.andrea.advanced_workout_clock.contract.ChronometerContract
import com.andrea.advanced_workout_clock.model.ChronometerActivitiesDB
import com.andrea.advanced_workout_clock.model.ActivityTiming


import com.andrea.advanced_workout_clock.contract.ChronometerContract.IChronometerPresenter.Companion.activities
import com.andrea.advanced_workout_clock.contract.ChronometerContract.IChronometerPresenter.Companion.currentSelectedActivity
import com.andrea.advanced_workout_clock.contract.ChartViewContract.IChartPresenter.Companion.observer
import com.andrea.advanced_workout_clock.contract.ChartViewContract.IChartObserver.CacheManager


class ChronometerPresenter(val view: ChronometerContract.IChronometerView) : ChronometerContract.IChronometerPresenter
{
    private var model: ChronometerActivitiesDB? = null
    private lateinit var viewContext: Context

    companion object
    {
        private const val TAG = "HOME PRESENTER"
    }

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
    override fun addNewActivity(activityName: String): Boolean
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
                    observer?.notifyActivityAdded()

                    view.displayResult(viewContext.getString(R.string.ADD_ACTIVITY_SUCCESS))
                    view.updateActivitiesView()
                    view.setNewItemAsSelected()
                    //Last element added is the new current selected activity
                    currentSelectedActivity = activities.size - 1
                    return true
                }
                else
                {
                    view.displayResult(viewContext.getString(R.string.ADD_ACTIVITY_DB_ERROR))
                    return false
                }
            }
            else
            {
                view.displayResult(viewContext.getString(R.string.ADD_EMPTY_ACTIVITY))
                return false
            }
        }
        view.displayResult(viewContext.getString(R.string.INTERNAL_ERROR))
        return false
    }

    override fun deleteActivity(activityName: String?)
    {
        /*If an activity isn't deleted it can't be an error of the user:
        the only deletable activities are the one already added!*/
        if (model == null)
        {   view.displayResult(viewContext.getString(R.string.INTERNAL_ERROR)); return  }

        if (activityName == null)
        {   view.displayResult(viewContext.getString(R.string.NO_SELECTED_ACTIVITY)); return }

        if (model!!.deleteActivity(activityName))
        {
            activities.removeAll { it.name == activityName }    //I can use removeAll because every activity has a different name
            view.displayResult(viewContext.getString(R.string.DEL_ACTIVITY_SUCCESS))
            view.updateActivitiesView()
        }
    }


    override fun deleteTiming(itemPosition: Int)
    {
        //if no activity is selected it means that there is no activity
        if (currentSelectedActivity == null)
        {
            view.displayResult(viewContext.getString(R.string.NO_SELECTED_ACTIVITY))
            return
        }

        //checking if data structures and DB are initialized. If not it can't be a user error
        if(model == null)
        {
            view.displayResult(viewContext.getString(R.string.INTERNAL_ERROR))
            return
        }

        //check if activity posisition exists. If it doesn't it can be only an internal error. User can't select an unexisting item
        if(currentSelectedActivity!! >= activities.size)
        {
            view.displayResult(viewContext.getString(R.string.INTERNAL_ERROR))
            return
        }

        //if timings and timestamps are null it means i didn't handle correctly the addition mechanism
        if (activities[currentSelectedActivity!!].timings_timestamp == null)
        {
            view.displayResult(viewContext.getString(R.string.INTERNAL_ERROR))
            return
        }

        //check if item posisition exists. If it doesn't it can be only an internal error. User can't select an unexisting item
        if(itemPosition >= activities[currentSelectedActivity!!].timings_timestamp!!.size)
        {
            view.displayResult(viewContext.getString(R.string.INTERNAL_ERROR))
            return
        }

        /*If the parent ID isn't in the itemList it means i have an internal error! It can't be user fault.
        *This is because the user can't handle the id's of the items*/
        val parentActivityID: Int

        try {
            parentActivityID = activities[currentSelectedActivity!!].id
        }
        catch (exc: ArrayIndexOutOfBoundsException){
            view.displayResult(viewContext.getString(R.string.INTERNAL_ERROR))
            return
        }

        //Same concepet of above for itemId
        val timingId: Int

        try {
            timingId = activities[currentSelectedActivity!!].timings_timestamp!![itemPosition].id
        }
        catch (exc: Exception ){
            view.displayResult(viewContext.getString(R.string.INTERNAL_ERROR))
            return
        }

        //when i have the two IDs i am ready to call the DB.
        //If the model succesfully deletes the item i don't want to log nothing to the user. I just want to delete the item from the list
        if(model!!.deleteTiming(timingId, parentActivityID))
        {
            //delete the item if it was cached into the CacheManager to be add into charts
            CacheManager.deleteCachedTiming(activities[currentSelectedActivity!!].id, activities[currentSelectedActivity!!].timings_timestamp!![itemPosition].id)
            activities[currentSelectedActivity!!].timings_timestamp!!.removeAt(itemPosition)
            view.itemRemovedFromDataSet(itemPosition)
            return
        }
        else
            view.displayResult(viewContext.getString(R.string.INTERNAL_ERROR))
    }


    override fun saveTiming(tempo: Long, activityId: Int?): ActivityTiming?
    {
        //if no activity is selected I will ask the user to create one to store the timings
        if (activityId == null)
        {
            view.displayResult(viewContext.getString(R.string.CREATE_ACTIVITY_HINT))
            view.showNewActivityDialog()
            return null
        }
        if (model != null)
        {
            val newTiming = model!!.addNewTiming(tempo, System.currentTimeMillis(), activityId)
            if(newTiming != null)
            {
                CacheManager.cacheNewTiming(activities[currentSelectedActivity!!].id, newTiming)
                if (currentSelectedActivity != null)
                    activities[currentSelectedActivity!!].timings_timestamp!!.add(newTiming)

                view.displayResult(viewContext.getString(R.string.SAVE_TIMING_SUCCES))
                return newTiming
            }
        }
        view.displayResult(viewContext.getString(R.string.INTERNAL_ERROR))
        return null
    }

    //updates the current selected activity and add timings if weren't already
    override fun handleNewSelectedActivity(activityId: Int)
    {
        if (model == null)
        {
            view.displayResult(viewContext.getString(R.string.INTERNAL_ERROR))
            return
        }

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