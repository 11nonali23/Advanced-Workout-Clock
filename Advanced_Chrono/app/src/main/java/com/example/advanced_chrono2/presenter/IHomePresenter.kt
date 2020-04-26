package com.example.advanced_chrono2.presenter

import com.example.advanced_chrono2.model.ActivityData

//View handles the logic of the chronometer because the chronometer is strictly related to her.
//This presenter handles only the beahaviours of adding, deleting and saving tempo on model

interface IHomePresenter
{
    fun onViewCreated()

    fun addNewActivity(activityName: String)

    fun deleteActivity(activityName: String)

    fun saveTempo(tempo: Long)
}

