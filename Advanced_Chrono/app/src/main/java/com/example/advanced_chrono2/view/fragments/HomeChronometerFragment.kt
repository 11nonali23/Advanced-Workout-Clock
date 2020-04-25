package com.example.advanced_chrono2.view.fragments

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.advanced_chrono2.R
import kotlinx.android.synthetic.main.chrono_layout.*


/**
 * This fragment show the home to the user. The home is the timer that saves data.
 * A Presenter class is unuseful beacuse if a fragment is destroyed, when it is recreated
 * its state is saved but the presenter doe not save the state
 */


class HomeChronometerFragment() : Fragment()

{
    companion object
    {
        private const val TAG = "HOME FRAGMENT"

        private var pauseOffset: Long = 0L                              //is used to set the timer properly
        private var progresssion :Int = -1                              //define the progression of the circular bar
        private var alwaysSaveChecked: Boolean = false                  //track the checbox to save performance of teh user
        private var chronoState: ChronoState = ChronoState.Resetted     //track the chronometer state
    }

    //This enum stores the states of the chronometer
    enum class ChronoState
    {
        Resetted, Paused, Running
    }

    //when creating the view inflating the chronometer layout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.chrono_layout, container, false)
    }

    //This method shows when a new fragment is created
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "FRAGMENT HOME CREATED")
    }

    //After the activity is created i have to set-up the components for the chronometer
    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)


        //LISTENERS-----------------------------------------------------------------------------------------------------------------------------------
        //Listener to update progression bar
        chronometer_.setOnChronometerTickListener {
            progress_circular_chrono.progress = progresssion++ % 60
        }

        //Listener for pause button
        chrono_start.setOnClickListener {

            chronometer_.base = SystemClock.elapsedRealtime() - pauseOffset //setting the base of chronometer removing the stopage time
            chronometer_.start()
            chronoState = ChronoState.Running
            updateUIButtons()
        }

        //Listener for start button
        chrono_pause.setOnClickListener {

            chronometer_.stop()
            //removing from the elapsed time the base of the chronometer: this will make the chrono find the offset of the pause
            //because the base is setted when i start the chronometer
            pauseOffset = SystemClock.elapsedRealtime() - chronometer_.base
            chronoState = ChronoState.Paused
            updateUIButtons()
        }

        chrono_reset.setOnClickListener {

            chronometer_.base = SystemClock.elapsedRealtime() //set the base to the elapsed time
            pauseOffset = 0L    //reset the offset
            //restart the bar of the chronometer from zero
            progresssion = - 1
            progress_circular_chrono.progress = progresssion

            chronoState = ChronoState.Resetted
            updateUIButtons()

            //i also have to save data in case checbox is checked

        }

        chrono_check.setOnCheckedChangeListener { _, isChecked ->
            alwaysSaveChecked = isChecked
        }

        //TODO
        /*chrono_save_btn.setOnClickListener {

            /*val dialogBuilder = AlertDialog.Builder(this.context)
            val dialogView = layoutInflater.inflate(R.layout.alert_dialog_layout, null)
            dialogBuilder.setView(dialogView)
            dialogBuilder.setPositiveButton("YES!") { dialog, id ->
                Toast.makeText(this.context, "OK", Toast.LENGTH_LONG).show()
            }*/
        }*/

        //LISTENERS-----------------------------------------------------------------------------------------------------------------------------------
    }

    //The fragment calls on resume when user swipe into it
    override fun onResume()
    {
        super.onResume()
        Log.e(TAG, "RESUMED")
    }

    //Update buttons of the user interface depending on the state of the chrono
    private fun updateUIButtons()
    {
        when(chronoState)
        {
            ChronoState.Running ->
            { chrono_start.isEnabled = false; chrono_pause.isEnabled = true; chrono_reset.isEnabled = false }

            ChronoState.Paused ->
            {chrono_start.isEnabled = true; chrono_pause.isEnabled = false; chrono_reset.isEnabled = true}

            ChronoState.Resetted ->
            {chrono_start.isEnabled = true; chrono_pause.isEnabled = false; chrono_reset.isEnabled = false}
        }
    }

}
