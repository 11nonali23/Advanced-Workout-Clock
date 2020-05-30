package com.andrea.advanced_workout_clock.contract

import android.content.Context
import com.andrea.advanced_workout_clock.R
import com.andrea.advanced_workout_clock.model.BaseModel
import com.andrea.advanced_workout_clock.model.TimerActivity
import com.andrea.advanced_workout_clock.model.TimerItem

//This interface define a contract between the timer activities view and the presenter related to it


interface TimerActivitiesContract
{

    interface ITimerActivitiesModel : BaseModel
    {
        fun getAllTimerItems(activityId: Int): ArrayList<TimerItem>

        fun addTimerItem(parentId: Int, workoutSeconds: Int, restSeconds: Int): TimerItem?

        fun delTimerItem(activityId: Int, itemId: Int): Boolean

        fun getNewMaxTimerItemId(): Int
    }


    interface ITimerActivitiesView
    {
        //Logo accessible for every class that needs it
        companion object { const val itemLogo = R.mipmap.ic_workout_logo_round }

        fun setUpView()

        fun isViewSetUp(): Boolean

        //The next four functions are MANDATORY to use when the data set of activities or items of an activity is changed
        fun activitiesDataSetChanged()

        fun activityRemovedFromDataSet(position: Int)

        fun itemDataSetChanged()

        fun itemRemovedFromDataSet(position: Int)

        fun changeAddItemButtonVisibility(isVisible: Boolean)
        //--------------------------------------------------------------------------------------------------------------

        fun displayResult(message: String)

        fun lendContext(): Context?
    }

    interface ITimerActivitiesPresenter
    {
        companion object
        {
            //the presenter has a unique list list of all the activities accessible for View and Adapters as a model
            var activitiesList: ArrayList<TimerActivity> = ArrayList()

            var currentActivityPosition: Int? = null
        }

        //first steps to do when a view is created
        fun onViewCreated(context: Context?)

        fun addNewActivity(activityName: String)

        fun deleteActivity(position: Int?): Boolean //true if item are deleted


        fun addNewTimerItem(selectedActivityPosition: Int?,
                            workoutMinutesText: String,
                            workoutSecondsText: String,
                            restMinutesText: String,
                            restSecondsText: String)

        fun deleteItem(selectedActivityPosition: Int?, itemPosition: Int)


        fun onSelectedActivityChange(position: Int?)

        //returns name of activity and its items
        fun onActivityStart(position: Int?): Pair<String, ArrayList<TimerItem>>?
    }
}