package com.example.advanced_chrono2.view.custom_views

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.advanced_chrono2.R
import com.example.advanced_chrono2.view.custom_adapters.ActivityTimingsAdapter


class CustomDialog(context: Context) : Dialog(context)
{
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: ActivityTimingsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.custom_dialog_layout)
        setCanceledOnTouchOutside(true)
        //setting rounded background
        window?.setBackgroundDrawableResource(R.drawable.rounded_dialog)

        //setting transparent background
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        viewAdapter = ActivityTimingsAdapter()

        recyclerView = findViewById(R.id.itemsList)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = viewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)

        //TODO set to match parent also height but make setCanceledOnTouchOutside work

        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        //setting the dialog to match parent. Margins wil be set up with inset
        /*margins and color of the dialog*/
        val back = ColorDrawable(Color.WHITE)
        val inset = InsetDrawable(back, 100)
        window?.setBackgroundDrawable(inset)

    }

    fun updateAdapter()
    {
        Log.e("dialog", "received update request")
        viewAdapter.autoUpdateCurrentTimings()
        viewAdapter.notifyDataSetChanged()
    }
}
