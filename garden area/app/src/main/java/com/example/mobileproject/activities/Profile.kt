package com.example.mobileproject.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.mobileproject.R
import com.example.mobileproject.repository.AuthRepository
import com.example.mobileproject.repository.homeRepository
import com.google.android.material.navigation.NavigationView

class Profile : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var firstName:EditText
    lateinit var lastName:EditText
    lateinit var email:EditText
    lateinit var phone:EditText
    lateinit var email_nav: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val header =navView.getHeaderView(0)
        email_nav=header.findViewById(R.id.email_nav)
        val authRepo = AuthRepository()
        email_nav.text = authRepo.getEmailFromCache(this)
        firstName = findViewById(R.id.firstName)
        lastName = findViewById(R.id.lastName)
        email = findViewById(R.id.email)
        phone = findViewById(R.id.phone)

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


                }
            }
            true

        }
        val authRepository = AuthRepository()
        Log.d("TAG", "i am heree")
        val token = authRepository.getTokenFromCache(this)
        Log.d("TAG", token.toString())
        authRepository.getProfile(token!!.toString()){user->
            firstName.hint = user!!.name.split(' ')[0] ?: " "
            lastName.hint = user!!.name.split(' ')[1] ?: " "
            email.hint= user!!.email ?:" "
            phone.hint =user!!.phone ?:"011*********"
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}