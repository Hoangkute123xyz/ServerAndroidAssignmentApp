package com.hoangpro.serverandroidassignmentapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hoangpro.serverandroidassignmentapp.R
import com.hoangpro.serverandroidassignmentapp.api.Client
import com.hoangpro.serverandroidassignmentapp.helper.DialogHelper
import com.hoangpro.serverandroidassignmentapp.helper.EventState
import com.hoangpro.serverandroidassignmentapp.helper.PreferenceHelper
import com.hoangpro.serverandroidassignmentapp.model.Product
import com.hoangpro.serverandroidassignmentapp.model.User
import kotlinx.android.synthetic.main.item_cart.view.*
import org.greenrobot.eventbus.EventBus

class CartAdapter(productList: ArrayList<Product>, context: Context) :
    RecyclerView.Adapter<CartAdapter.CartHolder>() {
    inner class CartHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName = itemView.tvName
        val imgThumb = itemView.imgThumb
        val numberView = itemView.numberView
        val tvPrice = itemView.tvPrice
    }

    companion object {
        var totalMoney = 0
    }

    private var context = context
    private var productsList = productList
    var cartMap = HashMap<String, Int>()
    private val preferenceHelper = PreferenceHelper(context)
    private var user: User? = null

    init {
        user = preferenceHelper.getUser()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartHolder =
        CartHolder(LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false))

    override fun getItemCount(): Int = productsList.size

    override fun onBindViewHolder(holder: CartHolder, position: Int) {
        val product = productsList[position]
        holder.tvName.text = product.name
        holder.tvPrice.text = "${product.price} đ"
        Glide.with(context).load(product.productThumb).into(holder.imgThumb)
        holder.numberView.onNumberChange = { newNumber, oldNumber ->
            cartMap["${product.id}"] = newNumber
            totalMoney = totalMoney - oldNumber * product.price!! + newNumber * product.price!!
            EventBus.getDefault().post(EventState.CHANGE_TOTAL_MONEY)
        }
        holder.numberView.max = product.amount!!
        holder.itemView.setOnLongClickListener {
            DialogHelper.showDialogConfirm(
                context,
                context.getString(R.string.delete_cart_confirm),
                onAccept = {
                    val dialogLoading = DialogHelper.showDialogLoading(context)
                    Client.removeCart(
                        user!!.userName,
                        user!!.password,
                        product.id!!,
                        onSuccess = { user, products ->
                            preferenceHelper.setUser(user)
                            productsList.clear()
                            productsList.addAll(products)
                            notifyDataSetChanged()
                            createCartMap()
                            dialogLoading.dismiss()
                        },onFailed = {
                            Toast.makeText(context,R.string.an_occured_error,Toast.LENGTH_SHORT).show()
                            dialogLoading.dismiss()
                        })
                })
            return@setOnLongClickListener true
        }
    }

    fun createCartMap() {
        cartMap.clear()
        totalMoney = 0
        for (product in productsList) {
            cartMap["${product.id}"] = 1
            totalMoney += product.price!!
        }
        EventBus.getDefault().post(EventState.CHANGE_TOTAL_MONEY)
    }
}