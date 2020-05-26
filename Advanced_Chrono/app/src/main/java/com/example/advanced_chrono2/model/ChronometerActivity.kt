package com.example.advanced_chrono2.model

import java.util.*
import kotlin.collections.ArrayList

//first element of pair is timing and second is timestamp

class ChronometerActivity(id: Int, name: String, var timings_timestamp: ArrayList<ActivityTiming>?) : Activity(id, name)