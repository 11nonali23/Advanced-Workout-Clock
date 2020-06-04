package com.andrea.advanced_workout_clock.view.custom_adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andrea.advanced_workout_clock.R
import com.github.mikephil.charting.charts.LineChart


//TODO decide where to pick the data!!!

class ChartListAdapter(): RecyclerView.Adapter<ChartListAdapter.ChartHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChartHolder
    {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.chart_view,parent,false)
        return ChartHolder(v)
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ChartHolder, position: Int) {
        TODO("Not yet implemented")
    }

    inner class ChartHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        val activityName: TextView = itemView.findViewById(R.id.activity_name)
        val chart: LineChart = itemView.findViewById(R.id.chart)
    }
}