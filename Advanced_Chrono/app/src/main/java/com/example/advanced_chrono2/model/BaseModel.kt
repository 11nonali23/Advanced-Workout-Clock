package com.example.advanced_chrono2.model

import android.database.sqlite.SQLiteDatabase

interface BaseModel
{
    fun getAllActivities(): List<Activity>

    fun addNewActivity(name: String): Activity?

    fun deleteActivity(activityName: String): Boolean

    fun getNewMaxId(writableDatabase: SQLiteDatabase): Int
}