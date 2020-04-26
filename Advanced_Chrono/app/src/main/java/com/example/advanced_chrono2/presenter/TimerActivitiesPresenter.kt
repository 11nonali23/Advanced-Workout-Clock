package com.example.advanced_chrono2.presenter

import com.example.advanced_chrono2.contract.TimerActivitiesContract
import com.example.advanced_chrono2.model.TimerActivitiesDatabaseHelper
import com.example.advanced_chrono2.model.TimerItemData

class TimerActivitiesPresenter(val fragment: TimerActivitiesContract.ITimerActivitiesView) : TimerActivitiesContract.ITimerActivitiesPresenter
{

    private val activitiesName: ArrayList<String> =
        TimerActivitiesDatabaseHelper.getAllActivitiesName() as ArrayList<String>  //List of activity names to pass to the activity

    private val timerActivities: ArrayList<TimerItemData> =
        TimerActivitiesDatabaseHelper.getTimerItemData(0)

    override fun onViewCreated()
    {
        fragment.setUpView(activitiesName, timerActivities)
    }

    //TODO in real app it needs also the position
    override fun addNewTimerItem(workoutSeconds: Long, restSeconds: Long) {
        //TODO
    }


    override fun deleteItem() {
        //TODO
    }
}