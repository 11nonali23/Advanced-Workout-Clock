package com.andrea.advanced_workout_clock.view.custom_adapters

interface ItemTouchHelperAdapter
{
    fun onItemMove(fromPosition: Int, toPosition: Int)
    fun onItemDismiss(position: Int)
}