package com.example.advanced_chrono2

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import com.example.advanced_chrono2.model.TimerItem
import com.example.advanced_chrono2.shared_preferences.TimerPrefUtilsManager
import kotlinx.android.synthetic.main.timer_layout.*
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.toolbar_layout.*
import java.util.*
import kotlin.collections.ArrayList

//TODO when in real activity upate the color of the rest or workout based of what is running
// in the real project data will be get from BUNDLE EXTRA on itent call

@Suppress("UNCHECKED_CAST")

class IntervalTimerActivity : AppCompatActivity()
{
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    private lateinit var timer: CountDownTimer

    private var currTimerLengthSeconds = 0L                 //tracks the length of the current timer
    private var timerState = TimerState.Stopped             //tracks the state of the timer
    private var secondsRemaining = 0L                       //tracks the remining seconds to the end of the timer

    private var timerItemList = LinkedList<TimerItem>()     //List of items to complete
    private var isWorkout = true                            //tracks if activity has to set to show workout bar or rest
    private var isTimerListStarted = false                  //tracks if item list is started
    private var currTimerItemData: TimerItem?  = null      //tracks the curent item to complete


    enum class TimerState {
        Stopped, Paused, Running
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.timer_layout)

        this.toolbar = toolBar

        toolbar.title = intent.getStringExtra("ACTIVITY_NAME")
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val list: ArrayList<TimerItem> = intent.getSerializableExtra("TIMER_ITEMS") as ArrayList<TimerItem>
        list.forEach { timerItemList.add(it) }

        //getting first element of the list
        currTimerItemData = timerItemList.poll()

        //TIMER LISTENERS-----------------------------------------------------------------------------------------------------------------------------------------------------------
        start.setOnClickListener {

            if (isTimerListStarted) {
                startNewTimer(); updateUIButtons()
            } else {
                startTimersFromList(); updateUIButtons()
            }

            timerState = TimerState.Running
        }

        pause.setOnClickListener {
            timer.cancel()
            timerState = TimerState.Paused
            updateUIButtons()
        }

        reset.setOnClickListener {
            timer.cancel()
            //updating shared preferences to new value
            TimerPrefUtilsManager.setSecondsRemaining(this, currTimerLengthSeconds)
            //resetting secondRemaing to the length of the timer
            secondsRemaining = currTimerLengthSeconds

            updateTimerUI()
            updateUIButtons()
        }

        fast_forward.setOnClickListener {
            secondsRemaining = 0 //fast forward delete seconds remaining
            updateTimerUI()
            timer.cancel()
            timer.onFinish()
        }

        //TIMER LISTENERS-----------------------------------------------------------------------------------------------------------------------------------------------------------

        //updating the remaining text
        timer_text_remaining.text = timerItemList.size.toString()
    }

    override fun onSupportNavigateUp(): Boolean
    {
        onBackPressed()
        return true
    }

    override fun onResume() {
        super.onResume()

        restartTimer()
    }

    override fun onPause() {
        super.onPause()

        if (timerState == TimerState.Running)
            timer.cancel()
        else if (timerState == TimerState.Paused) {
            //TODO show timer in notification
        }

        //save data of timer in shared preferences
        TimerPrefUtilsManager.setPreviousTimerLengthSeconds(this, currTimerLengthSeconds)
        TimerPrefUtilsManager.setSecondsRemaining(this, secondsRemaining)
        TimerPrefUtilsManager.setTimerState(this, timerState)
        TimerPrefUtilsManager.setTimerItemDatasList(this, timerItemList)
        TimerPrefUtilsManager.setIsTimerListStarted(this, isTimerListStarted)
        TimerPrefUtilsManager.setIsWorkout(this, isWorkout)
    }


    override fun onDestroy()
    {
        super.onDestroy()
        TimerPrefUtilsManager.clearAll(this)
    }

    override fun onBackPressed()
    {
        super.onBackPressed()
        TimerPrefUtilsManager.clearAll(this)
    }

    private fun restartTimer()
    {
        timerState = TimerPrefUtilsManager.getTimerState(this)
        isTimerListStarted = TimerPrefUtilsManager.getIsTimerListStarted(this)
        isWorkout = TimerPrefUtilsManager.getIsWorkout(this)

        if (timerState == TimerState.Stopped)
            setNewTimerAndProgressLength()
        else
            setPreviousTimerLength()

        secondsRemaining =
            if (timerState == TimerState.Running || timerState == TimerState.Paused)
                TimerPrefUtilsManager.getSecondsRemaining(this)
            else
                currTimerLengthSeconds

        if (timerState == TimerState.Running)
            startNewTimer()

        updateUIButtons()
        //set timer text
        updateTimerUIOnlyText()
    }

    //on finish of the timer. Manage the new Timer call
    private fun onTimerEnded()
    {
        timerState = TimerState.Stopped

        //changing is workout every time i restart timer
        isWorkout = !isWorkout

        //set the length of the timer to be the one set in SettingsActivity
        //if the length was changed when the timer was running
        setNewTimerAndProgressLength()

        //set the remaining text
        timer_text_remaining.text = timerItemList.size.toString()

        if (!isWorkout)
            currTimerItemData = timerItemList.poll()

        if (currTimerItemData == null)
            onActivityEnded()

        TimerPrefUtilsManager.setSecondsRemaining(this, currTimerLengthSeconds)
        secondsRemaining = currTimerLengthSeconds

        startNewTimer()
    }

    private fun startTimersFromList()
    {
        isTimerListStarted = true
        startNewTimer()
    }

    private fun startNewTimer()
    {
        //If the timer is started the progress bars will be 0 so i have to reset the current running one
        styleBeforeStart()

        timerState = TimerState.Running
        updateUIButtons()
        updateTimerUI()

        timer = object : CountDownTimer(secondsRemaining * 1000, 1000)
        {
            override fun onFinish() = onTimerEnded()

            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000
                updateTimerUI()
            }
        }.start()
    }

    private fun styleBeforeStart()
    {
        //setting progress bar
        if (isWorkout)
            if (progress_circular_work.progress == progress_circular_work.max)
                progress_circular_work.progress = 0
            else
                if (progress_circular_rest.progress == progress_circular_rest.max)
                    progress_circular_rest.progress = 0

        //setting color
        if (isWorkout) {
            timer_workout_indicator.setTextColor(ContextCompat.getColor(this, R.color.deep_orange))
            timer_rest_indicator.setTextColor(Color.WHITE)
        }
        else{
            timer_rest_indicator.setTextColor(ContextCompat.getColor(this, R.color.accent_yellow))
            timer_workout_indicator.setTextColor(Color.WHITE)
        }
    }


    private fun setNewTimerAndProgressLength()
    {
        val lengthInMinutes = TimerPrefUtilsManager.getTimerLength()

        if (isWorkout)
        {
            currTimerLengthSeconds = ((lengthInMinutes * currTimerItemData!!.workoutSeconds).toLong())
            progress_circular_work.max = currTimerLengthSeconds.toInt()
            progress_circular_work.progress = progress_circular_work.max

            progress_circular_rest.max = currTimerLengthSeconds.toInt()
            progress_circular_rest.progress = currTimerItemData!!.restSeconds.toInt()
        }
        else
        {
            currTimerLengthSeconds = ((lengthInMinutes * currTimerItemData!!.restSeconds).toLong())
            progress_circular_rest.max = currTimerLengthSeconds.toInt()
        }
    }

    private fun setPreviousTimerLength()
    {
        Log.e("TIMER", "prev")
        currTimerLengthSeconds = TimerPrefUtilsManager.getPreviousTimerLenghtSeconds(this)
        progress_circular_work.max = currTimerLengthSeconds.toInt()
    }

    @SuppressLint("SetTextI18n")
    private fun updateTimerUIOnlyText()
    {
        val minutesUntilFinished = secondsRemaining / 60
        val secondsInMinuteUntilFinished = secondsRemaining - minutesUntilFinished * 60
        val secondsStr = secondsInMinuteUntilFinished.toString()
        timer_text.text = "$minutesUntilFinished:${if (secondsStr.length == 2) secondsStr else "0$secondsStr"}"
    }

    private fun updateTimerUI()
    {
        updateTimerUIOnlyText()

        if (isWorkout)
            progress_circular_work.progress = (currTimerLengthSeconds - secondsRemaining).toInt()
        else
            progress_circular_rest.progress = (currTimerLengthSeconds - secondsRemaining).toInt()
    }

    private fun updateUIButtons()
    {
        when(timerState)
        {
            TimerState.Running ->
            { start.isEnabled = false; pause.isEnabled = true; reset.isEnabled = false; fast_forward.isEnabled = true }

            TimerState.Paused ->
            {start.isEnabled = true; pause.isEnabled = false; reset.isEnabled = true; fast_forward.isEnabled = false}

            TimerState.Stopped ->
            {start.isEnabled = true; pause.isEnabled = false; reset.isEnabled = false; fast_forward.isEnabled = false}
        }
    }

    private fun onActivityEnded()
    {
        //TODO
    }
}
