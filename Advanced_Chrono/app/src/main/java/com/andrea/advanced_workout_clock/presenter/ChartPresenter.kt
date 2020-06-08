package com.andrea.advanced_workout_clock.presenter

import android.content.Context
import com.andrea.advanced_workout_clock.contract.ChartViewContract.IChartPresenter
import com.andrea.advanced_workout_clock.contract.ChartViewContract.IChartView
import com.andrea.advanced_workout_clock.contract.ChronometerContract.IChronometerModel
import com.andrea.advanced_workout_clock.model.ActivityTiming
import com.andrea.advanced_workout_clock.model.ChronometerActivitiesDB
import com.andrea.advanced_workout_clock.model.ChronometerActivity
import com.andrea.advanced_workout_clock.observer.ChartObserver

import com.andrea.advanced_workout_clock.contract.ChartViewContract.IChartObserver.CacheManager
import com.andrea.advanced_workout_clock.contract.ChronometerContract.IChronometerPresenter.Companion.activities
import com.andrea.advanced_workout_clock.contract.ChartViewContract.IChartPresenter.Companion.observer


class ChartPresenter(val view: IChartView): IChartPresenter
{
    private lateinit var viewContext: Context
    private var model: IChronometerModel? = null //I need the database that stores all the timings for chronometer

    @Suppress("UNCHECKED_CAST")
    override fun onViewCreated(context: Context?)
    {
        if (context != null)
        {
            viewContext = context
            model = ChronometerActivitiesDB(viewContext)
            observer = ChartObserver(this)
            view.setUpView(model!!.getAllActivities() as ArrayList<ChronometerActivity>)
        }
    }

    override fun getCachedData() {
        //If there are new cahced timings I have to paint them on the view and delete them on the observer
        val newTimingsCached = CacheManager.getNewTimings()
        if (newTimingsCached != null)
        {
            view.paintCachedTimings(convertIdKeyToPosition(newTimingsCached))
            //clear cache because I have fetched the new timings
            CacheManager.clearCache()
        }
    }

    override fun addChartToView()
    {
        //the new activity is at the last position
        view.addChartView(activities.size - 1)
    }

    override fun deleteChartFromView(position: Int)
    {
        TODO("Not yet implemented")
    }


    private fun convertIdKeyToPosition(newTimingsCached: HashMap<Int, ArrayList<ActivityTiming>>): HashMap<Int, ArrayList<ActivityTiming>>
    {
        val positionMap = HashMap<Int, ArrayList<ActivityTiming>>()
        var currPos: Int?
        for ((key, value) in newTimingsCached) {
            currPos = findPosition(key)
            if (currPos != null)
                positionMap[currPos] = value
        }
        return positionMap
    }

    private fun findPosition(activityId: Int): Int?
    {
        activities.forEachIndexed { idx, it -> if (it.id == activityId) return idx}
        return null
    }


}