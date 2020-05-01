package com.hoangpro.serverandroidassignmentapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior

class MainPagerAdapter(fm:FragmentManager,behavior: Int,fragmentList:ArrayList<Fragment>) : FragmentStatePagerAdapter(fm,behavior) {
    val fragmentList = fragmentList
    override fun getItem(position: Int): Fragment =fragmentList[position]

    override fun getCount(): Int = fragmentList.size

}