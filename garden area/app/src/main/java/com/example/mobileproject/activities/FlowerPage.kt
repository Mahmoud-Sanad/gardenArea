package com.example.mobileproject.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.mobileproject.R
import com.example.mobileproject.api.ApiService
import com.example.mobileproject.repository.AuthRepository
import com.example.mobileproject.repository.homeRepository
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso

class FlowerPage : AppCompatActivity() {
    var count = 0
    lateinit var id:String
    lateinit var value: TextView
    lateinit var buttonPlus: Button
    lateinit var buttonMinus: Button
    lateinit var cardImage:ImageView
    lateinit var flowerName:TextView
    lateinit var flowerPrice:TextView
    lateinit var addToCart:Button
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var email_nav:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flower_page)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val header =navView.getHeaderView(0)
        email_nav=header.findViewById(R.id.email_nav)
        val authRepo = AuthRepository()
        email_nav.text = authRepo.getEmailFromCache(this)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, HomePage::class.java)
                    startActivity(intent)
                }



                R.id.nav_cart -> {
                    val intent = Intent(this, Orders::class.java)
                    startActivity(intent)
                }

                R.id.nav_person -> {
                    val intent = Intent(this, Profile::class.java)
                    startActivity(intent)

                }
            }
            true

        }
         id = intent.getStringExtra("id")!!
        Log.d("TAG", "onCreate:$id ")
        flowerName=findViewById(R.id.flowerName)
        cardImage=findViewById(R.id.cardImage)
        flowerPrice=findViewById(R.id.flowerPrice)
        // Initialize the views
        value = findViewById(R.id.value)
        buttonPlus = findViewById(R.id.button_plus)
        buttonMinus = findViewById(R.id.button_minus)

        buttonPlus.setOnClickListener {
            count++
            value.text = "$count"
        }
        buttonMinus.setOnClickListener {
            if (count == 0) {
                count = 0
            } else {
                count--
                value.text = "$count"
            }
        }
        addToCart=findViewById(R.id.addToCart)

        val homeRepo = homeRepository()
        homeRepo.getFlowerById(id.toInt()){flower->
            flowerName.text=flower!!.name
            flowerPrice.text=flower!!.price.toString()
            Picasso.get().load(flower.image).into(cardImage)
        }
        val authRepository = AuthRepository()
        val token = authRepository.getTokenFromCache(this)
        addToCart.setOnClickListener(){
            if (count>0){
                homeRepo.addToCart(token!!,count.toString(),id!!){value->
                    val snackbar = Snackbar.make(addToCart, value!!.message, Snackbar.LENGTH_LONG)
                    snackbar.show()
                }
            }
        }
    }
}
