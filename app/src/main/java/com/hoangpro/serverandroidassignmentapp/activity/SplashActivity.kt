package com.hoangpro.serverandroidassignmentapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.hoangpro.serverandroidassignmentapp.R
import com.hoangpro.serverandroidassignmentapp.api.Client
import com.hoangpro.serverandroidassignmentapp.base.BaseActivity

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val user = preferenceHelper.getUser()
        if (user!=null) {
            Client.login(
                user.userName,
                user.password,
                onSuccess = {
                    preferenceHelper.setUser(it)
                    startActivity(Intent(applicationContext,MainActivity::class.java))
                    finish()
                },
                onFailed = {
                    startActivity(Intent(applicationContext,MainActivity::class.java))
                    finish()
                }
            )
        } else {
            object :CountDownTimer(3000,3000){
                override fun onFinish() {
                    startActivity(Intent(applicationContext,MainActivity::class.java))
                    finish()
                }

                override fun onTick(millisUntilFinished: Long) {

                }
            }.start()
        }
    }
}
