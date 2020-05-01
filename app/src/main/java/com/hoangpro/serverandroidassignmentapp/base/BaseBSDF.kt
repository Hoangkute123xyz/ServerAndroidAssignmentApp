package com.hoangpro.serverandroidassignmentapp.base

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hoangpro.serverandroidassignmentapp.helper.PreferenceHelper

open class BaseBSDF :BottomSheetDialogFragment(){
    lateinit var preferenceHelper: PreferenceHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceHelper = PreferenceHelper(requireContext())
    }
}