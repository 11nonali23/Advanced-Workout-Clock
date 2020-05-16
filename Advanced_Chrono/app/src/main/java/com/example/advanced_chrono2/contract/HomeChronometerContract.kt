package com.example.advanced_chrono2.contract

import android.content.Context
import com.example.advanced_chrono2.model.BaseModel
import com.example.advanced_chrono2.model.ChronoActivity
import kotlin.collections.ArrayList

//This interface define a contract between the home view and the presenter related to it

interface HomeChronometerContract
{
    interface IHomeModel : BaseModel
    {
        fun addNewTiming(time: Long, timestamp: Long, activityId: Int): Boolean

        fun getTimings(activityId: Int): ArrayList<Pair<Long, Int>>?

    }

    interface IHomeChronometerView
    {
        fun setUpSpinnerView(activities: List<ChronoActivity>)

        fun updateActivitiesView()

        fun setNewItemAsSelected()

        fun displayResult(result: String)

        fun lendContext(): Context?

    }

    interface IHomePresenter
    {
        companion object
        {
            /*centralized map of activities with their timings
            This map contains only the activity that are selected previously from the user.
            If user selected another activity the map adds the activity and its timings.
            In this way I store only the activities the user work with and not the ones never selected */

            var activitiesValue = HashMap<Int, ArrayList<Pair<Long, Int>>>()
        }

        fun onViewCreated(context: Context?)

        fun addNewActivity(activityName: String)

        fun deleteActivity(activityName: String) //I can delete activity by name beacuase it is unique

        fun saveTempo(tempo: Long, activityId: Int)

        //fun getActivityTimings(activityId: Int): ArrayList<Pair<Long, Int>>
    }

}