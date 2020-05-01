package com.hoangpro.serverandroidassignmentapp.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class Product {
    @SerializedName("_id")
    @Expose
    var id: String? = null

    @SerializedName("dayImport")
    @Expose
    var dayImport: String? = null

    @SerializedName("dayExpr")
    @Expose
    var dayExpr: String? = null

    @SerializedName("productThumb")
    @Expose
    var productThumb: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("productType")
    @Expose
    var productType: String? = null

    @SerializedName("amount")
    @Expose
    var amount: Int? = null

    @SerializedName("price")
    @Expose
    var price: Int? = null

    constructor(
        dayImport: String?,
        dayExpr: String?,
        productThumb: String?,
        name: String?,
        productType: String?,
        amount: Int?,
        price: Int?
    ) {
        this.dayImport = dayImport
        this.dayExpr = dayExpr
        this.productThumb = productThumb
        this.name = name
        this.productType = productType
        this.amount = amount
        this.price = price
    }
}