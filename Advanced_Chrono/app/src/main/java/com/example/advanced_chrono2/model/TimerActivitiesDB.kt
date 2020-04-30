package com.example.advanced_chrono2.model

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.advanced_chrono2.contract.TimerActivitiesContract

class TimerActivitiesDB(context: Context) :
    SQLiteOpenHelper(context,
        DATABASE_NAME, null,
        DATABASE_VERSION
    ),
    TimerActivitiesContract.ITimerActivitiesModel
{
    companion object
    {
        private const val TAG = "TIMER_ACTIVITIES DB"

        private var DATABASE_VERSION = 1
        private const val DATABASE_NAME = "timerActivitiesDb"

        //Name of activity table
        private const val ACTIVITY_TABLE_NAME = "activity"
        //Keys of activity
        private const val KEY_ACTIVITY_ID = "id"/*The protection from id overflow is intrinsic in the application. It is more than highly improbable that someone creates that much activities*/
        private const val KEY_NAME = "name"

        //name of timing table
        private const val ITEM_TABLE_NAME= "item"
        //Keys of timing
        private const val KEY_ITEM_ID = "time"
        private const val KEY_WORKOUT_SEC = "workout_sec"
        private const val KEY_REST_SEC = "rest_sec"
        private const val KEY_FOREIGN_ACTIVITY_ID = "activity_id"
        private const val ON_UPDTAE_ON_DELETE_ITEM = "ON DELETE CASCADE ON UPDATE CASCADE"

        //create activity entries. Avoid using autoincrement for bad performances:
        private const val SQL_CREATE_ACTIVITY_ENTRIES =
            "CREATE TABLE $ACTIVITY_TABLE_NAME(" +
                    "$KEY_ACTIVITY_ID INTEGER PRIMARY KEY," +
                    "$KEY_NAME TEXT UNIQUE NOT NULL);"

        //create activity entries. Avoid using autoincrement fro bad performances:
        private const val SQL_CREATE_ITEM_ENTRIES =
            "CREATE TABLE $ITEM_TABLE_NAME(" +
                    "$KEY_ITEM_ID INTEGER PRIMARY KEY," +
                    "$KEY_NAME TEXT UNIQUE NOT NULL);"
    }



    override fun onCreate(db: SQLiteDatabase?) {
        TODO("Not yet implemented")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    override fun getAllActivities(): ArrayList<TimerActivity> {
        TODO("Not yet implemented")
    }

    override fun addNewActivity(name: String): Activity {
        TODO("Not yet implemented")
    }

    override fun deleteActivity(activityName: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun getNewMaxId(writableDatabase: SQLiteDatabase): Int {
        TODO("Not yet implemented")
    }

    override fun addTimerItem(timerItemData: TimerItem) {
        TODO("Not yet implemented")
    }

    override fun delTimerItem(timerItemData: TimerItem) {
        TODO("Not yet implemented")
    }
}