package com.yablunin.doshkify.presentative

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.yablunin.doshkify.R
import com.yablunin.doshkify.data.AuthHandler
import com.yablunin.doshkify.databinding.ActivityMainBinding
import com.yablunin.doshkify.presentative.dialogs.DialogHandler
import com.yablunin.doshkify.presentative.dialogs.SignDialog

class MainActivity() : AppCompatActivity(), OnNavigationItemSelectedListener{
    private lateinit var binding: ActivityMainBinding
    private lateinit var navHeaderEmail: TextView

    private val dialogHandler = DialogHandler(this)
    private val authHandler = AuthHandler(this)
    val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AuthHandler.GOOGLE_SIGN_IN_REQUEST_CODE){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null){
                    authHandler.signInFirebaseWithGoogle(account.idToken!!)
                }
            }catch (e: ApiException){
                Log.d("MyLog", "Api error: ${e.message}")
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
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
                dialogHandler.showSignDialog(DialogHandler.SIGN_UP_DIALOG)
            }
            R.id.main_menu_log_in -> {
                dialogHandler.showSignDialog(DialogHandler.LOG_IN_DIALOG)
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