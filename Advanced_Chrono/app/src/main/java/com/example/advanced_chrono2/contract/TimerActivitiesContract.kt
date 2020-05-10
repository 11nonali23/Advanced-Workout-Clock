package com.example.advanced_chrono2.contract

import android.content.Context
import com.example.advanced_chrono2.R
import com.example.advanced_chrono2.model.BaseModel
import com.example.advanced_chrono2.model.TimerActivity
import com.example.advanced_chrono2.model.TimerItem

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
        companion object { const val itemLogo = R.drawable.ic_timer_black }

        fun setUpView()

        fun isViewSetUp(): Boolean

        fun changeTimerItemListView(position: Int?)

        //The next for functions are MANDATORY to use when the data set of activities or items of an activity is changed
        fun activitiesDataSetChanged()

        fun activityRemovedFromDataSet(position: Int)

        fun itemDataSetChanged()

        fun itemRemovedFromDataSet(position: Int)
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
        }

        //first steps to do when a view is created
        fun onViewCreated(context: Context?)

        fun addNewActivity(activityName: String)

        fun deleteActivity(position: Int?)


        fun addNewTimerItem(selectedActivityPosition: Int?,
                            workoutMinutesText: String,
                            workoutSecondsText: String,
                            restMinutesText: String,
                            restSecondsText: String)

        fun deleteItem(selectedActivityPosition: Int?, itemPosition: Int)


        fun onSelectedActivityChange(position: Int?)

        fun onActivityStart(position: Int?): ArrayList<TimerItem>?
    }
}