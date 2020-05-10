package com.example.advanced_chrono2.presenter

import android.content.Context
import com.example.advanced_chrono2.R
import com.example.advanced_chrono2.contract.HomeChronometerContract
import com.example.advanced_chrono2.model.ChronoActivity
import com.example.advanced_chrono2.model.ChronometerActivitiesDB
import java.util.*
import kotlin.collections.ArrayList

class HomePresenter(val view: HomeChronometerContract.IHomeChronometerView) : HomeChronometerContract.IHomePresenter
{
    private var model: ChronometerActivitiesDB? = null
    private var activities: ArrayList<ChronoActivity>? = null  //List of activity names to pass to the activity

    companion object
    {
        private const val TAG = "HOME PRESENTER"

    }

    private lateinit var viewContext: Context


    override fun onViewCreated(context: Context?)
    {
        if(context != null)
        {
            viewContext = context

            model = ChronometerActivitiesDB(context)
            activities = model!!.getAllActivities()
            view.setUpSpinnerView(this.activities!!)
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
                    activities?.add(newActivity)
                    view.displayResult(viewContext.getString(R.string.ADD_ACTIVITY_SUCCESS))
                    view.updateActivitiesView()
                    view.setNewItemAsSelected()
                    return
                }
                else
                {
                    view.displayResult(viewContext.getString(R.string.ADD_ACTIVITY_DB_ERROR))
                    return
                }
            }
            else
            {
                view.displayResult(viewContext.getString(R.string.ADD_EMPTY_ACTIVITY))
                return
            }
        }
        view.displayResult(viewContext.getString(R.string.INTERNAL_ERROR))
    }

    override fun deleteActivity(activityName: String)
    {
        /*If an activity isn't deleted it can't be an error of the user:
        the only deletable activities are the one already added!*/
        if (model != null)
        {
            if(model!!.deleteActivity(activityName))
            {
                activities?.removeAll{it.name == activityName}
                view.displayResult(viewContext.getString(R.string.DEL_ACTIVITY_SUCCESS))
                view.updateActivitiesView()
                return
            }
        }
        view.displayResult(viewContext.getString(R.string.INTERNAL_ERROR))
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
                view.displayResult(viewContext.getString(R.string.SAVE_TIMING_SUCCES))
                return
            }
        }
        view.displayResult(viewContext.getString(R.string.INTERNAL_ERROR))
    }
}