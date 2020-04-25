package com.example.advanced_chrono2.model

import android.content.Context
import com.example.advanced_chrono2.R

//This class is used beacuse I don't want to delete the first item of the arrayList

class ActivityArrayList(context: Context?) : ArrayList<TimerActivityData>()
{
    init
    {
        if(context != null)
            add(TimerActivityData(context!!.resources.getString(R.string.addNewItem)))
        else
            add(TimerActivityData("Add Activity"))
    }


    override fun clear() { val temp = get(0); super.clear(); add(temp) }
}