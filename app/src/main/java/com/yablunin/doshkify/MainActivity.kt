package com.yablunin.doshkify

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.yablunin.doshkify.data.AuthHandler
import com.yablunin.doshkify.databinding.ActivityMainBinding
import com.yablunin.doshkify.dialogs.DialogHandler

class MainActivity() : AppCompatActivity(), OnNavigationItemSelectedListener{
    private lateinit var binding: ActivityMainBinding
    private lateinit var navHeaderEmail: TextView

    private val dialogHandler = DialogHandler(this)
    val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    override fun onStart() {
        super.onStart()
        updateUi(auth.currentUser)
    }

    private fun init(){
        val toolbar: Toolbar = findViewById(R.id.main_content_toolbar)
        val toggle: ImageView = toolbar.findViewById(R.id.main_content_toggle)
        toggle.setOnClickListener {
            binding.mainDrawerLayout.openDrawer(GravityCompat.START)
        }

        binding.mainNavView.setNavigationItemSelectedListener(this)

        navHeaderEmail = binding.mainNavView.getHeaderView(0).findViewById(R.id.nav_header_email)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.main_menu_my_ads -> {

            }
            R.id.main_menu_cars_ads -> {

            }
            R.id.main_menu_pc_ads -> {

            }
            R.id.main_menu_phones_ads -> {

            }
            R.id.main_menu_electronics_ads -> {

            }
            R.id.main_menu_sign_up -> {
                dialogHandler.createSignDialog(DialogHandler.SIGN_UP_DIALOG)
            }
            R.id.main_menu_log_in -> {
                dialogHandler.createSignDialog(DialogHandler.LOG_IN_DIALOG)
            }
            R.id.main_menu_log_out -> {
                updateUi(null)
                auth.signOut()
            }
        }

        binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun updateUi(user: FirebaseUser?){
        navHeaderEmail.text = if (user == null){
            getString(R.string.main_menu_not_signup)
        }
        else{
            user.email
        }
    }


}