package com.example.advanced_chrono2.contract

import android.content.Context
import com.example.advanced_chrono2.model.BaseModel
import com.example.advanced_chrono2.model.ChronometerActivity
import java.util.*
import kotlin.collections.ArrayList

//This interface define a contract between the home view and the presenter related to it

interface HomeChronometerContract
{
    interface IHomeModel : BaseModel
    {
        fun addNewTiming(time: Long, timestamp: Long, activityId: Int): Pair<Long, GregorianCalendar>?

        fun getTimings(activityId: Int) : ArrayList<Pair<Long, GregorianCalendar>>?

    }

    //TODO When the user change the selected item on the spinner I have to query the timings on the database
    interface IHomeChronometerView
    {
        fun setUpSpinnerView(activities: List<ChronometerActivity>)

        fun updateActivitiesView()

        fun updateTimingsView()

        fun setNewItemAsSelected()

        fun displayResult(result: String)

        fun lendContext(): Context?
    }

    interface IHomePresenter
    {
        companion object
        {
            /*List of activities.
            At first they have all the timings null, but then when on is selected the timings are uploaded
            This helps not to load timings that will not be seen from the user in a session*/
            var activities: ArrayList<ChronometerActivity> = ArrayList()

            //default selected activity is 0. //TODO use shared preferences to save it in a better way
            var currentSelectedActivity: Int? = null
        }

        fun onViewCreated(context: Context?, currentActivityId: Int)

        fun addNewActivity(activityName: String)

        fun deleteActivity(activityName: String) //I can delete activity by name beacuase it is unique

        fun saveTempo(tempo: Long, activityId: Int?)

        fun handleNewSelectedActivity(activityId: Int)
    }

}