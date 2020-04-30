package com.example.advanced_chrono2.shared_preferences


import android.content.Context
import androidx.preference.PreferenceManager
import com.example.advanced_chrono2.TimerActivity
import com.example.advanced_chrono2.model.TimerItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.LinkedList

//This class will use shared preferences to store the timer data

class TimerPrefUtilsManager {

    companion object
    {

//USEFUL VARIABLES ---------------------------------------------------------------------------------------------------------------------------------------------------------------

        //This variable helps me to save the LinkedList of TimerItemDatas into Json object
        private val gson = Gson()

        // the LinkedList of TimerItemDatas
        private val type: Type = object : TypeToken<LinkedList<TimerItem>>() {}.type

//USEFUL VARIABLES ----------------------------------------------------------------------------------------------------------------------------------------------------------------

        fun clearAll(context: Context)
        {
            PreferenceManager.getDefaultSharedPreferences(context).edit().clear().apply()
        }

        fun getTimerLength(context: Context): Int {return 1}


        //setting and getting timer lenght of the previous timer
        private const val PREVIOUS_TIMER_LENGHT_SECONDS =  "com.timer.previous_timer_length_seconds"

        fun getPreviousTimerLenghtSeconds(context: Context): Long
        {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(PREVIOUS_TIMER_LENGHT_SECONDS, 0)
        }

        fun setPreviousTimerLengthSeconds(context: Context, seconds: Long)
        {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(PREVIOUS_TIMER_LENGHT_SECONDS, seconds)
            editor.apply()
        }


        //setting and getting the state of the timer
        private const val TIMER_STATE_ID = "com.timer.timer_state"

        fun getTimerState(context: Context): TimerActivity.TimerState{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val ordinal = preferences.getInt(TIMER_STATE_ID, 0)
            return TimerActivity.TimerState.values()[ordinal]
        }

        fun setTimerState(context: Context, state: TimerActivity.TimerState){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            val ordinal = state.ordinal
            editor.putInt(TIMER_STATE_ID, ordinal)
            editor.apply()
        }


        //setting and getting the seconds remaining on the timer
        private const val SECONDS_REMAINING_ID = "com.timer.seconds_remaining"

        fun getSecondsRemaining(context: Context): Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(SECONDS_REMAINING_ID, 0)
        }

        fun setSecondsRemaining(context: Context, seconds: Long){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(SECONDS_REMAINING_ID, seconds)
            editor.apply()
        }


        //getting and setting the list of timer items using Gson
        //TODO remember it returns null as default value
        private const val TIMER_ITEMS_LIST_ID = "com.timer.item_list"

        fun getTimerItemDataList(context: Context): LinkedList<TimerItem>{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)

            val json = preferences.getString(TIMER_ITEMS_LIST_ID, null)

            if (json == null)
                return LinkedList<TimerItem>()

            val timerItemList: LinkedList<TimerItem> = gson.fromJson(json, type)
            return timerItemList
        }

        fun setTimerItemDatasList(context: Context, timerItemList: LinkedList<TimerItem>){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()

            editor.putString(TIMER_ITEMS_LIST_ID, gson.toJson(timerItemList))
            editor.apply()

        }


        //setting and getting boolean var that check if the timer list is started
        private const val IS_TIMER_LIST_STARTED_ID = "com.timer.is_list_started"

        fun getIsTimerListStarted(context: Context): Boolean{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getBoolean(IS_TIMER_LIST_STARTED_ID, false)
        }

        fun setIsTimerListStarted(context: Context, isListStarted: Boolean){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putBoolean(IS_TIMER_LIST_STARTED_ID, isListStarted)
            editor.apply()
        }

        //setting and boolean var that check if the workout is started
        private const val IS_WORKOUT_ID = "com.timer.is_workout"

        fun getIsWorkout(context: Context): Boolean{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getBoolean(IS_WORKOUT_ID, true)
        }

        fun setIsWorkout(context: Context, isWorkout: Boolean){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putBoolean(IS_WORKOUT_ID, isWorkout)
            editor.apply()
        }

    }

}
