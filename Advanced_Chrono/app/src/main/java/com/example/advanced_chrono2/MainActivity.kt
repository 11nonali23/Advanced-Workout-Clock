package com.example.advanced_chrono2

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.advanced_chrono2.view.custom_adapters.ViewPagerAdapter
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
                0 -> tab.text = resources.getString(R.string.chrono_tab_indicator)
                1 -> tab.text = resources.getString(R.string.timer_tab_indicator)
                2 -> tab.text = resources.getString(R.string.data_tab_indicator)
            }
        }.attach()
    }

    //basically this override function is used to hide the navigation bar
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        hideNavBar()
    }

    private fun hideNavBar()
    {
        val currentApiVersion = Build.VERSION.SDK_INT

        if (currentApiVersion >= Build.VERSION_CODES.KITKAT)
        {
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        }
    }
}
