package com.hoangpro.serverandroidassignmentapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.hoangpro.serverandroidassignmentapp.R
import com.hoangpro.serverandroidassignmentapp.adapter.CartAdapter
import com.hoangpro.serverandroidassignmentapp.adapter.MainPagerAdapter
import com.hoangpro.serverandroidassignmentapp.api.Client
import com.hoangpro.serverandroidassignmentapp.base.BaseActivity
import com.hoangpro.serverandroidassignmentapp.fragment.dialog.LoginBSDF
import com.hoangpro.serverandroidassignmentapp.fragment.dialog.RegisterBSDF
import com.hoangpro.serverandroidassignmentapp.fragment.fragment.BillFragment
import com.hoangpro.serverandroidassignmentapp.fragment.fragment.CartFragment
import com.hoangpro.serverandroidassignmentapp.fragment.fragment.ProductFragment
import com.hoangpro.serverandroidassignmentapp.fragment.fragment.ProfileFragment
import com.hoangpro.serverandroidassignmentapp.helper.AnimationHelper
import com.hoangpro.serverandroidassignmentapp.helper.EventState
import com.hoangpro.serverandroidassignmentapp.model.User
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.log

class MainActivity : BaseActivity() {

    private var user:User? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupUser()
        setupView()
        setupViewPager()
    }

    private fun setupUser(){
        user = preferenceHelper.getUser()
        if (user!=null){
            tvLogin.visibility=View.GONE
            tvRegister.visibility=View.GONE
            imgAvatar.visibility=View.VISIBLE
            tvFullName.visibility = View.VISIBLE
            Glide.with(this).load(user!!.avatar).into(imgAvatar)
            tvFullName.text=user!!.fullName
        } else {
            tvLogin.visibility=View.VISIBLE
            tvRegister.visibility=View.VISIBLE
            imgAvatar.visibility=View.GONE
            tvFullName.visibility = View.GONE
            AnimationHelper.scaleAnimation(tvRegister,0.95f){
                val registerBSDF = RegisterBSDF()
                registerBSDF.show(supportFragmentManager,registerBSDF.tag)
            }
            AnimationHelper.scaleAnimation(tvLogin,0.95f){
                val loginBSDF = LoginBSDF()
                loginBSDF.show(supportFragmentManager,loginBSDF.tag)
            }
        }
    }
    private fun setupViewPager() {
        if (user==null){
            setupViewPagerForUser()
        } else {
            if (user!!.role==2){
                setupViewPagerForAdmin()
            } else {
                setupViewPagerForUser()
            }
        }
    }
    private fun setupViewPagerForAdmin(){
        val fragmentList = ArrayList<Fragment>()
        fragmentList.add(ProductFragment())
        fragmentList.add(CartFragment())
        fragmentList.add(BillFragment())
        fragmentList.add(ProfileFragment())
        val mainPagerAdapter = MainPagerAdapter(supportFragmentManager,0,fragmentList)
        viewPager.adapter=mainPagerAdapter
        viewPager.offscreenPageLimit=fragmentList.size
        bottomBar.onItemSelected={
            viewPager.setCurrentItem(it,false)
            if (it==1 &&user!=null){
                tvMoney.visibility=View.VISIBLE
            } else {
                tvMoney.visibility=View.GONE
            }
        }
        bottomBar.setMenuRes(R.menu.bottom_bar_menu_admin)

    }
    private fun setupViewPagerForUser(){
        val fragmentList = ArrayList<Fragment>()
        fragmentList.add(ProductFragment())
        fragmentList.add(CartFragment())
        fragmentList.add(ProfileFragment())
        val mainPagerAdapter = MainPagerAdapter(supportFragmentManager,0,fragmentList)
        viewPager.adapter=mainPagerAdapter
        viewPager.offscreenPageLimit=fragmentList.size
        bottomBar.onItemSelected={
            viewPager.setCurrentItem(it,false)
            if (it==1 &&user!=null){
                tvMoney.visibility=View.VISIBLE
            } else {
                tvMoney.visibility=View.GONE
            }
        }
    }

    private fun setupView() {

    }

    override fun onEvent(eventState: EventState) {
        when(eventState){
            EventState.EVENT_LOGOUT->{
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }
            EventState.CHANGE_USER_INFORMATION->{
                recreate()
            }
            EventState.CHANGE_TOTAL_MONEY->{
                tvMoney.text="${getString(R.string.total_money)} ${CartAdapter.totalMoney}đ"
            }
        }
    }

}
