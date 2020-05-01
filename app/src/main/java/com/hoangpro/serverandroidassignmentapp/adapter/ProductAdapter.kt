package com.hoangpro.serverandroidassignmentapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.hoangpro.serverandroidassignmentapp.R
import com.hoangpro.serverandroidassignmentapp.api.Client
import com.hoangpro.serverandroidassignmentapp.helper.*
import com.hoangpro.serverandroidassignmentapp.model.Product
import com.hoangpro.serverandroidassignmentapp.model.ProductType
import com.hoangpro.serverandroidassignmentapp.model.User
import kotlinx.android.synthetic.main.item_product.view.*
import org.greenrobot.eventbus.EventBus

class ProductAdapter(
    context: Context,
    list: ArrayList<Product>,
    productTypeMap: Map<String, ProductType>
) : RecyclerView.Adapter<ProductAdapter.ProductHolder>() {
    inner class ProductHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgThumb = itemView.imgThumb
        val tvName = itemView.tvName
        val tvProductType = itemView.tvProductType
        val tvPrice = itemView.tvPrice
        val tvAmount = itemView.tvAmount
    }

    private val context = context
    private val list = list
    private val productTypeMap = productTypeMap
    private val preferenceHelper = PreferenceHelper(context)
    private var user: User? = null

    init {
        user = preferenceHelper.getUser()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder =
        ProductHolder(LayoutInflater.from(context).inflate(R.layout.item_product, parent, false))

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        val product = list[position]
        Glide.with(context).load(product.productThumb).into(holder.imgThumb)
        holder.tvName.text = product.name
        holder.tvPrice.text = "${product.price} đ"
        holder.tvAmount.text = "Số lượng: ${product.amount}"
        holder.tvProductType.text = productTypeMap["${product.productType}"]?.name
        AnimationHelper.scaleAnimation(holder.itemView, 0.95f) {
            if (user != null) {
                if (!Gson().toJson(preferenceHelper.getUser()!!.cart!!).contains(product.id!!)) {
                    val dialogLoading = DialogHelper.showDialogLoading(context)
                    Client.addCart(preferenceHelper.getUser()!!.userName, user!!.password, product.id!!,
                        { user, products ->
                            preferenceHelper.setUser(user)
                            EventBus.getDefault().post(MessageEvent(EventState.ADD_CART, products))
                            Toast.makeText(
                                context,
                                R.string.product_has_been_added_to_cart,
                                Toast.LENGTH_SHORT
                            ).show()
                            dialogLoading.dismiss()
                        }, {
                            dialogLoading.dismiss()
                            Toast.makeText(context, R.string.an_occured_error, Toast.LENGTH_SHORT)
                                .show()
                        })
                } else {
                    Toast.makeText(
                        context,
                        R.string.product_has_been_added_to_cart,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}