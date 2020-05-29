package com.andrea.advanced_workout_clock

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.andrea.advanced_workout_clock.view.custom_adapters.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_layout.*


class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var viewPager: ViewPager2
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //set app the toolbar (need to add the menu)
        this.toolbar = toolBar
        this.toolbar.title = "Workout Clock"
        this.toolbar.inflateMenu(R.menu.home_menu)
        setSupportActionBar(toolbar)

        viewPager = pager

        viewPagerAdapter =
            ViewPagerAdapter(this)

        viewPager.adapter = viewPagerAdapter

        tabLayout = findViewById(R.id.tab)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when(position)
            {
                0 -> tab.text = resources.getString(R.string.timer_tab_indicator)
                1 -> tab.text = resources.getString(R.string.chrono_tab_indicator)
                2 -> tab.text = resources.getString(R.string.data_tab_indicator)
            }
        }.attach()

        //setting the dark navigation button color
        setNavigationBarButtonsColor()
    }

    //the navigation bar is white so I need dark button color
    //https://stackoverflow.com/questions/44905749/navigation-bar-buttons-color
    private fun setNavigationBarButtonsColor()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val decorView: View = this.window.decorView
            var flags = decorView.systemUiVisibility

            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR

            decorView.systemUiVisibility = flags
        }
    }
}
