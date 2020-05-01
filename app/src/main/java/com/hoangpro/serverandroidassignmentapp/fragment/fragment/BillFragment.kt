package com.hoangpro.serverandroidassignmentapp.fragment.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.hoangpro.serverandroidassignmentapp.R
import com.hoangpro.serverandroidassignmentapp.adapter.BillAdapter
import com.hoangpro.serverandroidassignmentapp.api.Client
import com.hoangpro.serverandroidassignmentapp.base.BaseFragment
import com.hoangpro.serverandroidassignmentapp.model.Bill
import com.hoangpro.serverandroidassignmentapp.model.User
import kotlinx.android.synthetic.main.fragment_bill.*

class BillFragment : BaseFragment() {
    override fun getLayout(): Int = R.layout.fragment_bill
    private lateinit var billAdapter: BillAdapter
    private val userMap = HashMap<String, User>()
    private val moneyMap = HashMap<String, Int>()
    private val billList = ArrayList<Bill>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        fetchData()
    }

    private fun fetchData() {
        val user = preferenceHelper.getUser()
        swipeToRefresh.isRefreshing = true
        Client.getAllBill(user!!.userName, user.password, { billList, userMap, moneyMap ->
            this.billList.clear()
            this.billList.addAll(billList)
            this.userMap.clear()
            this.userMap.putAll(userMap)
            this.moneyMap.clear()
            this.moneyMap.putAll(moneyMap)
            billAdapter.notifyDataSetChanged()
            if (billList.size == 0) {
                tvPlaceHolder.visibility = View.VISIBLE
            } else {
                tvPlaceHolder.visibility = View.GONE
            }
            swipeToRefresh.isRefreshing = false
        }, {
            swipeToRefresh.isRefreshing = false
            Snackbar.make(rvBill, R.string.an_occured_error, Snackbar.LENGTH_SHORT).show()
        })
    }

    private fun setupView() {
        swipeToRefresh.setColorSchemeResources(R.color.colorPrimary)
        billAdapter = BillAdapter(billList, userMap, moneyMap, requireContext())
        rvBill.adapter = billAdapter
        rvBill.layoutManager = LinearLayoutManager(requireContext())
        swipeToRefresh.setOnRefreshListener {
            fetchData()
        }
    }
}