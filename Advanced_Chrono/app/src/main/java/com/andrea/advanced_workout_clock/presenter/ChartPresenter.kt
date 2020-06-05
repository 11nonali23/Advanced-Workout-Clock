package com.andrea.advanced_workout_clock.presenter

import android.content.Context
import com.andrea.advanced_workout_clock.contract.ChartViewContract.IChartView
import com.andrea.advanced_workout_clock.contract.ChartViewContract.IChartPresenter
import com.andrea.advanced_workout_clock.contract.ChronometerContract.IChronometerModel
import com.andrea.advanced_workout_clock.model.ChronometerActivitiesDB
import com.andrea.advanced_workout_clock.model.ChronometerActivity


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
            view.setUpView(model!!.getAllActivities() as ArrayList<ChronometerActivity>)
        }
    }

    override fun addChartToView() {
        TODO("Not yet implemented")
    }

    override fun deleteChartFromView(position: Int) {
        TODO("Not yet implemented")
    }


}