package com.yablunin.doshkify.data

import com.google.firebase.auth.FirebaseUser
import com.yablunin.doshkify.presentative.MainActivity

class AuthHandler(_activity: MainActivity) {
    private val activity = _activity
    fun signUpWithEmail (email: String, password: String){
        val auth = activity.auth
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if(it.isSuccessful){
                sendEmailVerification(it.result.user!!)
                activity.updateUi(it.result.user)
            }
            else{

            }
        }
    }

    private fun sendEmailVerification(user: FirebaseUser){
        user.sendEmailVerification().addOnCompleteListener {
            if (it.isSuccessful){

            }
            else{

            }
        }
    }

    fun logInWithEmail (email: String, password: String){
        val auth = activity.auth
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if(it.isSuccessful){
                activity.updateUi(it.result.user)
            }
            else{

            }
        }
    }
}