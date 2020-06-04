package com.andrea.advanced_workout_clock.contract

import android.content.Context
import com.andrea.advanced_workout_clock.model.ActivityTiming
import com.andrea.advanced_workout_clock.model.BaseModel
import com.andrea.advanced_workout_clock.model.ChronometerActivity
import kotlin.collections.ArrayList

//This interface define a contract between the home view and the presenter related to it

interface ChronometerContract
{
    interface IChronometerModel : BaseModel
    {
        fun addNewTiming(time: Long, timestamp: Long, activityId: Int): ActivityTiming?

        fun getTimings(activityId: Int) : ArrayList<ActivityTiming>

        fun deleteTiming(timingId: Int, parentActivityId: Int): Boolean

    }

    //TODO When the user change the selected item on the spinner I have to query the timings on the database
    interface IChronometerView
    {
        fun setUpSpinnerView(activities: List<ChronometerActivity>)

        fun updateActivitiesView()

        fun updateTimingsView()

        fun setNewItemAsSelected()

        fun itemRemovedFromDataSet(itemPosition: Int)

        fun displayResult(result: String)

        fun showNewActivityDialog()

        fun lendContext(): Context?
    }

    interface IChronometerPresenter
    {
        //Todo use view model in future
        companion object
        {
            /*List of activities.
            At first they have all the timings null, but then when on is selected the timings are uploaded
            This helps not to load timings that will not be seen from the user in a session*/
            var activities: ArrayList<ChronometerActivity> = ArrayList()

            var currentSelectedActivity: Int? = null
        }

        fun onViewCreated(context: Context?, currentActivityId: Int)

        fun addNewActivity(activityName: String): Boolean

        fun deleteActivity(activityName: String?) //I can delete activity by name beacuase it is unique

        fun deleteTiming(itemPosition: Int)

        fun saveTiming(tempo: Long, activityId: Int?): ActivityTiming?

        fun handleNewSelectedActivity(activityId: Int)
    }

}