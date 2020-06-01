package com.andrea.advanced_workout_clock.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.andrea.advanced_workout_clock.R

/**
 * A simple [Fragment] subclass.
 */
class DataFragment: Fragment() {

    companion object
    {
        private const val TAG = "DATA FRAGMENT CICLE"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_data_, container, false)
    }

    //This method shows when a new fragment is created
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }

    override fun onResume()
    {
        super.onResume()
        if (this.context != null)
            activity?.window?.navigationBarColor = ContextCompat.getColor(this.requireContext(), R.color.white)
    }


}
