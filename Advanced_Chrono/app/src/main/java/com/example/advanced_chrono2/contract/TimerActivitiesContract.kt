package com.example.advanced_chrono2.contract

import com.example.advanced_chrono2.R
import com.example.advanced_chrono2.model.TimerActivityData

//This interface define a contract between the timer activities view and the presenter related to it


interface TimerActivitiesContract
{

    interface ITimerActivitiesPresenter
    {
        fun onViewCreated()

        //ToDO it wil also need the position in the real app
        fun addNewTimerItem(workoutMinutesText: String,
                            workoutSecondsText: String,
                            restMinutesText: String,
                            restSecondsText: String)

        fun deleteItem()
    }

    interface ITimerActivitiesView
    {
        //Logo accessible for every class that needs it
        companion object { const val itemLogo = R.drawable.ic_timer_black }

        fun setUpView(activities: List<TimerActivityData>)

        fun updateActivitiesView()

        fun updateTimerItemsView()

        fun displayResult(message: String)

    }
}