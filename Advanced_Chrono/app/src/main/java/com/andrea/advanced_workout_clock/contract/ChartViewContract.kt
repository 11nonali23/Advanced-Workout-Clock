package com.andrea.advanced_workout_clock.contract

import com.andrea.advanced_workout_clock.model.ActivityTiming



interface ChartViewContract
{
    interface IChartView
    {
        fun setUpView()

        fun addChartView(position: Int)

        fun deleteChartView(position: Int)

    }

    interface IChartPresenter
    {

        fun onCreateSetUp()

        //returns null if there are not new data available
        fun getNewData(): HashMap<Int, ArrayList<ActivityTiming>>?
        {
            if (IChartObserver.newTimingsCached.size > 0)
                return IChartObserver.newTimingsCached
            return null
        }

        fun addChartToView()

        fun deleteChartFromView(position: Int)

    }

    /*This *observer listens for new timings of chronometers and has a local caching mechanism to store them TODO viewModel
    *Ith also listens for new activity added or deleted
    */
    interface IChartObserver
    {
        companion object
        {
            //map containing the values of the new data. Int is the Id of an activity
            val newTimingsCached: HashMap<Int, ArrayList<ActivityTiming>> = HashMap()
        }

        fun cacheNewTiming(chronometerActivityId: Int, activityTiming: ActivityTiming)
        {
            if (newTimingsCached[chronometerActivityId] == null)
                newTimingsCached[chronometerActivityId] = arrayListOf(activityTiming)
            else
                newTimingsCached[chronometerActivityId]!!.add(activityTiming)
        }

        fun deleteCachedTiming(chronometerActivityId: Int, activityTiming: ActivityTiming)
        {
            newTimingsCached[chronometerActivityId]?.filterNot { it -> it.id == activityTiming.id }
        }

        fun deleteCachedActivity(chronometerActivityId: Int) = newTimingsCached.remove(chronometerActivityId)

        fun notifyNewActivity()

        fun notifyDeletedActivity(position: Int)
    }
}