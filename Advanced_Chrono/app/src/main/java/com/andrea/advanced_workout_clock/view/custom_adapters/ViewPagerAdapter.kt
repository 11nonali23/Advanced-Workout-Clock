package com.andrea.advanced_workout_clock.view.custom_adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.andrea.advanced_workout_clock.view.fragments.DataFragment
import com.andrea.advanced_workout_clock.view.fragments.TimerActivitiesFragment
import com.andrea.advanced_workout_clock.view.fragments.ChronometerFragment


class ViewPagerAdapter(fa: FragmentActivity)  : FragmentStateAdapter(fa)
{
    companion object { private const val PAGES = 3 }

    override fun getItemCount(): Int  = PAGES

    override fun createFragment(position: Int): Fragment
    {
        return when (position)
        {
            0 -> TimerActivitiesFragment()
            1 -> ChronometerFragment()
            2 -> DataFragment()
            else -> ChronometerFragment()
        }
    }
}
