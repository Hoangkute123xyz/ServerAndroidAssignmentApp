package com.hoangpro.serverandroidassignmentapp.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class ProductType {
    @SerializedName("_id")
    @Expose
    var id: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    constructor(id: String?, description: String?, name: String?) {
        this.id = id
        this.description = description
        this.name = name
    }

    override fun toString(): String {
        return "ProductType(id=$id, description=$description, name=$name)"
    }

}