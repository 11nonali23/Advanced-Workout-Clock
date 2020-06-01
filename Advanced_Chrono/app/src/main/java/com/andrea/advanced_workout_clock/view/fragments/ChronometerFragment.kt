package com.andrea.advanced_workout_clock.view.fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.andrea.advanced_workout_clock.R
import com.andrea.advanced_workout_clock.contract.HomeChronometerContract
import com.andrea.advanced_workout_clock.model.ChronometerActivity
import com.andrea.advanced_workout_clock.presenter.ChronometerPresenter
import com.andrea.advanced_workout_clock.view.custom_views.CustomDialog
import kotlinx.android.synthetic.main.chrono_layout.*
import kotlin.math.pow


//ToDo implement the save button (only when you have the database)

/**
 * This fragment show the home to the user. The home is the timer that saves data.
 * A Presenter class is unuseful beacuse if a fragment is destroyed, when it is recreated
 * its state is saved but the presenter doe not save the state
 */


class ChronometerFragment : Fragment(), HomeChronometerContract.IHomeChronometerView
{
    private val homePresenter: HomeChronometerContract.IHomePresenter = ChronometerPresenter(this)

    //ADAPTERS
    private lateinit var spinnerAdapter: ArrayAdapter<ChronometerActivity>

    private var customDialog: CustomDialog? = null

    companion object
    {
        private const val TAG = "HOME FRAGMENT"

        //VIEW CHRONOMETER LOGIC
        private var pauseOffset: Long = 0L                                       //used to set the timer properly when restored from on pause and to take note of time
        private var progresssion :Int = -1                                       //define the progression of the circular bar
        private var chronoState: ChronometerState = ChronometerState.Resetted    //track the chronometer state
    }

    //This enum stores the states of the chronometer
    enum class ChronometerState
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
        setHasOptionsMenu(true)
    }

    //Setting the options menu
    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater)
    {
        menuInflater.inflate(R.menu.home_menu, menu)
    }

    //After the activity is created i have to set-up the components for the chronometer
    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)

        updateUIButtons()

        homePresenter.onViewCreated(this.context, chrono_spinner.selectedItemPosition)

        //removing the progress bar if the screen is too little
        val dm = resources.displayMetrics
        val density = dm.density * 160.toDouble()
        val x = (dm.widthPixels / density).pow(2.0)
        val y = (dm.heightPixels / density).pow(2.0)
        val screenInches = Math.sqrt(x + y)

        if (screenInches < 5.3)
            progress_circular_chrono.visibility = View.INVISIBLE

        //LISTENERS-----------------------------------------------------------------------------------------------------------------------------------

        //Listener to update progression bar
        chronometer_.setOnChronometerTickListener {
            progresssion++
            progress_circular_chrono.progress = (progresssion)%60
        }

        //Listener for pause button
        chrono_start.setOnClickListener {
            chronometer_.base = SystemClock.elapsedRealtime() - pauseOffset //setting the base of chronometer removing the stopage time
            chronometer_.start()
            chronoState = ChronometerState.Running
            updateUIButtons()
        }

        //Listener for start buttpp:mpb_showProgressBackground="true"on
        chrono_pause.setOnClickListener {

            chronometer_.stop()
            //removing from the elapsed time the base of the chronometer: this will make the chrono find the offset of the pause
            //because the base is setted when i start the chronometer
            pauseOffset = SystemClock.elapsedRealtime() - chronometer_.base
            chronoState = ChronometerState.Paused
            updateUIButtons()
        }

        chrono_reset.setOnClickListener {

            chronometer_.base = SystemClock.elapsedRealtime() //set the base to the elapsed time
            pauseOffset = 0L    //reset the offset
            //restart the bar of the chronometer from zero
            progresssion = -1
            progress_circular_chrono.progress = progresssion

            chronoState = ChronometerState.Resetted
            updateUIButtons()

            //i also have to save data in case checbox is checked
            if (chrono_check.isChecked)
                saveCurrentTiming()
        }

        //add new activity
        add_activity_button.setOnClickListener {
            showAddDialogBuilder()
        }

        //delete selected activity
        del_activity_button.setOnClickListener{
            showDeleteDialogBuilder()
        }

        show_timings_button.setOnClickListener{
            if (this.context != null)
            {
                customDialog = CustomDialog(this)
                customDialog!!.show()
            }
        }

        //save timing button
        chrono_save_btn.setOnClickListener {
            saveCurrentTiming()
        }

        //update the current selected activity of the presenter on change
        chrono_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {
                homePresenter.handleNewSelectedActivity((chrono_spinner.selectedItem as ChronometerActivity).id)
            }

        }
    }

    //LISTENERS-----------------------------------------------------------------------------------------------------------------------------------
    override fun onResume()
    {
        super.onResume()
        if (this.context != null)
            activity?.window?.navigationBarColor = ContextCompat.getColor(this.requireContext(), R.color.white)
    }

    //INTERFACE FUNCTIONS------------------------------------------------------------------------------------------

    override fun setUpSpinnerView(activities: List<ChronometerActivity>)
    {
        this.spinnerAdapter = ArrayAdapter(
            this.lendContext()!!,
            R.layout.spinner_text,
            activities)

        chrono_spinner.adapter = this.spinnerAdapter
    }

    override fun updateActivitiesView()
    {
        spinnerAdapter.notifyDataSetChanged()
    }

    override fun updateTimingsView() { customDialog?.updateAdapter() }


    override fun setNewItemAsSelected() = chrono_spinner.setSelection(chrono_spinner.count - 1)

    override fun itemRemovedFromDataSet(itemPosition: Int) {    customDialog?.notifyAdapterItemRemoved(itemPosition)    }

    override fun displayResult(result: String)
    {
        if (lendContext() != null)
            Toast.makeText(this.lendContext(), result, Toast.LENGTH_LONG).show()
    }

    override fun lendContext(): Context? {return context}

    //END INTERFACE FUNCTIONS------------------------------------------------------------------------------------------

    //FUNCTIONS FOR ADAPTERS------------------------------------------------------------------------------------------

    fun deleteTiming(itemPosition: Int){  homePresenter.deleteTiming(itemPosition)}
    //END FUNCTIONS FOR ADAPTERS------------------------------------------------------------------------------------------


    //VIEW HELPER FUNCTIONS------------------------------------------------------------------------------------------

    //TODO center the buttons
    private fun showAddDialogBuilder()
    {
        val dialogBuilder = AlertDialog.Builder(this.context, R.style.AlertDialogCustom)
        val dialogView = layoutInflater.inflate(R.layout.add_activity_layout, null)
        //setting the hint of the text
        dialogView.findViewById<TextView>(R.id.insertActivity).hint= getString(R.string.chrono_insert_hint)
        dialogBuilder.setView(dialogView)

        dialogBuilder.setPositiveButton(context?.getString(R.string.ADD_ACTIVITY_CONFIRM)) { _, _->
            val editText = dialogView.findViewById<EditText>(R.id.insertActivity)
            homePresenter.addNewActivity(editText.text.toString())
        }

        dialogBuilder.setNegativeButton(context?.getString(R.string.DISMISS_DIALOG)) { dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        dialogBuilder.show()
    }

    //TODO center the buttons
    private fun showDeleteDialogBuilder()
    {
        val dialogBuilder = AlertDialog.Builder(this.context, R.style.AlertDialogCustom)
        dialogBuilder.setTitle(context?.getString(R.string.DEL_ACTIVITY_TITLE))

        dialogBuilder.setPositiveButton(context?.getString(R.string.DEL_ACTIVITY_CONFIRM)) { _, _->
            if (chrono_spinner.selectedItem != null)
                homePresenter.deleteActivity(chrono_spinner.selectedItem.toString())
            else
                homePresenter.deleteActivity(null)

        }

        dialogBuilder.setNegativeButton(context?.getString(R.string.DISMISS_DIALOG)) { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        dialogBuilder.show()
    }


    private fun saveCurrentTiming()
    {
        if (chrono_spinner.selectedItem != null)
            homePresenter.saveTempo(pauseOffset, (chrono_spinner.selectedItem as ChronometerActivity).id)
        else
            homePresenter.saveTempo(pauseOffset, null)
    }


    //Update buttons of the user interface depending on the state of the chrono
    private fun updateUIButtons()
    {
        when(chronoState)
        {
            ChronometerState.Running ->
            { chrono_start.isEnabled = false; chrono_pause.isEnabled = true; chrono_reset.isEnabled = false; chrono_save_btn.isEnabled = false }

            ChronometerState.Paused ->
            {chrono_start.isEnabled = true; chrono_pause.isEnabled = false; chrono_reset.isEnabled = true; chrono_save_btn.isEnabled = true}

            ChronometerState.Resetted ->
            {chrono_start.isEnabled = true; chrono_pause.isEnabled = false; chrono_reset.isEnabled = false; chrono_save_btn.isEnabled = false}
        }
    }

    //ENDVIEW HELPER FUNCTIONS------------------------------------------------------------------------------------------
}
