package com.andrea.advanced_workout_clock.view.custom_views

import android.content.Context
import android.util.AttributeSet
import androidx.cardview.widget.CardView

class CustomCardView(context: Context, attributeSet: AttributeSet) : CardView(context, attributeSet)
{
    //This function will not make the rounded corners fade
    override fun setBackgroundColor(color: Int)
    {
        setCardBackgroundColor(color)
    }
}