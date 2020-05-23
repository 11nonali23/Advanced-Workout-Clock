package com.example.advanced_chrono2.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.advanced_chrono2.contract.HomeChronometerContract
import java.sql.SQLException
import java.util.*
import kotlin.collections.ArrayList

class ChronometerActivitiesDB(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION),
    HomeChronometerContract.IHomeModel
{

    companion object DbContract
    {
        private const val TAG = "HOME DB"

        private var DATABASE_VERSION = 4
        private const val DATABASE_NAME = "ChronometerDb"

        //Name of activity table
        private const val ACTIVITY_TABLE_NAME = "chronometer_activity"
        //Keys of activity
        private const val KEY_ID = "id"/*The protection from id overflow is intrinsic in the application. It is highly improbable that someone creates that much activities*/
        private const val KEY_NAME = "name"

        //name of timing table
        private const val TIMING_TABLE_NAME= "timing"
        //Keys of timing
        private const val KEY_TIME = "time"
        private const val KEY_TIMESTAMP = "timestamp"
        private const val KEY_ID_FOREIGN = "activity_id"
        private const val ON_UPDATE_ON_DELETE_TIMING = "ON DELETE CASCADE ON UPDATE CASCADE"

        //create activity entries. Avoid using autoincrement for bad performances:
        private const val SQL_CREATE_ACTIVITY_ENTRIES =
            "CREATE TABLE $ACTIVITY_TABLE_NAME(" +
                    "$KEY_ID INTEGER PRIMARY KEY," +
                    "$KEY_NAME TEXT UNIQUE NOT NULL);"

        private const val SQL_CREATE_TIMING_ENTRIES =
            "CREATE TABLE $TIMING_TABLE_NAME(" +
                    "$KEY_TIME REAL NOT NULL," +
                    "$KEY_TIMESTAMP INTEGER NOT NULL," +
                    "$KEY_ID_FOREIGN INTEGER NOT NULL," +
                    "FOREIGN KEY($KEY_ID_FOREIGN) REFERENCES $ACTIVITY_TABLE_NAME($KEY_ID) $ON_UPDATE_ON_DELETE_TIMING);"

        private const val SQL_DELETE_ACTIVITY_ENTRIES = "DROP TABLE IF EXISTS $ACTIVITY_TABLE_NAME"

        private const val SQL_DELETE_TIMING_ENTRIES = "DROP TABLE IF EXISTS $TIMING_TABLE_NAME"

        private const val SQL_SELECT_MAX_ID = "SELECT MAX($KEY_ID) FROM $ACTIVITY_TABLE_NAME"

        private const val SQL_SELECT_ALL_ACTIVITIES_ONLY_FIRST_TIMING =
            "SELECT * FROM $ACTIVITY_TABLE_NAME"

        private const val SQL_SELECT_ACTIVITY_TIMINGS_AND_TIMESTAMPS =
            "SELECT $KEY_ID_FOREIGN, $KEY_TIME, $KEY_TIMESTAMP " +
                    "FROM $TIMING_TABLE_NAME " +
                    "WHERE $KEY_ID_FOREIGN = ?"

    }


    override fun onCreate(db: SQLiteDatabase?)
    {
        db?.execSQL(SQL_CREATE_ACTIVITY_ENTRIES)
        db?.execSQL(SQL_CREATE_TIMING_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int)
    {
        db?.execSQL(SQL_DELETE_ACTIVITY_ENTRIES)
        db?.execSQL(SQL_DELETE_TIMING_ENTRIES)

        onCreate(db)
    }

    //INTERFACE FUNCITONS------------------------------------------------------------------------------------------------------------------------------------------------

    override fun getAllActivities(): ArrayList<ChronometerActivity>
    {
        val activities: ArrayList<ChronometerActivity> = ArrayList()

        val db = this.writableDatabase
        val cursor = db.rawQuery(SQL_SELECT_ALL_ACTIVITIES_ONLY_FIRST_TIMING, null)

        if (cursor.moveToFirst())
        {
            do
            {
                activities.add(
                    ChronometerActivity(
                        cursor.getInt(0), cursor.getString(1), getTimings(cursor.getInt(0)))
                )
            }
            while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return activities

    }

    override fun addNewActivity(name: String): ChronometerActivity?
    {
        val db = this.writableDatabase

        //Selecting the maximum id incremented by one
        val id = getNewMaxId()

        val values = ContentValues()
        values.put(KEY_ID, id)
        values.put(KEY_NAME, name)

        //if db insert the element i will return a new activity with an empty timing list else null
        try {
            db.insertOrThrow(ACTIVITY_TABLE_NAME, null, values)
        }
        catch (sqlExc: SQLException) {
            db.close()
            return null
        }

        db.close()
        return ChronometerActivity(id, name, ArrayList())
    }

    //TODO catch the error
    override fun deleteActivity(activityName:String): Boolean
    {
        val db = this.writableDatabase

        /*delete returns the number of rows affected if a whereClause is passed in, 0 otherwise.*/
        val rowsDeleted = db.delete(
            ACTIVITY_TABLE_NAME,
            "$KEY_NAME = ?",
            arrayOf(activityName)
        )

        db.close()

        return rowsDeleted > 0
    }

    override fun getNewMaxId(): Int
    {
        var id = 0
        val cursor = writableDatabase.rawQuery(SQL_SELECT_MAX_ID, null)
        if (cursor.moveToFirst())
            id = cursor.getInt(0) + 1

        cursor.close()

        Log.d(TAG, "new max id: $id")

        return id
    }

    override fun addNewTiming(time: Long, timestamp: Long, activityId: Int): Pair<Long, GregorianCalendar>?
    {
        var newItem: Pair<Long, GregorianCalendar>? = null
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(KEY_TIME, time)
        values.put(KEY_TIMESTAMP, timestamp)
        values.put(KEY_ID_FOREIGN, activityId)

        try {

            db.insertOrThrow(TIMING_TABLE_NAME, null, values)
        }
        catch (sqlExc: SQLException) {
            db.close()
            Log.e(TAG, "${sqlExc.printStackTrace()}")
            return newItem
        }

        db.close()
        Log.d(TAG, "actitivity $activityId:  new timing:   $time. Its timestamp :     $timestamp")

        val createOn = GregorianCalendar()
        createOn.timeInMillis = timestamp

        newItem = Pair(time, createOn)
        return newItem
    }

    override fun getTimings(activityId: Int): ArrayList<Pair<Long, GregorianCalendar>>?
    {
        val timings_timestamps = ArrayList<Pair<Long, GregorianCalendar>>()

        val db = this.writableDatabase

        val cursor = db.rawQuery(SQL_SELECT_ACTIVITY_TIMINGS_AND_TIMESTAMPS, arrayOf("$activityId"))
        if (cursor.moveToFirst())
        {
            do
            {
                val createOn = GregorianCalendar()
                createOn.timeInMillis = cursor.getLong(2)
                timings_timestamps.add(Pair(cursor.getLong(1), createOn))

                Log.d(TAG, "timing retrived:  $activityId:  timing:   ${cursor.getLong(0)}. Its timestamp :  ${cursor.getLong(2)}")
            }
            while (cursor.moveToNext())
        }
        //If activity does not have timings I will inform the presenter returning null
        else {  return null }

        cursor.close()
        db.close()

        //I return a blank name because it will never be used for this purpose
        return timings_timestamps
    }

    //END INTERFACE FUNCITONS------------------------------------------------------------------------------------------------------------------------------------------------
}