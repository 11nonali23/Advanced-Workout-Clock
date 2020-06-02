package com.andrea.advanced_workout_clock.view

import android.util.DisplayMetrics
import kotlin.math.pow
import kotlin.math.sqrt

//this object is used to determine if the layout shuld be full or light for small devices

object ScreenInchesDeterminator
{
    const val ADMITTED_INCHES_FULL_LAYOUT = 5.342670889302929 //empiric value

    //returns true if full layout display is admitted
    fun canDisplayFullLayout(dm: DisplayMetrics): Boolean
    {
        val density = dm.density * 160.toDouble()
        val x = (dm.widthPixels / density).pow(2.0)
        val y = (dm.heightPixels / density).pow(2.0)

        return sqrt(x + y) > ADMITTED_INCHES_FULL_LAYOUT
    }

}