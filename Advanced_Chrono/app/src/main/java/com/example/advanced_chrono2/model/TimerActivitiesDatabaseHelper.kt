package com.example.advanced_chrono2.model

import com.example.advanced_chrono2.view.fragments.TimerActivitiesFragment

/* This class is a singleton one ---> https://blog.mindorks.com/how-to-create-a-singleton-class-in-kotlin*/

//Fake database. One true will be added sooon

object TimerActivitiesDatabaseHelper {

    val activities: ArrayList<TimerActivityData> = ArrayList()
    private val timerItems = ArrayList<TimerItemData>()         //mokup items. All activities will have the same
    val activitiesName: ArrayList<String> = ArrayList()   //List of activity names to pass to the activity

    init {
        timerItems.add(TimerItemData(TimerActivitiesFragment.itemLogo, 20, 5))
        timerItems.add(TimerItemData(TimerActivitiesFragment.itemLogo, 30, 15))
        timerItems.add(TimerItemData(TimerActivitiesFragment.itemLogo, 40, 25))
        timerItems.add(TimerItemData(TimerActivitiesFragment.itemLogo, 50, 35))
        timerItems.add(TimerItemData(TimerActivitiesFragment.itemLogo, 60, 45))
        timerItems.add(TimerItemData(TimerActivitiesFragment.itemLogo, 70, 55))


        activities.add(TimerActivityData("Activity 1", timerItems))
        activities.add(TimerActivityData("Activity 2", timerItems))
        activities.add(TimerActivityData("Activity 3", timerItems))
        activities.add(TimerActivityData("Activity 4", timerItems))
        activities.add(TimerActivityData("Activity 5", timerItems))

        activities.forEach { activitiesName.add(it.name) }
    }


    fun getAllActivitiesName(): List<String>
    {
        return activitiesName
    }

    fun getTimerItemData(position: Int): ArrayList<TimerItemData>
    {
        return activities.get(position).timerItems
    }
}