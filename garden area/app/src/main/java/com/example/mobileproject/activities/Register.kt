package com.example.mobileproject.activities

import android.content.Intent
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.example.mobileproject.R
import com.example.mobileproject.api.ApiService

import com.example.mobileproject.models.registerResponse
import com.example.mobileproject.repository.AuthRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Register : AppCompatActivity() {
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var confirmPasswordInput: EditText
    private lateinit var user_name:EditText
    private lateinit var registerButton: Button
    private lateinit var login:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput)
        registerButton = findViewById(R.id.registerButton)
        user_name = findViewById(R.id.user_name)
        login=findViewById(R.id.login)
        login.setOnClickListener {
            val intent = Intent(this@Register, Login::class.java)
            startActivity(intent)
        }
        registerButton.setOnClickListener {
            Log.d("TAG", "Your debug message");
            Log.d("TAG",emailInput.text.toString())
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()
            val username = user_name.text.toString()
            val auth =  AuthRepository()
            var user: registerResponse
            auth.register(username,email,password){user->
                if (user!!.token!= ""){
                    auth.saveTokenToCache(this ,user!!.token)
                    auth.saveEmailToCache(this,email!!)
                    val intent = Intent(this@Register, HomePage::class.java)
                    startActivity(intent)
                }
                else {
                    val errorMessage = "Register Failed"
                    val snackbar = Snackbar.make(registerButton, errorMessage, Snackbar.LENGTH_LONG)
                    snackbar.show()
                }
            }


        }
    }


}