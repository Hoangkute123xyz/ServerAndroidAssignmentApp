package com.hoangpro.serverandroidassignmentapp.api

import com.google.gson.JsonElement
import com.hoangpro.serverandroidassignmentapp.model.Bill
import com.hoangpro.serverandroidassignmentapp.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface API {
    @GET("/api/get-all-user")
    fun getAllUser(): Call<ArrayList<JsonElement>>

    @POST("/api/register")
    fun register(@Body user: User): Call<JsonElement>

    @POST("/api/login")
    fun login(@Body body: Map<String, String>): Call<JsonElement>

    @POST("/api/user")
    fun changeUserInformation(@Body user: User): Call<JsonElement>

    @POST("api/avatar")
    fun changeAvatar(@Body body: Map<String, String>): Call<JsonElement>

    @GET("/api/products")
    fun getAllProducts(): Call<JsonElement>

    @POST("/api/my-carts")
    fun getMyCarts(@Body body: Map<String, String>): Call<JsonElement>

    @POST("/api/remove-cart")
    fun removeCart(@Body body: Map<String, String>): Call<JsonElement>

    @POST("/api/add-cart")
    fun addCart(@Body body: Map<String, String>): Call<JsonElement>

    @POST("/api/add-bill")
    fun addBill(@Body body: Bill): Call<JsonElement>

    @POST("/api/bills")
    fun getAllBills(@Body body: Map<String,String>):Call<JsonElement>

    @POST("/api/check-bill")
    fun checkBill(@Body body: Map<String, String>):Call<JsonElement>

    @POST("/api/delete-bill")
    fun deleteBill(@Body body: Map<String, String>): Call<JsonElement>
}