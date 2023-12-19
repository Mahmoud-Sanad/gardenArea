package com.example.mobileproject.repository

import android.content.Context
import android.util.Log
import com.example.mobileproject.api.ApiService
import com.example.mobileproject.api.Constants
import com.example.mobileproject.models.loginResponse
import com.example.mobileproject.models.profileModel
import com.example.mobileproject.models.registerResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthRepository {



    fun loginData(email: String, password: String,callback: (loginResponse?) -> Unit) {
        var user =loginResponse ("","")


        val service = Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(
            GsonConverterFactory.create()).build().create(ApiService::class.java)
        val call = service.login(email, password).enqueue(object : Callback<loginResponse> {
            override fun onResponse(call: Call<loginResponse>, response: Response<loginResponse>) {
                if (response.isSuccessful){
                    user.token = response.body()!!.token
                    user.message=response.body()!!.message
                    callback(user)
                }
            }

            override fun onFailure(call: Call<loginResponse>, t: Throwable) {

            }

        })
        return callback(user)
    }
    fun register(username: String, email: String, password: String, callback: (registerResponse?) -> Unit) {
        val service = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        service.register(username, email, password).enqueue(object : Callback<registerResponse> {
            override fun onResponse(call: Call<registerResponse>, response: Response<registerResponse>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    callback(user)
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<registerResponse>, t: Throwable) {
                callback(null)
            }
        })
    }
    fun getProfile(token:String,callback: (profileModel?) -> Unit){
        val service = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
        Log.d("TAG", "getProfile: $token")
        service.profile(token).enqueue(object :Callback<profileModel> {

            override fun onResponse(call: Call<profileModel>, response: Response<profileModel>) {
                if (response.isSuccessful) {
                    val user = response.body()!!
                    callback(user)

                } else {
                    Log.d("TAG", "not correct")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<profileModel>, t: Throwable) {
                Log.d("TAG","fail")
                callback(null)

            }
        })
    }
    fun saveTokenToCache(context: Context, token: String) {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("token", token)
        editor.apply()
    }

    // Function to retrieve token from SharedPreferences
    fun getTokenFromCache(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("token", null)
    }
    fun saveEmailToCache(context: Context, email: String) {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.apply()
    }

    // Function to retrieve token from SharedPreferences
    fun getEmailFromCache(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("email", null)
    }



}