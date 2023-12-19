package com.example.mobileproject.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.mobileproject.R
import com.example.mobileproject.models.cartItemModel
import com.example.mobileproject.repository.AuthRepository
import com.example.mobileproject.repository.homeRepository
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.coroutines.delay

class Orders : AppCompatActivity() {
    var count = 0
    lateinit var value: TextView
    lateinit var tableParent:TableLayout
    lateinit var email_nav:TextView
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var total_price:TextView
    lateinit var checkout:Button
    var totalPrice = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val header =navView.getHeaderView(0)
        email_nav=header.findViewById(R.id.email_nav)
        total_price=findViewById(R.id.total_price)
        val authRepo = AuthRepository()
        email_nav.text = authRepo.getEmailFromCache(this)
        tableParent = findViewById(R.id.tableParent)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        checkout=findViewById(R.id.checkout)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, HomePage::class.java)
                    startActivity(intent)
                }


                R.id.nav_cart -> {

                }

                R.id.nav_person -> {
                    val intent = Intent(this, Profile::class.java)
                    startActivity(intent)

                }
            }
            true

        }

        val authRepository=AuthRepository()
        val homeRepo = homeRepository()
        val token = authRepository.getTokenFromCache(this)
        homeRepo.getCart(token!!){flowers->
            if (flowers!=null){
                addCartItem(this,flowers,tableParent)
                total_price.text=totalPrice.toString()
                checkout.setOnClickListener {
                    homeRepo.emptyCart(token){message->
                        val snackbar = Snackbar.make(checkout, message!!.message, Snackbar.LENGTH_LONG)
                        snackbar.show()
                        Handler().postDelayed({
                            val intent = Intent(this, HomePage::class.java)
                            startActivity(intent)
                        }, 2000)
                    }
                }
            }
        }
    }

    fun addCartItem(context:Context,items:List<cartItemModel>,parent:TableLayout) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        for (flower in items) {
            val flowerView = inflater.inflate(R.layout.cartitem, null)

            val imageView = flowerView.findViewById<ImageView>(R.id.cardImage2)
            // Set the image for the flower using Picasso/Glide or any image loading library
            Picasso.get().load(flower.product.image).into(imageView)

            val titleTextView = flowerView.findViewById<TextView>(R.id.cartItemName)
            titleTextView.text = flower.product.name

            val priceTextView = flowerView.findViewById<TextView>(R.id.cartItemPrice)
            priceTextView.text = "${flower.product.price}EGP"

            val quantity = flowerView.findViewById<TextView>(R.id.quantity)
            quantity.text = flower.quantity.toString()
            totalPrice+=(flower.product.price*flower.quantity)
            // Add the inflated view for each flower to the main layout


            parent.addView(flowerView)

        }
    }
}