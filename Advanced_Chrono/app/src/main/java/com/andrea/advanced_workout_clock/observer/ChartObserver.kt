package com.andrea.advanced_workout_clock.observer

import com.andrea.advanced_workout_clock.contract.ChartViewContract.IChartPresenter
import com.andrea.advanced_workout_clock.contract.ChartViewContract.IChartObserver


class ChartObserver(val presenter: IChartPresenter): IChartObserver
{
    override fun notifyActivityAdded()
    {
        presenter.addChartToView()
    }

    override fun notifyDeletedActivity(activityPosition: Int)
    {
        presenter.deleteChartFromView(activityPosition)
    }
}