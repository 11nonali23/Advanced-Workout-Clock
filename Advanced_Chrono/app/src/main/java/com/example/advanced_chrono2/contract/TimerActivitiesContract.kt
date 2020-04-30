package com.example.advanced_chrono2.contract

import com.example.advanced_chrono2.R
import com.example.advanced_chrono2.model.BaseModel
import com.example.advanced_chrono2.model.TimerActivity
import com.example.advanced_chrono2.model.TimerItem

//This interface define a contract between the timer activities view and the presenter related to it


interface TimerActivitiesContract
{

    interface ITimerActivitiesModel : BaseModel
    {
        fun getAllTimerItems(activityId: Int): ArrayList<TimerItem>

        fun addTimerItem(timerItemData: TimerItem)

        fun delTimerItem(timerItemData: TimerItem)
    }


    interface ITimerActivitiesView
    {
        //Logo accessible for every class that needs it
        companion object { const val itemLogo = R.drawable.ic_timer_black }

        fun setUpView(activities: List<TimerActivity>)

        fun updateActivitiesView()

        fun updateTimerItemsView()

        fun displayResult(message: String)
    }

    interface ITimerActivitiesPresenter
    {
        //first steps to do when a view is created
        fun onViewCreated()

        //ToDO it wil also need the position in the real app
        fun addNewTimerItem(workoutMinutesText: String,
                            workoutSecondsText: String,
                            restMinutesText: String,
                            restSecondsText: String)

        //TODO need to understand what parameter pass
        fun deleteItem()
    }
}