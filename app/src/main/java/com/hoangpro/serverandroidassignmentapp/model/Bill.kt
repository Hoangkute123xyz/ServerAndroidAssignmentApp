package com.hoangpro.serverandroidassignmentapp.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class Bill {
    inner class ProductMap(id:String,amount:Int){
        val id =id
        val amount =amount
    }

    @SerializedName("_id")
    @Expose
    var id: String? = null

    @SerializedName("products")
    @Expose
    var products: ArrayList<ProductMap>? = null

    @SerializedName("isDone")
    @Expose
    var isDone: Boolean? = null

    @SerializedName("user")
    @Expose
    var user: String? = null

    constructor(products: Map<String,Int>, isDone: Boolean?, user: String?) {
        val productMaps = ArrayList<ProductMap>()
        for(entry in products.entries){
            productMaps.add(ProductMap(entry.key,entry.value))
        }
        this.products=productMaps
        this.isDone = isDone
        this.user = user
    }

    override fun toString(): String {
        return "Bill(id=$id, products=$products, isDone=$isDone, user=$user)"
    }

}