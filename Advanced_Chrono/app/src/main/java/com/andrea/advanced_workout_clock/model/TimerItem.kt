package com.andrea.advanced_workout_clock.model

import java.io.Serializable

class TimerItem(
    val id: Int,
    val imageResource: Int,
    val workoutSeconds: Int,
    val restSeconds: Int

) : Serializable