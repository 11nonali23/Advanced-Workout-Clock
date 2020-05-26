package com.example.advanced_chrono2.view.custom_adapters

interface ItemTouchHelperAdapter
{
    fun onItemMove(fromPosition: Int, toPosition: Int)
    fun onItemDismiss(position: Int)
}