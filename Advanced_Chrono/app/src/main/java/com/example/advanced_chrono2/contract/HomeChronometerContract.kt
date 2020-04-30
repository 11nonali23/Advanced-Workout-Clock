package com.example.advanced_chrono2.contract

import android.content.Context
import com.example.advanced_chrono2.model.ChronoActivityData

//This interface define a contract between the home view and the presenter related to it

interface HomeChronometerContract
{
    interface IHomeModel
    {
        fun getAllActivitiesName(): ArrayList<String>?

        fun addNewActivity(name: String): Boolean

        fun deleteActivity(activityName: String): Boolean

        fun addNewTiming(time: Long, timestamp: Long, activityName: String)

    }

    interface IHomeChronometerView
    {
        fun setUpSpinnerView(activitiesName: List<String>)

        fun updateActivitiesList()

        fun displayResult(result: String)

    }

    interface IHomePresenter
    {
        fun onViewCreated(context: Context?)

        fun addNewActivity(activityName: String)

        fun deleteActivity(activityName: String) //I can delete activity by name beacuase it is unique

        fun saveTempo(tempo: Long)
    }

}