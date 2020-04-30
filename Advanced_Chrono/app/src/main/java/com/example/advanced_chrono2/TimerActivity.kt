package com.example.advanced_chrono2

import androidx.core.content.ContextCompat
import com.example.advanced_chrono2.model.TimerItem
import com.example.advanced_chrono2.shared_preferences.TimerPrefUtilsManager
import kotlinx.android.synthetic.main.timer_layout.*
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.util.LinkedList
import kotlinx.android.synthetic.main.toolbar_layout.*

//TODO when in real activity upate the color of the rest or workout based of what is running
// in the real project data will be get from BUNDLE EXTRA on itent call

class TimerActivity : AppCompatActivity()
{
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    private lateinit var timer: CountDownTimer
    private var currTimerLengthSeconds = 0L //tracks the length of the current timer
    private var timerState = TimerState.Stopped //tracks the state of the timer
    private var secondsRemaining = 0L //tracks the remining seconds to the end of the timer

    private var timerItemList = LinkedList<TimerItem>() //List of items to complete
    private var isWorkout = true //tracks if activity has to set to show workout bar or rest
    private var isTimerListStarted = false //tracks if item list is started
    private var currTimerItemData: TimerItem //tracks the curent item to complete

    //TEST --_> This init will be deleted
    init {
        timerItemList.add(TimerItem(0,R.drawable.ic_timer_black, 20, 15))
        timerItemList.add(TimerItem(1, R.drawable.ic_timer_black,20 ,10))
        timerItemList.add(TimerItem(2, R.drawable.ic_timer_black,15, 5))
        timerItemList.add(TimerItem(3, R.drawable.ic_timer_black, 20, 4))
        timerItemList.add(TimerItem(4, R.drawable.ic_timer_black,25, 40))

        currTimerItemData = timerItemList.poll()
    }

    enum class TimerState {
        Stopped, Paused, Running
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.timer_layout)

        this.toolbar = toolBar

        toolbar.title = "nome attività (preso da bundle)"
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_orange)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

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


    override fun onDestroy() {
        super.onDestroy()
        TimerPrefUtilsManager.clearAll(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        TimerPrefUtilsManager.clearAll(this)
    }

    private fun restartTimer() {
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
    private fun onTimerEnded() {
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

        TimerPrefUtilsManager.setSecondsRemaining(this, currTimerLengthSeconds)
        secondsRemaining = currTimerLengthSeconds

        startNewTimer()
    }

    private fun startTimersFromList() {
        isTimerListStarted = true
        startNewTimer()
    }

    private fun startNewTimer() {
        //If the timer is started the progress bars will be 0 so i have to reset the current running one
        styleBeforeStart()

        timerState = TimerState.Running
        updateUIButtons()
        updateTimerUI()

        timer = object : CountDownTimer(secondsRemaining * 1000, 1000) {
            override fun onFinish() = onTimerEnded()

            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000
                updateTimerUI()
            }
        }.start()
    }

    private fun styleBeforeStart() {
        //setting progress bar
        if (isWorkout)
            if (progress_circular.progress == progress_circular.max)
                progress_circular.progress = 0
            else
                if (progress_circular_rest.progress == progress_circular_rest.max)
                    progress_circular_rest.progress = 0

        //setting color
        if (isWorkout) {
            timer_workout_indicator.setTextColor(ContextCompat.getColor(this, R.color.blood))
            timer_rest_indicator.setTextColor(Color.BLACK)
        }
        else{
            timer_rest_indicator.setTextColor(ContextCompat.getColor(this, R.color.blood))
            timer_workout_indicator.setTextColor(Color.BLACK)
        }
    }


    private fun setNewTimerAndProgressLength()
    {
        val lenghtInMinutes = TimerPrefUtilsManager.getTimerLength(this)

        if (isWorkout)
        {
            currTimerLengthSeconds = ((lenghtInMinutes * currTimerItemData.workoutSeconds).toLong())
            progress_circular.max = currTimerLengthSeconds.toInt()
            progress_circular.progress = progress_circular.max

            progress_circular_rest.max = currTimerLengthSeconds.toInt()
            progress_circular_rest.progress = currTimerItemData.restSeconds.toInt()
        }
        else
        {
            currTimerLengthSeconds = ((lenghtInMinutes * currTimerItemData.restSeconds).toLong())
            progress_circular_rest.max = currTimerLengthSeconds.toInt()
        }
    }

    private fun setPreviousTimerLength()
    {
        Log.e("TIMER", "prev")
        currTimerLengthSeconds = TimerPrefUtilsManager.getPreviousTimerLenghtSeconds(this)
        progress_circular.max = currTimerLengthSeconds.toInt()
    }

    private fun updateTimerUIOnlyText()
    {
        val minutesUntilFinished = secondsRemaining / 60
        val secondsInMinuteUntilFinished = secondsRemaining - minutesUntilFinished * 60
        val secondsStr = secondsInMinuteUntilFinished.toString()
        timer_text.text = "$minutesUntilFinished:${if (secondsStr.length == 2) secondsStr else "0" + secondsStr}"
    }

    private fun updateTimerUI()
    {
        updateTimerUIOnlyText()

        if (isWorkout)
            progress_circular.progress = (currTimerLengthSeconds - secondsRemaining).toInt()
        else
            progress_circular_rest.progress = (currTimerLengthSeconds - secondsRemaining).toInt()
    }

    private fun updateUIButtons()
    {
        when(timerState)
        {
            TimerState.Running ->
            { start.isEnabled = false; pause.isEnabled = true; reset.isEnabled = false }

            TimerState.Paused ->
            {start.isEnabled = true; pause.isEnabled = false; reset.isEnabled = true}

            TimerState.Stopped ->
            {start.isEnabled = true; pause.isEnabled = false; reset.isEnabled = false}
        }
    }
}
