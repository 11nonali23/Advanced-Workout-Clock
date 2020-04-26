package com.example.advanced_chrono2.view.fragments

import com.example.advanced_chrono2.model.ActivityData

interface IHomeView
{
    fun displayResult(result: String)

    fun setUpSpinnerView(activitiesName: List<String>)

    fun updateActivitiesList()

}