package com.andrea.advanced_workout_clock.view

import android.util.DisplayMetrics
import android.util.Log
import kotlin.math.pow
import kotlin.math.sqrt

//this object is used to determine if the layout shuld be full or light for small devices

object ScreenInchesDeterminator
{
    private const val ADMITTED_INCHES_FULL_LAYOUT = 4.985714285714286 //empiric value

    //returns true if full layout display is admitted
    fun canDisplayFullLayout(dm: DisplayMetrics): Boolean
    {
        val density = dm.density * 160.toDouble()
        val x = (dm.widthPixels / density).pow(2.0)
        val y = (dm.heightPixels / density).pow(2.0)
        val inches = sqrt(x + y)

        return inches > ADMITTED_INCHES_FULL_LAYOUT
    }

}