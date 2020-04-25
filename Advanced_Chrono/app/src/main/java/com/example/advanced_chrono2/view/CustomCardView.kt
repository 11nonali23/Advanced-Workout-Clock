package com.example.advanced_chrono2.view

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