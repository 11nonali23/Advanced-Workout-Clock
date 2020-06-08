package com.andrea.advanced_workout_clock.view

import android.content.Context
import android.graphics.DashPathEffect
import androidx.core.content.ContextCompat
import com.andrea.advanced_workout_clock.R
import com.andrea.advanced_workout_clock.model.ActivityTiming
import com.andrea.advanced_workout_clock.view.custom_adapters.ChartListAdapter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

object ChartManager
{
    //return the chart initialized, or null if there are no timings
    fun initChart(
        timings: ArrayList<ActivityTiming>?,
        viewHolder: ChartListAdapter.ChartHolder,
        viewContext: Context
    )
    {
        if (timings == null) return

        viewHolder.chart.setTouchEnabled(true)
        viewHolder.chart.setPinchZoom(true)

        val entries = createEntries(timings)
        val data = initDataSet(viewHolder, entries, viewContext)
        setChartStyle(viewHolder.chart)
        viewHolder.chart.data = data
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
    private fun initDataSet(
        viewHolder: ChartListAdapter.ChartHolder,
        entries: ArrayList<Entry>,
        viewContext: Context
    ): LineData
    {
        viewHolder.dataSet = LineDataSet(entries, null)
        viewHolder.dataSet!!.setDrawIcons(false)
        viewHolder.dataSet!!.color = ContextCompat.getColor(viewContext, R.color.colorPrimary)
        viewHolder.dataSet!!.setCircleColor(ContextCompat.getColor(viewContext, R.color.colorSecondary))
        viewHolder.dataSet!!.fillColor = ContextCompat.getColor(viewContext, R.color.white)
        viewHolder.dataSet!!.lineWidth = 1f
        viewHolder.dataSet!!.circleRadius = 3f
        viewHolder.dataSet!!.setDrawCircleHole(false)
        viewHolder.dataSet!!.valueTextSize = 9f
        viewHolder.dataSet!!.setDrawFilled(true)
        viewHolder.dataSet!!.formLineWidth = 1f
        viewHolder.dataSet!!.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
        viewHolder.dataSet!!.formSize = 15f

        val dataSets: ArrayList<ILineDataSet> = ArrayList()
        dataSets.add(viewHolder.dataSet!!)
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

    fun paintNewTiming(holder: ChartListAdapter.ChartHolder, timing: Long)
    {
        holder.dataSet!!.addEntry(
            Entry(
                (holder.dataSet!!.entryCount + 1).toFloat(),
                timing.toFloat()
            )
        )
        holder.chart.data.notifyDataChanged()
        holder.chart.notifyDataSetChanged()
        holder.chart.invalidate()
    }

}