package com.hoangpro.serverandroidassignmentapp.fragment.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.hoangpro.serverandroidassignmentapp.R
import com.hoangpro.serverandroidassignmentapp.adapter.ProductAdapter
import com.hoangpro.serverandroidassignmentapp.api.Client
import com.hoangpro.serverandroidassignmentapp.base.BaseFragment
import com.hoangpro.serverandroidassignmentapp.helper.DialogHelper
import com.hoangpro.serverandroidassignmentapp.model.Product
import com.hoangpro.serverandroidassignmentapp.model.ProductType
import kotlinx.android.synthetic.main.fragment_product.*

class ProductFragment :BaseFragment(){
    private lateinit var productAdapter: ProductAdapter
    private val productsList = ArrayList<Product>()
    private val productsTypeMap = HashMap<String,ProductType>()
    override fun getLayout(): Int = R.layout.fragment_product

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        fetchData()
    }

    private fun fetchData() {
        swipeToRefresh.isRefreshing=true
        Client.getAllProducts(
            onSuccess = { products, productType ->
                productsList.clear()
                productsTypeMap.clear()
                productsList.addAll(products)
                productsTypeMap.putAll(productType)
                productAdapter.notifyDataSetChanged()
                swipeToRefresh.isRefreshing=false
            },
            onFailed = {
                swipeToRefresh.isRefreshing=false
                Snackbar.make(rvProduct,R.string.an_occured_error,Snackbar.LENGTH_SHORT).show()
            }
        )
    }

    private fun setupView() {
        productAdapter = ProductAdapter(requireContext(),productsList,productsTypeMap)
        rvProduct.adapter=productAdapter
        rvProduct.layoutManager = GridLayoutManager(requireContext(),2)
        swipeToRefresh.setColorSchemeResources(R.color.colorPrimary)
        swipeToRefresh.setOnRefreshListener {
            fetchData()
        }
    }
}