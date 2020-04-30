package com.example.advanced_chrono2.model

import com.example.advanced_chrono2.contract.TimerActivitiesContract

/* This class is a singleton one ---> https://blog.mindorks.com/how-to-create-a-singleton-class-in-kotlin*/

//Fake database. One true will be added soon

object TimerActivitiesFAKEDatabase {

    val activities: ArrayList<TimerActivity> = ArrayList()
    private val timerItems = ArrayList<TimerItem>()         //mokup items. All activities will have the same

    init {
        timerItems.add(TimerItem(0 ,TimerActivitiesContract.ITimerActivitiesView.itemLogo, 20, 5))
        timerItems.add(TimerItem(1 ,TimerActivitiesContract.ITimerActivitiesView.itemLogo, 30, 15))
        timerItems.add(TimerItem(2 ,TimerActivitiesContract.ITimerActivitiesView.itemLogo, 40, 25))
        timerItems.add(TimerItem(3 ,TimerActivitiesContract.ITimerActivitiesView.itemLogo, 50, 35))
        timerItems.add(TimerItem(4 ,TimerActivitiesContract.ITimerActivitiesView.itemLogo, 60, 45))
        timerItems.add(TimerItem(5 ,TimerActivitiesContract.ITimerActivitiesView.itemLogo, 70, 55))


        activities.add(TimerActivity(0,"BICEPS", timerItems))
        activities.add(TimerActivity(1,"LEGS", timerItems))
        activities.add(TimerActivity(2,"CHEST", timerItems))
        activities.add(TimerActivity(3,"CARDIO", timerItems))
        activities.add(TimerActivity(4,"STRETCHING", timerItems))
    }


    fun getAllActivities(): ArrayList<TimerActivity>
    {
        return activities
    }

    //TODO real db need position
    fun addTimerItem(timerItemData: TimerItem): Boolean
    {
        timerItems.add(timerItemData)
        return true
    }
}