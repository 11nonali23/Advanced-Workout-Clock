package com.example.advanced_chrono2.view.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CustomNestedScrollView (context: Context, attributeSet: AttributeSet) : NestedScrollView(context, attributeSet)
{

    override fun onNestedPreScroll(
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    )
    {
        val rv = target as RecyclerView
        if (dy < 0 && isRvScrolledToTop(
                rv
            ) || dy > 0 && !isNsvScrolledToBottom(
                this
            )
        ) {
            scrollBy(0, dy)
            consumed[1] = dy
            return
        }
        super.onNestedPreScroll(target, dx, dy, consumed, type)
    }

    companion object
    {
        private fun isNsvScrolledToBottom(nsv: NestedScrollView): Boolean {
            return !nsv.canScrollVertically(1)
        }

        private fun isRvScrolledToTop(rv: RecyclerView): Boolean {
            val lm = rv.layoutManager as LinearLayoutManager?
            return (lm!!.findFirstVisibleItemPosition() == 0
                    && lm.findViewByPosition(0)!!.top == 0)
        }
    }
}