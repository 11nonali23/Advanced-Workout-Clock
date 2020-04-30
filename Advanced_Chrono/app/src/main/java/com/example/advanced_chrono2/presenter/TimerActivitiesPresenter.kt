package com.example.advanced_chrono2.presenter

import com.example.advanced_chrono2.contract.TimerActivitiesContract
import com.example.advanced_chrono2.model.TimerActivitiesFAKEDatabase
import com.example.advanced_chrono2.model.TimerActivity
import com.example.advanced_chrono2.model.TimerItem
import java.lang.NumberFormatException

class TimerActivitiesPresenter(val fragment: TimerActivitiesContract.ITimerActivitiesView) : TimerActivitiesContract.ITimerActivitiesPresenter
{

    companion object
    {
        private const val numberFormatExceptionMessage = "Error: only numbers are accepted"
        private const val emptyFieldMessage = "Error. Use at least one field for both rest e workout"
        private const val internalErroMessage = "Sorry, internal error occurred"
    }

    private val activities: ArrayList<TimerActivity> =
        TimerActivitiesFAKEDatabase.getAllActivities()  //List of activity names to pass to the activity

    override fun onViewCreated()
    {
        fragment.setUpView(activities)
    }

    override fun addNewTimerItem(
        workoutMinutesText: String,
        workoutSecondsText: String,
        restMinutesText: String,
        restSecondsText: String
    )
    {

        //Checking if at least one value is not empty for seconds e minutes
        if(workoutMinutesText.isEmpty() && workoutSecondsText.isEmpty())
        {
            fragment.displayResult(emptyFieldMessage)
            return
        }

        if (restMinutesText.isEmpty() && restSecondsText.isEmpty())
        {
            fragment.displayResult(emptyFieldMessage)
            return
        }

        //creating seconds value for each string passed. If i have NumberFormatException i will advise view and end
        //Converting minutes in seconds
        val workoutMinutesInSeconds: Long
        val workoutSeconds: Long
        val restMinutesInSeconds: Long
        val restSeconds: Long

        try {
            workoutMinutesInSeconds = if (workoutMinutesText.isNotEmpty()) workoutMinutesText.toLong() * 60  else 0L
            workoutSeconds = if(workoutSecondsText.isNotEmpty()) workoutSecondsText.toLong() else 0L
            restMinutesInSeconds = if (restMinutesText.isNotEmpty()) restMinutesText.toLong() * 60  else 0L
            restSeconds = if(restSecondsText.isNotEmpty()) restSecondsText.toLong() else 0L
        }
        catch (exc: NumberFormatException){
            fragment.displayResult(numberFormatExceptionMessage)
            return
        }

        //PROVVISORIO
        val newTimerItemData =
            TimerItem(10,TimerActivitiesContract.ITimerActivitiesView.itemLogo,
                workoutMinutesInSeconds + workoutSeconds,
                restMinutesInSeconds + restSeconds)

        if (TimerActivitiesFAKEDatabase.addTimerItem(newTimerItemData))
            fragment.updateTimerItemsView()
        else
            fragment.displayResult(internalErroMessage)
    }


    override fun deleteItem() {
        //TODO
    }
}