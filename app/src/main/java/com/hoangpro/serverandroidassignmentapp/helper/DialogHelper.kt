package com.hoangpro.serverandroidassignmentapp.helper

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.hoangpro.serverandroidassignmentapp.R
import kotlinx.android.synthetic.main.dialog_confirm.view.*

class DialogHelper {
    companion object{
        fun showDialogLoading(context:Context):Dialog{
            val builder = AlertDialog.Builder(context)
                .setView(LayoutInflater.from(context).inflate(R.layout.dialog_loading,null,false))
            val alertDialog = builder.create()
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.setCancelable(false)
            alertDialog.show()
            return alertDialog
        }
        fun showDialogConfirm(context: Context,message:String,onAccept:()->Unit){
            val builder = AlertDialog.Builder(context)
            val view = LayoutInflater.from(context).inflate(R.layout.dialog_confirm,null,false)
            builder.setView(view)
            val tvAccept = view.tvOk
            val tvCancel = view.tvCancel
            val tvTitle = view.tvTitle
            tvTitle.text=message
            val alertDialog =builder.create()
            AnimationHelper.scaleAnimation(tvCancel,0.95f){
                alertDialog.dismiss()
            }
            AnimationHelper.scaleAnimation(tvAccept,0.95f){
                onAccept()
                alertDialog.dismiss()
            }
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.show()
        }
    }
}