package com.hoangpro.serverandroidassignmentapp.helper

import android.content.Context
import com.google.gson.Gson
import com.hoangpro.serverandroidassignmentapp.model.User
import java.lang.Exception

class PreferenceHelper(context: Context) {
    companion object{
        val PREFERENCE_NAME="HoangShopper"
        val USER_DATA="userData"
    }
    private var context=context
    private var preference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun setUser(user: User?){
        if (user==null){
            preference.edit().putString(USER_DATA, "").apply()
        } else {
            val userString = Gson().toJson(user)
            preference.edit().putString(USER_DATA, userString).apply()
        }
    }
    fun getUser():User?{
        val userString = preference.getString(USER_DATA,"")
        if (userString=="") return null
        val user =Gson().fromJson(userString,User::class.java)
        if (user.userName.isNullOrEmpty() || user.password.isNullOrEmpty()){
            return null
        }
        return user
    }
}