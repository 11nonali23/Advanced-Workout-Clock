package com.example.advanced_chrono2.view.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.advanced_chrono2.R
import com.example.advanced_chrono2.model.ActivityData
import com.example.advanced_chrono2.presenter.HomePresenter
import com.example.advanced_chrono2.presenter.IHomePresenter
import kotlinx.android.synthetic.main.chrono_layout.*

//ToDo implement the save button (only when you have the database)

/**
 * This fragment show the home to the user. The home is the timer that saves data.
 * A Presenter class is unuseful beacuse if a fragment is destroyed, when it is recreated
 * its state is saved but the presenter doe not save the state
 */


class HomeChronometerFragment : Fragment(), IHomeView
{
    private val homePresenter: IHomePresenter = HomePresenter(this)

    //ADAPTERS
    private lateinit var spinnerAdapter: ArrayAdapter<String>

    companion object
    {
        private const val TAG = "HOME FRAGMENT"

        //VIEW CHRONOMETER LOGIC
        private var pauseOffset: Long = 0L                              //is used to set the timer properly when restored from on pause and to take note of time
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

        homePresenter.onViewCreated()

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
            Log.e(TAG, "$pauseOffset")
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

        add_activity_button.setOnClickListener {
            showAddActivityDialogBuilder()
        }

        //TODO
        chrono_save_btn.setOnClickListener {
            chronometer_
        }

        //LISTENERS-----------------------------------------------------------------------------------------------------------------------------------
    }

    //The fragment calls on resume when user swipe into it
    override fun onResume()
    {
        super.onResume()
        Log.e(TAG, "RESUMED")
    }

    //INTERFACE FUNCTIONS------------------------------------------------------------------------------------------

    override fun displayResult(result: String) = Toast.makeText(this.context, result, Toast.LENGTH_LONG).show()

    override fun setUpSpinnerView(activitiesName: List<String>)
    {
        this.spinnerAdapter = ArrayAdapter(this.context!!,
            R.layout.support_simple_spinner_dropdown_item,
            activitiesName)

        chrono_spinner.adapter = this.spinnerAdapter
    }

    override fun updateActivitiesList()
    {
        spinnerAdapter.notifyDataSetChanged()
    }

    //END INTERFACE FUNCTIONS------------------------------------------------------------------------------------------


    //VIEW HELPER FUNCTIONS------------------------------------------------------------------------------------------

    private fun showAddActivityDialogBuilder()
    {
        val dialogBuilder = AlertDialog.Builder(this.context, R.style.AlertDialogCustom)
        val dialogView = layoutInflater.inflate(R.layout.add_chrono_activity_layout, null)
        dialogBuilder.setView(dialogView)

        dialogBuilder.setPositiveButton("SAVE ACTIVITY") {_ , _->
            val editText = dialogView.findViewById<EditText>(R.id.insertText)
            homePresenter.addNewActivity(editText.text.toString())
        }

        dialogBuilder.setNegativeButton("CANCEL") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        dialogBuilder.show()
    }

    //Update buttons of the user interface depending on the state of the chrono
    private fun updateUIButtons()
    {
        when(chronoState)
        {
            ChronoState.Running ->
            { chrono_start.isEnabled = false; chrono_pause.isEnabled = true; chrono_reset.isEnabled = false; chrono_save_btn.isEnabled = false }

            ChronoState.Paused ->
            {chrono_start.isEnabled = true; chrono_pause.isEnabled = false; chrono_reset.isEnabled = true; chrono_save_btn.isEnabled = true}

            ChronoState.Resetted ->
            {chrono_start.isEnabled = true; chrono_pause.isEnabled = false; chrono_reset.isEnabled = false; chrono_save_btn.isEnabled = false}
        }
    }

    //ENDVIEW HELPER FUNCTIONS------------------------------------------------------------------------------------------
}
