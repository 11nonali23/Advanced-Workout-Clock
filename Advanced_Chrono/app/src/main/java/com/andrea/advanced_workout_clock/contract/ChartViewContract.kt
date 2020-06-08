package com.andrea.advanced_workout_clock.contract

import android.content.Context
import com.andrea.advanced_workout_clock.model.ActivityTiming
import com.andrea.advanced_workout_clock.model.ChronometerActivity

interface ChartViewContract
{
    interface IChartView
    {
        fun setUpView(activities: ArrayList<ChronometerActivity>)

        //Int key of te hashMap will be converted by the presenter to the position and not to the ID
        fun paintCachedTimings(cachedTimings: HashMap<Int, ArrayList<ActivityTiming>>)

        fun addChartView(position: Int)

        fun deleteChartView(position: Int)
    }

    interface IChartPresenter
    {
        companion object {  var observer: IChartObserver? = null    }

        fun onViewCreated(context: Context?)

        //returns null if there are not new data available
        fun getCachedData()

        fun addChartToView()

        fun deleteChartFromView(position: Int)

    }

    /*This *observer listens for new timings of chronometers
    *Ith also listens for new activity added or deleted
    */
    interface IChartObserver
    {
        fun notifyActivityAdded()

        fun notifyDeletedActivity(activityPosition: Int)
    }

    //caching mechanism to store the new timings. Presenter of chronometer and chart will use it
    object CacheManager
    {
        //map containing the values of the new data. Int is the position of an activity
        private val newTimingsCached: HashMap<Int, ArrayList<ActivityTiming>> = HashMap()

        fun cacheNewTiming(chronometerActivityId: Int?, activityTiming: ActivityTiming?)
        {
            if (activityTiming == null || chronometerActivityId == null) return

            if (newTimingsCached[chronometerActivityId] == null)
                newTimingsCached[chronometerActivityId] = arrayListOf(activityTiming)
            else
                newTimingsCached[chronometerActivityId]!!.add(activityTiming)

            // Works fine: Log.e("Observer", "New Timing Cached => ${newTimingsCached[chronometerActivityId]?.get(0)?.timing}")
        }

        fun deleteCachedTiming(chronometerActivityId: Int, activityTimingId: Int)
        {
            if (newTimingsCached[chronometerActivityId]!= null)
                newTimingsCached[chronometerActivityId] = newTimingsCached[chronometerActivityId]!!.filterNot { it.id == activityTimingId } as java.util.ArrayList<ActivityTiming>

        }

        fun clearCache() = newTimingsCached.clear()

        fun getNewTimings(): HashMap<Int, ArrayList<ActivityTiming>>? =
            if (newTimingsCached.size > 0)
                newTimingsCached
            else
                null
    }
}