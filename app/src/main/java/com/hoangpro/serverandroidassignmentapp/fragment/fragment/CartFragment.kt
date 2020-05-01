package com.hoangpro.serverandroidassignmentapp.fragment.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.hoangpro.serverandroidassignmentapp.R
import com.hoangpro.serverandroidassignmentapp.adapter.CartAdapter
import com.hoangpro.serverandroidassignmentapp.api.Client
import com.hoangpro.serverandroidassignmentapp.base.BaseFragment
import com.hoangpro.serverandroidassignmentapp.helper.AnimationHelper
import com.hoangpro.serverandroidassignmentapp.helper.DialogHelper
import com.hoangpro.serverandroidassignmentapp.helper.EventState
import com.hoangpro.serverandroidassignmentapp.helper.MessageEvent
import com.hoangpro.serverandroidassignmentapp.model.Bill
import com.hoangpro.serverandroidassignmentapp.model.Product
import com.hoangpro.serverandroidassignmentapp.model.User
import kotlinx.android.synthetic.main.fragment_cart.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class CartFragment : BaseFragment() {
    private lateinit var cartAdapter: CartAdapter
    private lateinit var cartMap: Map<String, Int>
    private var listProduct = ArrayList<Product>()
    private var user: User? = null

    override fun getLayout(): Int = R.layout.fragment_cart
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        user = preferenceHelper.getUser()
        cartAdapter = CartAdapter(listProduct, requireContext())
        rvProduct.adapter = cartAdapter
        rvProduct.layoutManager = LinearLayoutManager(requireContext())
        swipeToRefresh.setColorSchemeResources(R.color.colorPrimary)
        tvPlaceHolder.visibility = View.VISIBLE
        EventBus.getDefault().post(EventState.CHANGE_TOTAL_MONEY)
        if (user == null) {
            btnSubmit.visibility = View.GONE
            tvPlaceHolder.text = getString(R.string.login_to_shopping)
        } else {
            btnSubmit.visibility = View.VISIBLE
            tvPlaceHolder.text = getString(R.string.empty_cart)
            if (user!!.cart == null) return
            if (user!!.cart!!.size > 0) {
                fetchData()
            }
        }

        AnimationHelper.scaleAnimation(btnSubmit, 0.95f) {
            if (listProduct.size > 0) {
                val bill = Bill(cartAdapter.cartMap, false, user!!._id)
                val dialogLoading = DialogHelper.showDialogLoading(requireContext())
                Client.addBill(bill,
                    {
                        preferenceHelper.setUser(it)
                        listProduct.clear()
                        cartAdapter.notifyDataSetChanged()
                        cartAdapter.createCartMap()
                        dialogLoading.dismiss()
                        Snackbar.make(
                            swipeToRefresh,
                            R.string.bill_has_been_created,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }, {
                        dialogLoading.dismiss()
                        Snackbar.make(
                            swipeToRefresh,
                            R.string.an_occured_error,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    })
            } else {
                Snackbar.make(swipeToRefresh, R.string.empty_cart, Snackbar.LENGTH_SHORT).show()
            }
        }
        swipeToRefresh.setOnRefreshListener {
            fetchData()
        }
    }

    private fun fetchData() {
        if (user != null) {
            swipeToRefresh.isRefreshing = true
            Client.getMyCarts(user!!.userName, user!!.password, {
                if (it.size > 0) {
                    listProduct.clear()
                    listProduct.addAll(it)
                    cartAdapter.notifyDataSetChanged()
                    if (listProduct.size == 0) {
                        tvPlaceHolder.visibility = View.VISIBLE
                    }
                    cartAdapter.createCartMap()
                    tvPlaceHolder.visibility = View.GONE
                } else {
                    tvPlaceHolder.visibility = View.VISIBLE
                }
                swipeToRefresh.isRefreshing = false
            }, {
                Snackbar.make(rvProduct, R.string.an_occured_error, Snackbar.LENGTH_SHORT)
                    .show()
                swipeToRefresh.isRefreshing = false
            })
        } else {
            swipeToRefresh.isRefreshing = false
        }
    }

    override fun onEvent(eventState: EventState) {
        super.onEvent(eventState)
        when (eventState) {
            EventState.EVENT_LOGOUT -> {
                setupView()
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(messageEvent: MessageEvent) {
        when (messageEvent.eventState) {
            EventState.ADD_CART -> {
                val productsList = messageEvent.data as ArrayList<Product>
                listProduct.clear()
                listProduct.addAll(productsList)
                cartAdapter.notifyDataSetChanged()
                cartAdapter.createCartMap()
                EventBus.getDefault().post(EventState.CHANGE_TOTAL_MONEY)
            }
        }
    }
}