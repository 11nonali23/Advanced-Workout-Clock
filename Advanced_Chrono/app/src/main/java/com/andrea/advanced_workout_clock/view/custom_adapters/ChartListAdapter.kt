package com.andrea.advanced_workout_clock.view.custom_adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andrea.advanced_workout_clock.R
import com.andrea.advanced_workout_clock.contract.ChartViewContract
import com.andrea.advanced_workout_clock.model.ActivityTiming
import com.andrea.advanced_workout_clock.view.ChartManager
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineDataSet

import com.andrea.advanced_workout_clock.contract.ChronometerContract.IChronometerPresenter.Companion.activities

class ChartListAdapter(val parentRecyclerView: RecyclerView) : RecyclerView.Adapter<ChartListAdapter.ChartHolder>()
{
    private lateinit var parentContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChartHolder
    {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.chart_view,parent,false)
        parentContext = parent.context
        return ChartHolder(v)
    }

    override fun getItemCount(): Int = activities.size


    override fun onBindViewHolder(holder: ChartHolder, position: Int)
    {
        holder.activityName.text = activities[position].name
        ChartManager.initChart(activities[position].timings_timestamp, holder, parentContext)

    }

    fun paintNewData(cachedTimings: HashMap<Int, ArrayList<ActivityTiming>>)
    {
        var currViewHolder: ChartListAdapter.ChartHolder?
        for ((key, value) in cachedTimings) {
            currViewHolder = parentRecyclerView.findViewHolderForAdapterPosition(key) as ChartHolder?
            if (currViewHolder != null)
                value.forEach { ChartManager.paintNewTiming(currViewHolder, it.timing) }
        }
    }

    inner class ChartHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        val activityName: TextView = itemView.findViewById(R.id.activity_name)
        val chart: LineChart = itemView.findViewById(R.id.chart)
        var dataSet: LineDataSet? = null
    }
}