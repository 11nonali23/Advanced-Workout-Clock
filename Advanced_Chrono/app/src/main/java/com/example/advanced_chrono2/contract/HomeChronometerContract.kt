package com.example.advanced_chrono2.contract

//This interface define a contract between the home view and the presenter related to it

interface HomeChronometerContract
{

    interface IHomeChronometerView
    {
        fun displayResult(result: String)

        fun setUpSpinnerView(activitiesName: List<String>)

        fun updateActivitiesList()

    }

    interface IHomePresenter
    {
        fun onViewCreated()

        fun addNewActivity(activityName: String)

        fun deleteActivity(activityName: String)

        fun saveTempo(tempo: Long)
    }
}