package com.hoangpro.serverandroidassignmentapp.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class CustomViewPager(context: Context,attrs:AttributeSet) :ViewPager(context,attrs){
    var hasScroll=false
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return hasScroll && super.onTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return hasScroll && super.onInterceptTouchEvent(ev)
    }
}