package com.hoangpro.serverandroidassignmentapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hoangpro.serverandroidassignmentapp.R
import com.hoangpro.serverandroidassignmentapp.api.Client
import com.hoangpro.serverandroidassignmentapp.helper.AnimationHelper
import com.hoangpro.serverandroidassignmentapp.helper.DialogHelper
import com.hoangpro.serverandroidassignmentapp.helper.PreferenceHelper
import com.hoangpro.serverandroidassignmentapp.model.Bill
import com.hoangpro.serverandroidassignmentapp.model.User
import kotlinx.android.synthetic.main.item_bill.view.*

class BillAdapter(list:ArrayList<Bill>, userMap:HashMap<String, User>, moneyMap:HashMap<String,Int>, context: Context) : RecyclerView.Adapter<BillAdapter.BillHolder>() {
    private val list = list
    private val context = context
    private var userMap=userMap
    private var moneyMap=moneyMap

    inner class BillHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val imgAvatar = itemView.imgAvatar
        val tvName = itemView.tvName
        val tvMoney = itemView.tvMoney
        val imgCheck = itemView.imgCheck
        val imgDelete = itemView.imgDelete
        val tvCheck = itemView.tvCheck
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillHolder =BillHolder(LayoutInflater.from(context).inflate(R.layout.item_bill,parent,false))

    override fun getItemCount(): Int =list.size

    override fun onBindViewHolder(holder: BillHolder, position: Int) {
        val bill = list[position]
        Glide.with(context).load(userMap["${bill.user}"]?.avatar).into(holder.imgAvatar)
        holder.tvName.text = userMap["${bill.user}"]?.fullName
        holder.tvMoney.text = "${moneyMap["${bill.id}"]} đ"
        if (bill.isDone!!){
            holder.tvCheck.visibility=View.VISIBLE
            holder.imgCheck.visibility=View.GONE
            holder.imgDelete.visibility=View.GONE
        } else {
            holder.tvCheck.visibility=View.GONE
            holder.imgCheck.visibility=View.VISIBLE
            holder.imgDelete.visibility=View.VISIBLE
        }
        AnimationHelper.scaleAnimation(holder.imgCheck,0.95f){
            DialogHelper.showDialogConfirm(context,"${context.getString(R.string.check_bill_confirm)}: ${moneyMap["${bill.id}"]} đ"){
                val user = PreferenceHelper(context).getUser()!!
                val dialogLoading = DialogHelper.showDialogLoading(context)
                Client.checkBill(user.userName,user.password,bill.id!!,{billList,userMap,moneyMap->
                    list.clear()
                    list.addAll(billList)
                    this.userMap.clear()
                    this.moneyMap.clear()
                    this.userMap.putAll(userMap)
                    this.moneyMap.putAll(moneyMap)
                    notifyDataSetChanged()
                    dialogLoading.dismiss()
                    Toast.makeText(context,context.getString(R.string.make_purchase_successfully),Toast.LENGTH_SHORT).show()
                },{
                    Toast.makeText(context,context.getString(R.string.an_occured_error),Toast.LENGTH_SHORT).show()
                    dialogLoading.dismiss()
                })
            }
        }

        AnimationHelper.scaleAnimation(holder.imgDelete,0.95f){
            DialogHelper.showDialogConfirm(context,"${context.getString(R.string.delete_bill_confirm)}?"){
                val user = PreferenceHelper(context).getUser()!!
                val dialogLoading = DialogHelper.showDialogLoading(context)
                Client.deleteBill(user.userName,user.password,bill.id!!,{billList,userMap,moneyMap->
                    list.clear()
                    list.addAll(billList)
                    this.userMap.clear()
                    this.moneyMap.clear()
                    this.userMap.putAll(userMap)
                    this.moneyMap.putAll(moneyMap)
                    notifyDataSetChanged()
                    dialogLoading.dismiss()
                    Toast.makeText(context,context.getString(R.string.make_purchase_successfully),Toast.LENGTH_SHORT).show()
                },{
                    Toast.makeText(context,context.getString(R.string.an_occured_error),Toast.LENGTH_SHORT).show()
                    dialogLoading.dismiss()
                })
            }
        }
    }
}