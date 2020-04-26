package com.example.advanced_chrono2.presenter

import com.example.advanced_chrono2.contract.HomeChronometerContract
import com.example.advanced_chrono2.model.ActivityData
import com.example.advanced_chrono2.model.HomeDatabaseHelper

class HomePresenter(val fragment: HomeChronometerContract.IHomeChronometerView) : HomeChronometerContract.IHomePresenter
{
    private val activitiesName: ArrayList<String> =
        HomeDatabaseHelper.getAllActivitiesName() as ArrayList<String>  //List of activity names to pass to the activity

    override fun onViewCreated()
    {
        //retrive data from model and set up the spinner.
        fragment.setUpSpinnerView(this.activitiesName)
    }

    //Add new activity from model. It creates a new ActividyData provided a string and pass to the database
    //If the database add it successfully it will be addedd also in the local list
    override fun addNewActivity(activityName: String)
    {
        if (activityName.isNotEmpty())
        {
            val newActivityData = ActivityData(activityName, ArrayList())

            if(HomeDatabaseHelper.addActivity(newActivityData))
            {
                //activitiesName.add(newActivityData.name) --->This line add the list two times but in the real db will not
                fragment.updateActivitiesList()
            }
        }
        else
            fragment.displayResult("Activity must have text")
    }

    override fun deleteActivity(activityName: String)
    {
        //model try to del activity and return a succes code
    }

    override fun saveTempo(tempo: Long)
    {
        //model try to save tempo and return a code
    }
}