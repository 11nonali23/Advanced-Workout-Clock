package com.example.advanced_chrono2.model

import com.example.advanced_chrono2.contract.TimerActivitiesContract
import com.example.advanced_chrono2.view.fragments.TimerActivitiesFragment

/* This class is a singleton one ---> https://blog.mindorks.com/how-to-create-a-singleton-class-in-kotlin*/

//Fake database. One true will be added soon

object TimerActivitiesFAKEDatabase {

    val activities: ArrayList<TimerActivityData> = ArrayList()
    private val timerItems = ArrayList<TimerItemData>()         //mokup items. All activities will have the same

    init {
        timerItems.add(TimerItemData(TimerActivitiesContract.ITimerActivitiesView.itemLogo, 20, 5))
        timerItems.add(TimerItemData(TimerActivitiesContract.ITimerActivitiesView.itemLogo, 30, 15))
        timerItems.add(TimerItemData(TimerActivitiesContract.ITimerActivitiesView.itemLogo, 40, 25))
        timerItems.add(TimerItemData(TimerActivitiesContract.ITimerActivitiesView.itemLogo, 50, 35))
        timerItems.add(TimerItemData(TimerActivitiesContract.ITimerActivitiesView.itemLogo, 60, 45))
        timerItems.add(TimerItemData(TimerActivitiesContract.ITimerActivitiesView.itemLogo, 70, 55))


        activities.add(TimerActivityData("BICEPS", timerItems))
        activities.add(TimerActivityData("LEGS", timerItems))
        activities.add(TimerActivityData("CHEST", timerItems))
        activities.add(TimerActivityData("CARDIO", timerItems))
        activities.add(TimerActivityData("STRETCHING", timerItems))
    }


    fun getAllActivities(): ArrayList<TimerActivityData>
    {
        return activities
    }

    //TODO real db need position
    fun addTimerItem(timerItemData: TimerItemData): Boolean
    {
        timerItems.add(timerItemData)
        return true
    }
}