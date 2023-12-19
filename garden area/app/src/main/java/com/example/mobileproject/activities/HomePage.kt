package com.example.mobileproject.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.mobileproject.R
import com.example.mobileproject.databinding.NavHeaderBinding
import com.example.mobileproject.models.flowerModel
import com.example.mobileproject.repository.AuthRepository
import com.example.mobileproject.repository.homeRepository
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso

class HomePage : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var email_nav:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val header =navView.getHeaderView(0)
        email_nav=header.findViewById(R.id.email_nav)
        val authRepo = AuthRepository()
        email_nav.text = authRepo.getEmailFromCache(this)
        val grid: GridLayout = findViewById(R.id.grid)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {

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
        val homeRepository = homeRepository()

        homeRepository.getFlowers { flowers->
            addFlowerViewsToLayout(this,grid,flowers)

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    fun addFlowerViewsToLayout(context: Context, parentLayout: GridLayout, flowers: List<flowerModel>) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        for (flower in flowers) {
            val flowerView = inflater.inflate(R.layout.flower_item, null)
            flowerView.setOnClickListener(){
                val intent = Intent(this,FlowerPage::class.java)
                intent.putExtra("id", flower.id.toString())
                Log.d("TAG", "addFlowerViewsToLayout: ${flower.id} ")
                startActivity(intent)
            }

            val imageView = flowerView.findViewById<ImageView>(R.id.image1)
            // Set the image for the flower using Picasso/Glide or any image loading library
            Picasso.get().load(flower.image).into(imageView)

            val titleTextView = flowerView.findViewById<TextView>(R.id.flowerName)
            titleTextView.text = flower.name

            val priceTextView = flowerView.findViewById<TextView>(R.id.flowerPrice)
            priceTextView.text = "${flower.price}EGP"

            // Add the inflated view for each flower to the main layout


            parentLayout.addView(flowerView)
        }
    }

}