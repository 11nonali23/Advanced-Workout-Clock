package com.example.advanced_chrono2.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.advanced_chrono2.contract.TimerActivitiesContract
import java.sql.SQLException

class TimerActivitiesDB(context: Context) :
    SQLiteOpenHelper(
        context,
        DATABASE_NAME, null,
        DATABASE_VERSION
    ),
    TimerActivitiesContract.ITimerActivitiesModel
{
    companion object
    {
        private const val TAG = "TIMER_ACTIVITIES DB"

        private var DATABASE_VERSION = 9
        private const val DATABASE_NAME = "timerActivitiesDb"

        //Name of activity table
        private const val ACTIVITY_TABLE_NAME = "timer_activity"
        //Keys of activity
        private const val KEY_ACTIVITY_ID = "id"/*The protection from id overflow is intrinsic in the application. It is more than highly improbable that someone creates that much activities*/
        private const val KEY_NAME = "name"

        //name of timing table
        private const val ITEM_TABLE_NAME= "item"
        //Keys of timing
        private const val KEY_ITEM_ID = "item_id"
        private const val KEY_WORKOUT_SEC = "workout_sec"
        private const val KEY_REST_SEC = "rest_sec"
        private const val KEY_FOREIGN_ACTIVITY_ID = "activity_id"
        private const val ON_UPDATE_ON_DELETE_ITEM = "ON DELETE CASCADE ON UPDATE CASCADE"

        //create activity entries. Avoid using autoincrement for bad performances:
        private const val SQL_CREATE_ACTIVITY_ENTRIES =
            "CREATE TABLE $ACTIVITY_TABLE_NAME(" +
                    "$KEY_ACTIVITY_ID INTEGER PRIMARY KEY," +
                    "$KEY_NAME TEXT UNIQUE NOT NULL);"

        //create activity entries. Avoid using autoincrement fro bad performances:
        private const val SQL_CREATE_ITEM_ENTRIES =
            "CREATE TABLE $ITEM_TABLE_NAME(" +
                    "$KEY_ITEM_ID INTEGER PRIMARY KEY," +
                    "$KEY_WORKOUT_SEC INTEGER NOT NULL," +
                    "$KEY_REST_SEC INTEGER NOT NULL," +
                    "$KEY_FOREIGN_ACTIVITY_ID INTEGER NOT NULL," +
                    "FOREIGN KEY($KEY_FOREIGN_ACTIVITY_ID) REFERENCES $ACTIVITY_TABLE_NAME($KEY_ACTIVITY_ID) $ON_UPDATE_ON_DELETE_ITEM);"

        private const val SQL_DELETE_ACTIVITY_ENTRIES = "DROP TABLE IF EXISTS $ACTIVITY_TABLE_NAME"

        private const val SQL_DELETE_ITEM_ENTRIES = "DROP TABLE IF EXISTS $ITEM_TABLE_NAME"

        private const val SQL_SELECT_ALL_ACTIVITIES = "SELECT * FROM $ACTIVITY_TABLE_NAME"

        private const val SQL_SELECT_ACTIVITY_ITEMS =
            "SELECT * " +
            "FROM $ITEM_TABLE_NAME " +
            "WHERE $KEY_FOREIGN_ACTIVITY_ID = ?"

        private const val SQL_SELECT_MAX_ID = "SELECT MAX($KEY_ACTIVITY_ID) FROM $ACTIVITY_TABLE_NAME"

        private const val SQL_SELECT_MAX_TIMER_ITEM_ID = "SELECT MAX($KEY_ITEM_ID) FROM $ITEM_TABLE_NAME "
    }



    override fun onCreate(db: SQLiteDatabase?)
    {
        db?.execSQL(SQL_CREATE_ACTIVITY_ENTRIES)
        db?.execSQL(SQL_CREATE_ITEM_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int)
    {
        db?.execSQL(SQL_DELETE_ACTIVITY_ENTRIES)
        db?.execSQL(SQL_DELETE_ITEM_ENTRIES)

        onCreate(db)
    }

    override fun getAllActivities(): ArrayList<TimerActivity>
    {
        val activities: ArrayList<TimerActivity> = ArrayList()

        val db = this.writableDatabase
        val cursor = db.rawQuery(SQL_SELECT_ALL_ACTIVITIES, null)


        if (cursor.moveToFirst())
        {
            do
            {
                activities.add(
                    TimerActivity(
                        cursor.getInt(0), cursor.getString(1), getAllTimerItems(cursor.getInt(0)))
                )
            }
            while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return activities
    }

    override fun addNewActivity(name: String): TimerActivity?
    {
        val db = this.writableDatabase

        //Selecting the maximum id incremented by one
        val id = getNewMaxId()

        val values = ContentValues()
        values.put(KEY_ACTIVITY_ID, id)
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
        return TimerActivity(id, name, ArrayList())
    }

    override fun deleteActivity(activityName: String): Boolean
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

        return id
    }

    //if no timer Items are associated to the activity I will only return an empty array
    override fun getAllTimerItems(activityId: Int): ArrayList<TimerItem>
    {
        val db = this.writableDatabase
        val cursor = db.rawQuery(SQL_SELECT_ACTIVITY_ITEMS, arrayOf(activityId.toString()))
        val timerItems: ArrayList<TimerItem> = ArrayList()

        if(cursor.moveToFirst())
        {
            do {
                timerItems.add(
                    TimerItem(
                        cursor.getInt(0),
                        TimerActivitiesContract.ITimerActivitiesView.itemLogo,
                        cursor.getInt(1),
                        cursor.getInt(2)
                    )
                )
            }while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return timerItems
    }

    override fun addTimerItem(parentId: Int, workoutSeconds: Int, restSeconds: Int): TimerItem?
    {
        val db = this.writableDatabase

        val id = getNewMaxTimerItemId()

        val values = ContentValues()
        values.put(KEY_ITEM_ID, id)
        values.put(KEY_WORKOUT_SEC, workoutSeconds)
        values.put(KEY_REST_SEC, restSeconds)
        values.put(KEY_FOREIGN_ACTIVITY_ID, parentId)

        try {

            db.insertOrThrow(ITEM_TABLE_NAME, null, values)
        }
        catch (sqlExc: SQLException) {
            db.close()
            return null
        }

        db.close()

        return TimerItem(id, TimerActivitiesContract.ITimerActivitiesView.itemLogo, workoutSeconds, restSeconds)
    }

    override fun delTimerItem(activityId: Int, itemId: Int) : Boolean
    {
        val db = this.writableDatabase

        /*delete returns the number of rows affected if a whereClause is passed in, 0 otherwise.*/
        val rowsDeleted = db.delete(
            ITEM_TABLE_NAME,
            "$KEY_ITEM_ID = ? AND $KEY_FOREIGN_ACTIVITY_ID = ?",
            arrayOf(itemId.toString(), activityId.toString())
        )

        db.close()

        return rowsDeleted > 0
    }

    override fun getNewMaxTimerItemId() : Int
    {
        var id = 0
        val cursor = writableDatabase.rawQuery(SQL_SELECT_MAX_TIMER_ITEM_ID, null)
        if (cursor.moveToFirst())
            id = cursor.getInt(0) + 1

        cursor.close()

        return id
    }
}