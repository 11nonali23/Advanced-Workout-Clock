package com.andrea.advanced_workout_clock.view.custom_adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andrea.advanced_workout_clock.R
import com.andrea.advanced_workout_clock.model.ChronometerActivity
import com.andrea.advanced_workout_clock.view.ChartManager
import com.github.mikephil.charting.charts.LineChart


//TODO decide where to pick the data!!!

class ChartListAdapter(private val activitiesList: ArrayList<ChronometerActivity>): RecyclerView.Adapter<ChartListAdapter.ChartHolder>()
{
    private lateinit var parentContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChartHolder
    {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.chart_view,parent,false)
        parentContext = parent.context
        return ChartHolder(v)
    }

    override fun getItemCount(): Int = activitiesList.size


    override fun onBindViewHolder(holder: ChartHolder, position: Int)
    {
        holder.activityName.text = activitiesList[position].name
        ChartManager.initChart(activitiesList[position].timings_timestamp, holder.chart, parentContext)

    }

    inner class ChartHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        val activityName: TextView = itemView.findViewById(R.id.activity_name)
        val chart: LineChart = itemView.findViewById(R.id.chart)
    }
}