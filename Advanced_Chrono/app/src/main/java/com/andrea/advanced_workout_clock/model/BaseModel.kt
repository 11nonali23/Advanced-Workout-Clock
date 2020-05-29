package com.andrea.advanced_workout_clock.model

interface BaseModel
{
    fun getAllActivities(): List<Activity>

    fun addNewActivity(name: String): Activity?

    fun deleteActivity(activityName: String): Boolean

    fun getNewMaxId(): Int
}