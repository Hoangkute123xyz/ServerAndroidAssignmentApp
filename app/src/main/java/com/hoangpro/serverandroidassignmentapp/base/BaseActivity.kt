package com.hoangpro.serverandroidassignmentapp.base

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.hoangpro.serverandroidassignmentapp.helper.EventState
import com.hoangpro.serverandroidassignmentapp.helper.PreferenceHelper
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

open class BaseActivity :AppCompatActivity(){
    lateinit var  preferenceHelper : PreferenceHelper

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        preferenceHelper = PreferenceHelper(this)
    }

    override fun onStart() {
        super.onStart()
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onEvent(eventState: EventState){

    }
}