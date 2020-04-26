package com.example.advanced_chrono2.presenter

import com.example.advanced_chrono2.model.ActivityData
import com.example.advanced_chrono2.model.DatabaseHelper
import com.example.advanced_chrono2.view.fragments.IHomeView

class HomePresenter(val fragment: IHomeView) : IHomePresenter
{

    override fun onViewCreated()
    {
        //retrive data from model and set up the spinner.
        fragment.setUpSpinnerView(DatabaseHelper.getAllActivitiesName())
    }

    //Add new activity from model. It creates a new ActividyData provided a string
    override fun addNewActivity(activityName: String)
    {
        if (activityName.isNotEmpty())
        {
            DatabaseHelper.addActivity(ActivityData(activityName, ArrayList()))
            fragment.updateActivitiesList()
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