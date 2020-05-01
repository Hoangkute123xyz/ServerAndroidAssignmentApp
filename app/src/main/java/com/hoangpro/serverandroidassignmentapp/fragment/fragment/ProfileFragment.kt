package com.hoangpro.serverandroidassignmentapp.fragment.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import com.google.android.material.snackbar.Snackbar
import com.hoangpro.serverandroidassignmentapp.R
import com.hoangpro.serverandroidassignmentapp.api.Client
import com.hoangpro.serverandroidassignmentapp.base.BaseFragment
import com.hoangpro.serverandroidassignmentapp.helper.AnimationHelper
import com.hoangpro.serverandroidassignmentapp.helper.DialogHelper
import com.hoangpro.serverandroidassignmentapp.helper.EventState
import com.hoangpro.serverandroidassignmentapp.helper.ImageHelper
import kotlinx.android.synthetic.main.fragment_profile.*
import org.greenrobot.eventbus.EventBus

class ProfileFragment :BaseFragment(){
    private lateinit var imagePicker: ImagePicker
    override fun getLayout(): Int  = R.layout.fragment_profile

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        imagePicker = ImagePicker.create(this)
            .returnMode(ReturnMode.ALL)
            .toolbarArrowColor(ContextCompat.getColor(requireActivity(),R.color.colorWhite))
            .toolbarImageTitle(getString(R.string.choose_avatar))
            .includeVideo(false)
            .single()
            .showCamera(true)
            .theme(R.style.AppTheme)
            .enableLog(false)
    }

    override fun onEvent(eventState: EventState) {
        super.onEvent(eventState)
        when(eventState){
            EventState.CHANGE_USER_INFORMATION->{
                setupView()
            }
        }
    }
    private fun setupView() {
        val user = preferenceHelper.getUser()
        if (user!=null){
            if (user.role==2){
                imgVip.visibility=View.VISIBLE
                tvFullName.setTextColor(Color.RED)
            } else {
                imgVip.visibility=View.GONE
            }
            tvPlaceHolder.visibility=View.GONE
            constraintContent.visibility=View.VISIBLE
            Glide.with(requireActivity()).load(user.avatar).into(imgAvatar)
            tvFullName.text=user.fullName
            tvUserName.text=user.userName
            tvAddress.text=user.address
            tvPhoneNumber.text=user.phoneNumber
            AnimationHelper.scaleAnimation(imgLogout,0.95f){
                DialogHelper.showDialogConfirm(requireContext(),getString(R.string.logout_confirm)){
                    preferenceHelper.setUser(null)
                    EventBus.getDefault().post(EventState.EVENT_LOGOUT)
                }
            }
            AnimationHelper.scaleAnimation(imgAvatar,0.95f){
                imagePicker.start()
            }
        } else {
            tvPlaceHolder.visibility=View.VISIBLE
            constraintContent.visibility=View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (ImagePicker.shouldHandle(requestCode,resultCode,data)){
            val image = ImagePicker.getFirstImageOrNull(data)
            Log.e("path",image.path)
            val imageData = ImageHelper.getImageDataFormFilePath(image.path)
            val dialogLoading = DialogHelper.showDialogLoading(requireContext())
            val user = preferenceHelper.getUser()
            Client.changeAvatar(user!!._id,imageData,{
                preferenceHelper.setUser(it)
                EventBus.getDefault().post(EventState.CHANGE_USER_INFORMATION)
                dialogLoading.dismiss()
                Snackbar.make(constraintContent,R.string.change_avatar_successfully,Snackbar.LENGTH_SHORT).show()
            },{
                dialogLoading.dismiss()
                Snackbar.make(constraintContent,R.string.an_occured_error,Snackbar.LENGTH_SHORT).show()
            })
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}