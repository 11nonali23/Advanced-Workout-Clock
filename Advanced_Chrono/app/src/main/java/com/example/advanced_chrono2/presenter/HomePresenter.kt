package com.example.advanced_chrono2.presenter

import android.content.Context
import com.example.advanced_chrono2.contract.HomeChronometerContract
import com.example.advanced_chrono2.model.ChronoActivity
import com.example.advanced_chrono2.model.ChronometerActivitiesDatabase
import java.util.*
import kotlin.collections.ArrayList

class HomePresenter(val view: HomeChronometerContract.IHomeChronometerView) : HomeChronometerContract.IHomePresenter
{
    private var model: ChronometerActivitiesDatabase? = null
    private var activitiesName: ArrayList<ChronoActivity>? = null  //List of activity names to pass to the activity

    companion object
    {
        private const val INTERNAL_ERROR = "Internal Error: activity not added"

        private const val ADD_ACTIVITY_SUCCES = "Succesfully added!"
        private const val ADD_ACTIVITY_DB_ERROR = "Error: this name already exists!"
        private const val ADD_EMPTY_ACTIVITY = "Error: activity can' t be empty"

        private const val DEL_ACTIVITY_SUCCES = "Succesfully deleted!"

        private const val SAVE_TIMING_SUCCES = "Timing added succesfully"
    }


    override fun onViewCreated(context: Context?)
    {
        if(context != null)
        {
            model = ChronometerActivitiesDatabase(context)
            activitiesName = model!!.getAllActivities()
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
                val newActivity = model!!.addNewActivity(activityNameTrimmed)

                if (newActivity != null)
                {
                    activitiesName?.add(newActivity)
                    view.displayResult(ADD_ACTIVITY_SUCCES)
                    view.updateActivitiesList()
                    view.setNewItemAsSelected()
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
                activitiesName?.removeAll{it.name == activityName}
                view.displayResult(DEL_ACTIVITY_SUCCES)
                view.updateActivitiesList()
                return
            }
        }
        view.displayResult(INTERNAL_ERROR)
    }

    //saving a tempo is not an error than acan occur by the user input. It is only internal
    override fun saveTempo(tempo: Long, activityId: Int)
    {
        if (model != null)
        {
            if(model!!.addNewTiming(
                tempo,
                GregorianCalendar.getInstance().timeInMillis,
                activityId
                ))
            {
                view.displayResult(SAVE_TIMING_SUCCES)
                return
            }
        }
        view.displayResult(INTERNAL_ERROR)
    }
}