package com.yablunin.doshkify.presentative

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.yablunin.doshkify.R
import com.yablunin.doshkify.data.AuthHandler
import com.yablunin.doshkify.databinding.ActivityMainBinding
import com.yablunin.doshkify.presentative.activities.EditAdsActivity
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

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun init(){
        val toolbar: Toolbar = findViewById(R.id.main_content_toolbar)
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFFFF"))
        val toggle = ActionBarDrawerToggle(this, binding.mainDrawerLayout, toolbar, R.string.open, R.string.close)
        toggle.isDrawerIndicatorEnabled = false
        toolbar.navigationIcon = getDrawable(R.drawable.ic_menu)
        toggle.setToolbarNavigationClickListener {
            binding.mainDrawerLayout.openDrawer(GravityCompat.START)
        }
        toggle.syncState()

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
                authHandler.signOutFromGoogle()
            }
        }

        binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.top_main_menu_new_ad){
            val intent = Intent(this, EditAdsActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
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