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
        fun addNewTiming(time: Long, timestamp: Long, activitiyId: Int): Boolean

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
        fun onViewCreated(context: Context?)

        fun addNewActivity(activityName: String)

        fun deleteActivity(activityName: String) //I can delete activity by name beacuase it is unique

        fun saveTempo(tempo: Long, activityId: Int)
    }

}