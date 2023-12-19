package com.example.mobileproject.api
import com.example.mobileproject.models.cartItemModel
import com.example.mobileproject.models.flowerModel
import com.example.mobileproject.models.loginResponse
import com.example.mobileproject.models.messageModel
import com.example.mobileproject.models.profileModel
import com.example.mobileproject.models.registerResponse

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.FormUrlEncoded
import retrofit2.Call
import retrofit2.http.Field
interface ApiService {
    @GET("flowers/{id}")
     fun getFlowerById(@Path("id") id:Int): Call<flowerModel>
    @GET("flowers")
     fun getFlowers(): Call<List<flowerModel>>
    @FormUrlEncoded
    @POST("users/register")
    fun register(
        @Field("username") username:String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<registerResponse>
    @FormUrlEncoded
    @POST("users/login")
    fun login(
    @Field("email") email:String,
    @Field("password") password: String,
    ):Call<loginResponse>
    @FormUrlEncoded
    @POST("users/me")
    fun profile(
        @Field("token") token:String
    ):Call<profileModel>
    @FormUrlEncoded
    @POST("users/cart")
    fun cart(
        @Field("token") token:String
    ):Call<List<cartItemModel>>
    @FormUrlEncoded
    @POST("users/addToCart")
    fun addToCart(
        @Field("token") token: String,
        @Field("quantity") quantity:String,
        @Field("id") id:String
    ):Call<loginResponse>
    @FormUrlEncoded
    @POST("users/emptyCart")
    fun emptyCart(
        @Field("token") token:String
    ):Call<messageModel>
}