package com.example.advanced_chrono2.contract

import com.example.advanced_chrono2.model.TimerActivityData
import com.example.advanced_chrono2.model.TimerItemData

//This interface define a contract between the timer activities view and the presenter related to it


interface TimerActivitiesContract
{

    interface ITimerActivitiesPresenter
    {
        fun onViewCreated()

        //ToDO it wil also need the position in the real app
        fun addNewTimerItem(workoutSeconds: Long, restSeconds: Long)

        fun deleteItem()
    }

    interface ITimerActivitiesView
    {
        fun setUpView(activities: List<String>, timerItems: List<TimerItemData>)

        fun updateActivitiesView()

        fun updateTimerItemsView()

    }
}