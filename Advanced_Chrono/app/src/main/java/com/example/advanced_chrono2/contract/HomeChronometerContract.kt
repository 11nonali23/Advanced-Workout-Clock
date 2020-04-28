package com.example.advanced_chrono2.contract

//This interface define a contract between the home view and the presenter related to it

interface HomeChronometerContract
{

    interface IHomeChronometerView
    {
        fun setUpSpinnerView(activitiesName: List<String>)

        fun updateActivitiesList()

        fun displayResult(result: String)

    }

    interface IHomePresenter
    {
        fun onViewCreated()

        fun addNewActivity(activityName: String)

        fun deleteActivity(activityName: String)

        fun saveTempo(tempo: Long)
    }

    interface IHomeModel
    {
        fun getAllActivitiesName(): ArrayList<String>

        fun addNewActivity(name: String)

        fun deleteActivity(activityId: Int)

        fun addNewTiming(time: Long, timestamp: Long, activityId: Int)

    }
}