package com.example.advanced_chrono2.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.advanced_chrono2.contract.HomeChronometerContract
import java.sql.SQLException

class ChronometerActivitiesDatabase(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION),
    HomeChronometerContract.IHomeModel
{

    companion object
    {
        private var DATABASE_VERSION = 2
        private const val DATABASE_NAME = "timerActivitiesDb"

        //Name of activity table
        private const val ACTIVITY_TABLE_NAME = "activity"
        //Keys of activity
        private const val KEY_ID = "id"/*The protection from id overflow is intrinsic in the application. It is more than highly improbable that someone creates that much activities*/
        private const val KEY_NAME = "name"

        //name of timing table
        private const val TIMING_TABLE_NAME= "timing"
        //Keys of timing
        private const val KEY_TIME = "time"
        private const val KEY_TIMESTAMP = "timestamp"
        private const val KEY_ID_FOREIGN = "activity_id"
        private const val ON_UPDTAE_ON_DELETE_TIMING = "ON DELETE CASCADE ON UPDATE CASCADE"

        //create activity entries. Avoid using autoincrement fro bad performances:
        private const val SQL_CREATE_ACTIVITY_ENTRIES =
            "CREATE TABLE $ACTIVITY_TABLE_NAME(" +
                    "$KEY_ID INTEGER PRIMARY KEY," +
                    "$KEY_NAME TEXT UNIQUE NOT NULL);"

        private const val SQL_CREATE_TIMING_ENTRIES =
            "CREATE TABLE $TIMING_TABLE_NAME(" +
                    "$KEY_TIME REAL NOT NULL," +
                    "$KEY_TIMESTAMP INTEGER NOT NULL," +
                    "$KEY_ID_FOREIGN INTEGER NOT NULL," +
                    "FOREIGN KEY($KEY_ID_FOREIGN) REFERENCES $ACTIVITY_TABLE_NAME($KEY_ID) $ON_UPDTAE_ON_DELETE_TIMING);"

        private const val SQL_DELETE_ACTIVITY_ENTRIES = "DROP TABLE IF EXISTS $ACTIVITY_TABLE_NAME"

        private const val SQL_DELETE_TIMING_ENTRIES = "DROP TABLE IF EXISTS $TIMING_TABLE_NAME"

        private const val SQL_SELECT_MAX_ID = "SELECT MAX($KEY_ID) FROM $ACTIVITY_TABLE_NAME"

        private const val SQL_SELECT_ALL_ACTIVITIES_NAME =
            "SELECT $KEY_NAME FROM $ACTIVITY_TABLE_NAME"

        private const val SQL_SELECT_ALL_ACTIVITIES_AND_TIMINGS = "" +
                    "SELECT $KEY_NAME " +
                    "FROM $ACTIVITY_TABLE_NAME a JOIN $TIMING_TABLE_NAME t ON a.$KEY_ID = t.$KEY_ID_FOREIGN"

    }

    //INTERFACE FUNCITONS------------------------------------------------------------------------------------------------------------------------------------------------

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

    override fun getAllActivitiesName(): ArrayList<String>?
    {
        val activityNames = ArrayList<String>()
        val db = this.writableDatabase
        val cursor = db.rawQuery(SQL_SELECT_ALL_ACTIVITIES_NAME, null)

        if (cursor.moveToFirst())
        {
            do
            { activityNames.add(cursor.getString(0)) }
            while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return activityNames

    }

    override fun addNewActivity(name: String): Boolean
    {
        val db = this.writableDatabase

        //Selecting the maximum id incremented by one
        val id = getNewMaxId(db)

        val values = ContentValues()
        values.put(KEY_ID, id)
        values.put(KEY_NAME, name)

        //if db insert the element i will return a new activity with an empty timing list else null
        try {
            db.insertOrThrow(ACTIVITY_TABLE_NAME, null, values)
        }
        catch (sqlExc: SQLException) {
            db.close()
            return false
        }

        db.close()
        return true
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

    override fun addNewTiming(time: Long, timestamp: Long, activityName: String)
    {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(KEY_TIME, time)
        values.put(KEY_TIMESTAMP, timestamp)

        db.insert(ACTIVITY_TABLE_NAME, null, values)

        db.close()
    }

    //END INTERFACE FUNCITONS------------------------------------------------------------------------------------------------------------------------------------------------

    //HELPER FUNCITONS------------------------------------------------------------------------------------------------------------------------------------------------

    //Returns maximum id
    private fun getNewMaxId(writableDatabase: SQLiteDatabase): Int
    {
        var id = 0
        val cursor = writableDatabase.rawQuery(SQL_SELECT_MAX_ID, null)
        if (cursor.moveToFirst())
            id = cursor.getInt(0) + 1

        cursor.close()

        return id
    }
}