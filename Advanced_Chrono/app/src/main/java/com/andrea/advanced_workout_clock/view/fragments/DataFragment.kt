package com.andrea.advanced_workout_clock.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andrea.advanced_workout_clock.R
import com.andrea.advanced_workout_clock.contract.ChartViewContract
import com.andrea.advanced_workout_clock.model.ChronometerActivity
import com.andrea.advanced_workout_clock.presenter.ChartPresenter
import com.andrea.advanced_workout_clock.view.custom_adapters.ChartListAdapter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.fragment_data_.*

/**
 * Fragment that visualize data with Line Charts
 */

class DataFragment: Fragment(), ChartViewContract.IChartView {

    private val presenter: ChartViewContract.IChartPresenter = ChartPresenter(this)

    private lateinit var _chartList: RecyclerView
    private lateinit var chartListAdapter: ChartListAdapter

    companion object
    {
        private const val TAG = "DATA FRAGMENT CICLE"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_data_, container, false)
    }

    //This method shows when a new fragment is created
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        presenter.onViewCreated(this.context)
    }

    override fun onResume()
    {
        super.onResume()
        if (this.context != null)
            activity?.window?.navigationBarColor = ContextCompat.getColor(this.requireContext(), R.color.white)
    }

    override fun setUpView(activities: ArrayList<ChronometerActivity>)
    {
        //setting the recycler view adapter
        _chartList = chartList
        chartListAdapter = ChartListAdapter(activities)
        _chartList.layoutManager = LinearLayoutManager(this.context)
        _chartList.adapter = chartListAdapter
        _chartList.setHasFixedSize(true)
    }

    override fun addChartView(position: Int) {
       //TODo
    }

    override fun deleteChartView(position: Int) {
        //TODO
    }


}
