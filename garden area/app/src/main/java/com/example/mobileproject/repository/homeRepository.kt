package com.example.mobileproject.repository

import android.content.Context
import com.example.mobileproject.api.ApiService
import com.example.mobileproject.api.Constants
import com.example.mobileproject.models.cartItemModel
import com.example.mobileproject.models.flowerModel
import com.example.mobileproject.models.loginResponse
import com.example.mobileproject.models.messageModel
import com.example.mobileproject.models.registerResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Objects

class homeRepository {
    fun getFlowers(callback:(List<flowerModel>) ->Unit) {
        val service = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
        service.getFlowers().enqueue(object : Callback<List<flowerModel>> {
            override fun onResponse(call: Call<List<flowerModel>>, response: Response<List<flowerModel>>) {
                if (response.isSuccessful) {
                    val flowers = response.body()
                    callback(flowers!!)
                } else {
                    callback(emptyList())
                }
            }



            override fun onFailure(call: Call<List<flowerModel>>, t: Throwable) {
                callback(emptyList())
            }
        })
    }
    fun getFlowerById(id:Int,callback:(flowerModel?) ->Unit) {
        val service = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
        service.getFlowerById(id).enqueue(object : Callback<flowerModel> {
            override fun onResponse(call: Call<flowerModel>, response: Response<flowerModel>) {
                if (response.isSuccessful) {
                    val flowers = response.body()
                    callback(flowers!!)
                } else {
                    callback(null)
                }
            }



            override fun onFailure(call: Call<flowerModel>, t: Throwable) {
                callback(null)
            }
        })
    }
    fun getCart(token :String,callback: (List<cartItemModel>?) ->Unit){
        val service = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
        service.cart(token).enqueue(object :Callback<List<cartItemModel>>{
            override fun onResponse(
                call: Call<List<cartItemModel>>,
                response: Response<List<cartItemModel>>
            ) {
                if (response.isSuccessful){
                    val cartItems = response.body()!!
                    callback(cartItems)
                }
            }

            override fun onFailure(call: Call<List<cartItemModel>>, t: Throwable) {
                callback(null)
            }

        })
    }
    fun addToCart(token :String,quantity:String,id:String,callback: (loginResponse?) ->Unit){
        val service = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
        service.addToCart(token,quantity,id).enqueue(object :Callback<loginResponse>{
            override fun onResponse(
                call: Call<loginResponse>,
                response: Response<loginResponse>
            ) {
                if (response.isSuccessful){
                    val cartItems = response.body()!!
                    callback(cartItems)
                }
            }

            override fun onFailure(call: Call<loginResponse>, t: Throwable) {
                callback(null)
            }

        })
    }
    fun emptyCart(token :String,callback: (messageModel?) ->Unit){
        val service = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
        service.emptyCart(token).enqueue(object :Callback<messageModel>{
            override fun onResponse(
                call: Call<messageModel>,
                response: Response<messageModel>
            ) {
                if (response.isSuccessful){
                    val cartItems = response.body()!!
                    callback(cartItems)
                }
            }

            override fun onFailure(call: Call<messageModel>, t: Throwable) {
                callback(null)
            }

        })
    }


}