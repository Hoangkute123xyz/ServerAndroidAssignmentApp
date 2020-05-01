package com.hoangpro.serverandroidassignmentapp.api

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.hoangpro.serverandroidassignmentapp.model.Bill
import com.hoangpro.serverandroidassignmentapp.model.Product
import com.hoangpro.serverandroidassignmentapp.model.ProductType
import com.hoangpro.serverandroidassignmentapp.model.User
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Client {
    companion object {
        val RESULT_OK = 200
        private val mAPI by lazy { create() }
        private fun create(): API {
            val retrofit = Retrofit.Builder().baseUrl("http://192.168.100.103:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(API::class.java)
        }

        //get all user
//        fun getAllUser(onSuccess:(users:ArrayList<User>)->Unit,onFailed:()->Unit){
//            mAPI.getAllUser().enqueue(object :Callback<ArrayList<User>>{
//                override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
//                    onFailed()
//                }
//
//                override fun onResponse(
//                    call: Call<ArrayList<User>>,
//                    response: Response<ArrayList<User>>
//                ) {
//                    if (response.isSuccessful){
//                        onSuccess(response.body()!!)
//                    }
//                }
//            })
//        }
        //register
        fun register(user: User, onSuccess: (user: User) -> Unit, onFailed: () -> Unit) {
            mAPI.register(user).enqueue(object : Callback<JsonElement> {
                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    onFailed()
                    Log.e("Retrofit", t.message)
                }

                override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                    if (response.isSuccessful) {
                        val res = response.body() ?: return
                        val body = JSONObject(res.toString())
                        val status = body.getInt("status")
                        if (status == RESULT_OK) {
                            val user = Gson().fromJson(body.getString("result"), User::class.java)
                            onSuccess(user)
                        } else {
                            onFailed()
                        }
                    }
                }
            })
        }

        //login
        fun login(
            userName: String,
            password: String,
            onSuccess: (user: User) -> Unit,
            onFailed: () -> Unit
        ) {
            val body = HashMap<String, String>()
            body["userName"] = userName
            body["password"] = password
            mAPI.login(body).enqueue(object : Callback<JsonElement> {
                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    onFailed()
                }

                override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                    if (response.isSuccessful) {

                        val res = response.body() ?: return
                        val json = JSONObject(res.toString())
                        val status = json.getInt("status")
                        if (status == RESULT_OK) {
                            val user = Gson().fromJson(json.getString("result"), User::class.java)
                            onSuccess(user)
                        } else {
                            onFailed()
                        }
                    }
                }
            })
        }

        fun changeUserInformation(
            user: User,
            onSuccess: (user: User) -> Unit,
            onFailed: () -> Unit
        ) {
            mAPI.changeUserInformation(user).enqueue(object : Callback<JsonElement> {
                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    onFailed()
                }

                override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                    if (response.isSuccessful) {
                        val res = response.body() ?: return
                        val json = JSONObject(res.toString())
                        val status = json.getInt("status")
                        if (status == RESULT_OK) {
                            val user = Gson().fromJson(json.getString("result"), User::class.java)
                            onSuccess(user)
                        } else {
                            onFailed()
                        }
                    } else {
                        onFailed()
                    }
                }
            })
        }

        fun changeAvatar(
            id: String,
            imageData: String,
            onSuccess: (user: User) -> Unit,
            onFailed: () -> Unit
        ) {
            val body = HashMap<String, String>()
            body["_id"] = id
            body["imageData"] = imageData
            mAPI.changeAvatar(body).enqueue(object : Callback<JsonElement> {
                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    onFailed()
                }

                override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                    if (response.isSuccessful) {
                        val res = response.body() ?: return
                        val json = JSONObject(res.toString())
                        val status = json.getInt("status")
                        if (status == RESULT_OK) {
                            val user = Gson().fromJson(json.getString("result"), User::class.java)
                            onSuccess(user)
                        } else {
                            onFailed()
                        }
                    }
                }
            })
        }

        fun getAllProducts(
            onSuccess: (products: ArrayList<Product>, productTypes: Map<String, ProductType>) -> Unit,
            onFailed: () -> Unit
        ) {
            mAPI.getAllProducts().enqueue(object : Callback<JsonElement> {
                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    onFailed()
                }

                override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                    if (response.isSuccessful) {
                        val res = response.body() ?: return
                        val json = JSONObject(res.toString())
                        val status = json.getInt("status")
                        if (status == RESULT_OK) {
                            val products = Gson().fromJson(
                                json.getString("products"),
                                Array<Product>::class.java
                            )
                            val productArrayList = ArrayList<Product>()
                            productArrayList.addAll(products)
                            val productTypes = Gson().fromJson(
                                json.getString("productTypes"),
                                Array<ProductType>::class.java
                            )
                            val productTypesArrayList = ArrayList<ProductType>()
                            productTypesArrayList.addAll(productTypes)
                            val productMap = HashMap<String, ProductType>()
                            for (productType in productTypesArrayList) {
                                productMap["${productType.id}"] = productType
                            }
                            onSuccess(productArrayList, productMap)
                        } else {
                            onFailed()
                        }
                    } else {
                        onFailed()
                    }
                }
            })
        }

        fun getMyCarts(
            userName: String,
            password: String,
            onSuccess: (products: ArrayList<Product>) -> Unit,
            onFailed: () -> Unit
        ) {
            val body = HashMap<String, String>()
            body["userName"] = userName
            body["password"] = password
            mAPI.getMyCarts(body).enqueue(object : Callback<JsonElement> {
                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    onFailed()
                }

                override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                    if (response.isSuccessful) {
                        val res = response.body() ?: return
                        val json = JSONObject(res.toString())
                        val status = json.getInt("status")
                        if (status == RESULT_OK) {
                            val productsList = Gson().fromJson(
                                json.getString("products"),
                                Array<Product>::class.java
                            )
                            val productArrayList = ArrayList<Product>()
                            productArrayList.addAll(productsList)
                            onSuccess(productArrayList)
                        } else {
                            onFailed()
                        }
                    } else {
                        onFailed()
                    }
                }
            })
        }

        fun removeCart(
            userName: String,
            password: String,
            id: String,
            onSuccess: (user: User, products: ArrayList<Product>) -> Unit,
            onFailed: () -> Unit
        ) {
            val body = HashMap<String, String>()
            body["userName"] = userName
            body["password"] = password
            body["id"] = id

            mAPI.removeCart(body).enqueue(object : Callback<JsonElement> {
                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    onFailed()
                }

                override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                    if (response.isSuccessful) {
                        val res = response.body() ?: return
                        val json = JSONObject(res.toString())
                        val status = json.getInt("status")
                        if (status == RESULT_OK) {
                            val user = Gson().fromJson(json.getString("user"), User::class.java)
                            val productsList = Gson().fromJson(
                                json.getString("products"),
                                Array<Product>::class.java
                            )
                            val productArrayList = ArrayList<Product>()
                            productArrayList.addAll(productsList)
                            onSuccess(user, productArrayList)
                        } else {
                            onFailed()
                        }
                    } else {
                        onFailed()
                    }
                }
            })
        }

        fun addCart(
            userName: String,
            password: String,
            id: String,
            onSuccess: (user: User, products: ArrayList<Product>) -> Unit,
            onFailed: () -> Unit
        ) {
            val body = HashMap<String, String>()
            body["userName"] = userName
            body["password"] = password
            body["id"] = id

            mAPI.addCart(body).enqueue(object : Callback<JsonElement> {
                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    onFailed()
                }

                override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                    if (response.isSuccessful) {
                        val res = response.body() ?: return
                        val json = JSONObject(res.toString())
                        val status = json.getInt("status")
                        if (status == RESULT_OK) {
                            val user = Gson().fromJson(json.getString("user"), User::class.java)
                            val productsList = Gson().fromJson(
                                json.getString("products"),
                                Array<Product>::class.java
                            )
                            val productArrayList = ArrayList<Product>()
                            productArrayList.addAll(productsList)
                            onSuccess(user, productArrayList)
                        } else {
                            onFailed()
                        }
                    } else {
                        onFailed()
                    }
                }
            })
        }

        fun addBill(bill: Bill,onSuccess: (user: User) -> Unit,onFailed: () -> Unit){
            mAPI.addBill(bill).enqueue(object : Callback<JsonElement>{
                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    onFailed()
                }

                override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                    if (response.isSuccessful){
                        val res = response.body()?:return
                        val json = JSONObject(res.toString())
                        if (json.getInt("status")== RESULT_OK){
                            val user = Gson().fromJson(json.getString("user"),User::class.java)
                            onSuccess(user)
                        } else{
                            onFailed()
                        }
                    } else {
                        onFailed()
                    }
                }
            })
        }

        fun getAllBill(userName: String,password: String,onSuccess: (billList:ArrayList<Bill>,userMap:Map<String,User>,moneyMap:Map<String,Int>) -> Unit,onFailed: () -> Unit){
            val body = HashMap<String,String>()
            body["userName"]=userName
            body["password"]=password

            mAPI.getAllBills(body).enqueue(object:Callback<JsonElement>{
                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    onFailed()
                }

                override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                    if (response.isSuccessful){
                        val res = response.body()?:return
                        val json = JSONObject(res.toString())
                        if (json.getInt("status")== RESULT_OK){
                            val billList = Gson().fromJson(json.getString("bills"),Array<Bill>::class.java)
                            val billArrayList = ArrayList<Bill>()
                            billArrayList.addAll(billList)
                            val userMap:Map<String,User> = Gson().fromJson(json.getString("userMap"),object :TypeToken<Map<String,User>>(){}.type)
                            val moneyMap:Map<String,Int> = Gson().fromJson(json.getString("moneyMap"),object :TypeToken<Map<String,Int>>(){}.type)
                            onSuccess(billArrayList,userMap, moneyMap)
                        } else {
                            onFailed()
                        }
                    } else {
                        onFailed()
                    }
                }

            })
        }

        fun checkBill(userName: String, password: String, id: String, onSuccess: (billList: ArrayList<Bill>, userMap: Map<String, User>, moneyMap: Map<String, Int>) -> Unit, onFailed: () -> Unit){
            val body = HashMap<String,String>()
            body["userName"]=userName
            body["password"]=password
            body["id"]=id
            mAPI.checkBill(body).enqueue(object :Callback<JsonElement>{
                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    onFailed()
                }

                override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                    if (response.isSuccessful){
                        val res = response.body()?:return
                        val json = JSONObject(res.toString())
                        if (json.getInt("status")== RESULT_OK){
                            val billList = Gson().fromJson(json.getString("bills"),Array<Bill>::class.java)
                            val billArrayList = ArrayList<Bill>()
                            billArrayList.addAll(billList)
                            val userMap:Map<String,User> = Gson().fromJson(json.getString("userMap"),object :TypeToken<Map<String,User>>(){}.type)
                            val moneyMap:Map<String,Int> = Gson().fromJson(json.getString("moneyMap"),object :TypeToken<Map<String,Int>>(){}.type)
                            onSuccess(billArrayList,userMap, moneyMap)
                        } else {
                            onFailed()
                        }
                    } else {
                        onFailed()
                    }
                }
            })
        }

        fun deleteBill(userName: String, password: String, id: String, onSuccess: (billList: ArrayList<Bill>, userMap: Map<String, User>, moneyMap: Map<String, Int>) -> Unit, onFailed: () -> Unit){
            val body = HashMap<String,String>()
            body["userName"]=userName
            body["password"]=password
            body["id"]=id
            mAPI.deleteBill(body).enqueue(object :Callback<JsonElement>{
                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    onFailed()
                }

                override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                    if (response.isSuccessful){
                        val res = response.body()?:return
                        val json = JSONObject(res.toString())
                        if (json.getInt("status")== RESULT_OK){
                            val billList = Gson().fromJson(json.getString("bills"),Array<Bill>::class.java)
                            val billArrayList = ArrayList<Bill>()
                            billArrayList.addAll(billList)
                            val userMap:Map<String,User> = Gson().fromJson(json.getString("userMap"),object :TypeToken<Map<String,User>>(){}.type)
                            val moneyMap:Map<String,Int> = Gson().fromJson(json.getString("moneyMap"),object :TypeToken<Map<String,Int>>(){}.type)
                            onSuccess(billArrayList,userMap, moneyMap)
                        } else {
                            onFailed()
                        }
                    } else {
                        onFailed()
                    }
                }
            })
        }

    }
}