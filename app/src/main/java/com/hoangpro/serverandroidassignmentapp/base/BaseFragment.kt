package com.hoangpro.serverandroidassignmentapp.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hoangpro.serverandroidassignmentapp.helper.EventState
import com.hoangpro.serverandroidassignmentapp.helper.PreferenceHelper
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.zip.Inflater

open abstract class BaseFragment :Fragment(){
    lateinit var preferenceHelper: PreferenceHelper
    override fun onStart() {
        super.onStart()
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        preferenceHelper = PreferenceHelper(requireContext())
        return inflater.inflate(getLayout(),container,false)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onEvent(eventState: EventState){

    }
    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    abstract fun getLayout():Int
}