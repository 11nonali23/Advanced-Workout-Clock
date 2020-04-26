package com.example.advanced_chrono2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.WindowManager
import androidx.viewpager2.widget.ViewPager2
import com.example.advanced_chrono2.view.adapters.ViewPagerAdapter
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


        //TODO mettere a posto correttamente
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    //Setting the options menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean
    {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }
}
