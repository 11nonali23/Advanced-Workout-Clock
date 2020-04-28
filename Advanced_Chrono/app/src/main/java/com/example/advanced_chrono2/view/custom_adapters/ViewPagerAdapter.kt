package com.example.advanced_chrono2.view.custom_adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.advanced_chrono2.view.fragments.DataFragment
import com.example.advanced_chrono2.view.fragments.TimerActivitiesFragment
import com.example.advanced_chrono2.view.fragments.HomeChronometerFragment


class ViewPagerAdapter(fa: FragmentActivity)  : FragmentStateAdapter(fa)
{
    companion object { private const val PAGES = 3 }

    override fun getItemCount(): Int  = PAGES

    override fun createFragment(position: Int): Fragment
    {
        return when (position)
        {
            0 -> HomeChronometerFragment()
            1 -> TimerActivitiesFragment()
            2 -> DataFragment()
            else -> HomeChronometerFragment()
        }
    }
}