package com.example.advanced_chrono2.presenter

import android.content.Context
import com.example.advanced_chrono2.contract.HomeChronometerContract
import com.example.advanced_chrono2.model.ChronometerActivitiesDatabase

class HomePresenter(val view: HomeChronometerContract.IHomeChronometerView) : HomeChronometerContract.IHomePresenter
{
    private var model: ChronometerActivitiesDatabase? = null
    private var activitiesName: ArrayList<String>? = null  //List of activity names to pass to the activity

    companion object
    {
        private const val INTERNAL_ERROR = "Internal Error: activity not added"
        private const val ADD_ACTIVITY_SUCCES = "Succesfully added!"
        private const val ADD_ACTIVITY_DB_ERROR = "Error: this name already exists!"
        private const val ADD_EMPTY_ACTIVITY = "Error: activity can' t be empty"

        private const val DEL_ACTIVITY_SUCCES = "Succesfully deleted!"
    }


    override fun onViewCreated(context: Context?)
    {
        if(context != null)
        {
            model = ChronometerActivitiesDatabase(context)
            activitiesName = model!!.getAllActivitiesName()
            view.setUpSpinnerView(this.activitiesName!!)
        }
    }

    //Add new activity from model. It creates a new ActividyData provided a string and pass to the database
    //If the database add it successfully it will be addedd also in the local list
    override fun addNewActivity(activityName: String)
    {
        if (model != null)
        {
            val activityNameTrimmed: String = activityName.trimEnd().trimStart() //trimeEnd and trimStart remove all special character at the end and the start
            if (activityNameTrimmed.isNotEmpty())
            {
                if (model!!.addNewActivity(activityNameTrimmed))
                {
                    activitiesName?.add(activityNameTrimmed)
                    view.displayResult(ADD_ACTIVITY_SUCCES)
                    view.updateActivitiesList()
                    return
                }
                else
                {
                    view.displayResult(ADD_ACTIVITY_DB_ERROR)
                    return
                }
            }
            else
            {
                view.displayResult(ADD_EMPTY_ACTIVITY)
                return
            }
        }
        view.displayResult(INTERNAL_ERROR)
    }

    override fun deleteActivity(activityName: String)
    {
        /*If an activity isn't deleted it can't be an error of the user:
        the only deletable activities are the one already added!*/
        if (model != null)
        {
            if(model!!.deleteActivity(activityName))
            {
                activitiesName?.remove(activityName)
                view.displayResult(DEL_ACTIVITY_SUCCES)
                view.updateActivitiesList()
                return
            }
        }
        view.displayResult(INTERNAL_ERROR)
    }

    override fun saveTempo(tempo: Long)
    {
        //model try to save tempo and return a code
    }
}