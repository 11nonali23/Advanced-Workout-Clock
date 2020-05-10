package com.example.advanced_chrono2.model

import java.io.Serializable

class TimerItem(
    val id: Int,
    val imageResource: Int,
    val workoutSeconds: Int,
    val restSeconds: Int

) : Serializable