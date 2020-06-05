package com.andrea.advanced_workout_clock.view

import android.content.Context
import android.graphics.Color
import android.graphics.DashPathEffect
import androidx.core.content.ContextCompat
import com.andrea.advanced_workout_clock.R
import com.andrea.advanced_workout_clock.model.ActivityTiming
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

object ChartManager
{
    private var chartList: ArrayList<LineChart> = ArrayList()
    private var dataSetList: ArrayList<LineDataSet> = ArrayList()

    //return the chart initialized, or null if there are no timings
    fun initChart(
        timings: ArrayList<ActivityTiming>?,
        chart: LineChart,
        viewContext: Context
    )
    {
        if (timings == null || timings.size == 0) return

        chart.setTouchEnabled(true)
        chart.setPinchZoom(true)

        val entries = createEntries(timings)
        val data = initDataset(entries, viewContext)
        setChartStyle(chart)
        chart.data = data
    }

    //Create the entries for the chart given the timings
    private fun createEntries(timings: ArrayList<ActivityTiming>): ArrayList<Entry>
    {
        val entries: ArrayList<Entry> = ArrayList()
        for (i in 0 until timings.size)
            entries.add(Entry(i.toFloat(), timings[i].timing.toFloat()))
        return entries
    }

    //Initialize the line data set and return the data the chart needs
    private fun initDataset(entries: ArrayList<Entry>, viewContext: Context): LineData
    {
        val lineDataSet =  LineDataSet(entries, null)
        lineDataSet.setDrawIcons(false)
        //TODO set a proper color
        lineDataSet.color = ContextCompat.getColor(viewContext, R.color.colorPrimary)
        lineDataSet.setCircleColor(ContextCompat.getColor(viewContext, R.color.colorSecondary))
        lineDataSet.fillColor = ContextCompat.getColor(viewContext, R.color.white)
        lineDataSet.lineWidth = 1f
        lineDataSet.circleRadius = 3f
        lineDataSet.setDrawCircleHole(false)
        lineDataSet.valueTextSize = 9f
        lineDataSet.setDrawFilled(true)
        lineDataSet.formLineWidth = 1f
        lineDataSet.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
        lineDataSet.formSize = 15f


        dataSetList.add(lineDataSet)

        val dataSets: ArrayList<ILineDataSet> = ArrayList()
        dataSets.add(lineDataSet)
        return LineData(dataSets)
    }

    private fun setChartStyle(chart: LineChart)
    {
        chart.description = null
        chart.legend.isEnabled = false
        chart.isHighlightPerDragEnabled = true
        chart.setDrawGridBackground(false)
        chart.setDrawBorders(false)
        chart.xAxis.setDrawGridLines(false)
        chart.xAxis.setDrawLabels(false)
        chart.xAxis.setDrawAxisLine(false);
        chart.axisLeft.setDrawGridLines(false)
        chart.axisLeft.setDrawLabels(false)
        chart.axisLeft.setDrawAxisLine(false)
        chart.axisRight.setDrawGridLines(false)
        chart.axisRight.setDrawLabels(false)
        chart.axisRight.setDrawAxisLine(false)
    }

    fun addValue(activityPosition: Int, timing: Long )
    {
        dataSetList[activityPosition].addEntry(Entry((dataSetList[activityPosition].entryCount + 1).toFloat(),timing.toFloat()))
    }
}