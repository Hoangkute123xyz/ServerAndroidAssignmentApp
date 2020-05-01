package com.hoangpro.serverandroidassignmentapp.fragment.dialog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.hoangpro.serverandroidassignmentapp.R
import com.hoangpro.serverandroidassignmentapp.activity.MainActivity
import com.hoangpro.serverandroidassignmentapp.api.Client
import com.hoangpro.serverandroidassignmentapp.base.BaseBSDF
import com.hoangpro.serverandroidassignmentapp.helper.AnimationHelper
import com.hoangpro.serverandroidassignmentapp.helper.DialogHelper
import kotlinx.android.synthetic.main.bsdf_login.*

class LoginBSDF :BaseBSDF(){
    private var userName=""
    private var password=""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bsdf_login,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        AnimationHelper.scaleAnimation(btnSubmit,0.95f){
            val dialogLoading = DialogHelper.showDialogLoading(requireContext())
            if (getData()){
                Client.login(
                    userName,
                    password,
                    onSuccess = {
                        preferenceHelper.setUser(it)
                        dialogLoading.dismiss()
                        dismiss()
                        startActivity(Intent(activity,MainActivity::class.java))
                        requireActivity()!!.finish()
                    },
                    onFailed = {
                        dialogLoading.dismiss()
                        Snackbar.make(constraintContent,R.string.an_occured_error,Snackbar.LENGTH_SHORT).show()
                    }
                )
            }
        }
        AnimationHelper.scaleAnimation(imgBack,0.95f){
            dismiss()
        }
    }

    private fun getData():Boolean{
        userName = edtUserName.text.toString()
        password = edtPassword.text.toString()
        if (userName.isNullOrEmpty())
            return false
        if (password.isNullOrEmpty())
            return false
        return true
    }
}