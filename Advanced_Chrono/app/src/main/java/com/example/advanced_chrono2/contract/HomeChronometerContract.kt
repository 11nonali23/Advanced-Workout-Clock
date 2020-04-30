package com.example.advanced_chrono2.contract

import android.content.Context
import com.example.advanced_chrono2.model.ChronoActivity
import kotlin.collections.ArrayList

//This interface define a contract between the home view and the presenter related to it

interface HomeChronometerContract
{
    interface IHomeModel
    {
        fun getAllActivities(): ArrayList<ChronoActivity>?

        fun addNewActivity(name: String): ChronoActivity?

        fun deleteActivity(activityName: String): Boolean

        fun addNewTiming(time: Long, timestamp: Long, activitiyId: Int): Boolean

    }

    interface IHomeChronometerView
    {
        fun setUpSpinnerView(activities: List<ChronoActivity>)

        fun updateActivitiesList()

        fun setNewItemAsSelected()

        fun displayResult(result: String)

    }

    interface IHomePresenter
    {
        fun onViewCreated(context: Context?)

        fun addNewActivity(activityName: String)

        fun deleteActivity(activityName: String) //I can delete activity by name beacuase it is unique

        fun saveTempo(tempo: Long, activityId: Int)
    }

}