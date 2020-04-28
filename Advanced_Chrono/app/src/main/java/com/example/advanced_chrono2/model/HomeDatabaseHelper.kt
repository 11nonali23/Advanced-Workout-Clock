package com.example.advanced_chrono2.model

/* This class is a singleton one ---> https://blog.mindorks.com/how-to-create-a-singleton-class-in-kotlin*/

//Fake database. One true will be added sooon
object HomeDatabaseHelper {

    private val activities: ArrayList<ChronoActivityData> = ArrayList()
    private val activitiesName: ArrayList<String> = ArrayList()   //List of activity names to pass to the activity

    init {
        activities.add(ChronoActivityData("Cubo Rubik", ArrayList()))
        activities.add(ChronoActivityData("Cubo Rubik1", ArrayList()))
        activities.add(ChronoActivityData("Cubo Rubik2", ArrayList()))
        activities.add(ChronoActivityData("Cubo Rubik3", ArrayList()))
        activities.add(ChronoActivityData("Cubo Rubik4", ArrayList()))
        activities.add(ChronoActivityData("Cubo Rubik5", ArrayList()))

        activities.forEach { activitiesName.add(it.name) }
    }

    fun addActivity(activityData: ChronoActivityData): Boolean
    {
        activities.add(activityData)
        activitiesName.add(activityData.name)

        return true
    }

    // The model filter ActivityData items to give view only the name
    fun getAllActivitiesName(): List<String>
    {
        return activitiesName
    }
}