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
import com.example.mobileproject.models.loginResponse

import com.example.mobileproject.models.registerResponse
import com.example.mobileproject.repository.AuthRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Login : AppCompatActivity() {
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
     private lateinit var signInButton: Button
     private lateinit var register:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)

        signInButton = findViewById(R.id.signInButton)
        register=findViewById(R.id.register)
        register.setOnClickListener {
            val intent = Intent(this@Login,Register::class.java)
            startActivity(intent)
        }
        signInButton.setOnClickListener {
            Log.d("TAG", "Your debug message");
            Log.d("TAG",emailInput.text.toString())
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            val auth =  AuthRepository()
            var user: loginResponse
          auth.loginData(email,password){user->
              if (user?.token!=""){
                  auth.saveTokenToCache(this ,user!!.token)
                  auth.saveEmailToCache(this,email)
                  Log.d("TAG", "onCreate:${email} ")
                  val intent = Intent(this@Login, HomePage::class.java)
                  startActivity(intent)
              }
              else {
                  val errorMessage = "Login Failed"
                  val snackbar = Snackbar.make(signInButton, errorMessage, Snackbar.LENGTH_LONG)
                  snackbar.show()
              }
          }

        }
    }


}