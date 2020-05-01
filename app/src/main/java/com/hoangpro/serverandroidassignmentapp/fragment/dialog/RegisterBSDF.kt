package com.hoangpro.serverandroidassignmentapp.fragment.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.hoangpro.serverandroidassignmentapp.R
import com.hoangpro.serverandroidassignmentapp.api.Client
import com.hoangpro.serverandroidassignmentapp.base.BaseBSDF
import com.hoangpro.serverandroidassignmentapp.helper.AnimationHelper
import com.hoangpro.serverandroidassignmentapp.helper.DialogHelper
import com.hoangpro.serverandroidassignmentapp.helper.EventState
import com.hoangpro.serverandroidassignmentapp.model.User
import kotlinx.android.synthetic.main.bsdf_register.*
import org.greenrobot.eventbus.EventBus

class RegisterBSDF: BaseBSDF(){
    private var userName=""
    private var fullName=""
    private var password=""
    private var address=""
    private var phoneNumber=""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bsdf_register,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        AnimationHelper.scaleAnimation(imgBack,0.95f){
            dismiss()
        }
        AnimationHelper.scaleAnimation(btnSubmit,0.95f){
            if (getData()){
                val loadingDialog = DialogHelper.showDialogLoading(requireContext())
                Client.register(User(userName,1,fullName,password,address,phoneNumber,null,null),
                onSuccess = {
                    dismiss()
                    loadingDialog.dismiss()
                    Snackbar.make(constraintContent,R.string.register_successfully,Snackbar.LENGTH_SHORT).show()
                    preferenceHelper.setUser(it)
                    EventBus.getDefault().post(EventState.CHANGE_USER_INFORMATION)
                },
                onFailed = {
                    loadingDialog.dismiss()
                    Snackbar.make(constraintContent,R.string.an_occured_error,Snackbar.LENGTH_SHORT).show()
                })
            } else {
                Snackbar.make(constraintContent,R.string.checking_field,Snackbar.LENGTH_SHORT).show()
            }
        }
    }
    private fun getData():Boolean{
        userName = edtUserName.text.toString()
        fullName = edtFullName.text.toString()
        password = edtPassword.text.toString()
        address = edtAddress.text.toString()
        phoneNumber = edtPhoneNumber.text.toString()
        val rePassword= edtRePassword.text.toString()

        if (userName.isNullOrEmpty())
            return false
        if (fullName.isNullOrEmpty())
            return false
        if (password.isNullOrEmpty())
            return false
        if (rePassword.isNullOrEmpty())
            return false
        if (password!=rePassword){
            return false
        }
        if (address.isNullOrEmpty())
            return false
        if (phoneNumber.isNullOrEmpty())
            return false
        return true
    }

}