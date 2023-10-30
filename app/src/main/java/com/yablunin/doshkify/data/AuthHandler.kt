package com.yablunin.doshkify.data

import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.yablunin.doshkify.R
import com.yablunin.doshkify.presentative.MainActivity
import com.yablunin.doshkify.presentative.dialogs.DialogHandler
import com.yablunin.doshkify.presentative.dialogs.SignDialog

class AuthHandler(_activity: MainActivity) {
    private val activity = _activity
    private lateinit var signInClient: GoogleSignInClient
    companion object{
        const val GOOGLE_SIGN_IN_REQUEST_CODE = 123
    }

    fun signUpWithEmail (email: String, password: String, dialog: SignDialog){
        val auth = activity.auth
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if(it.isSuccessful){
                sendEmailVerification(it.result.user!!)
                activity.updateUi(it.result.user)
                dialog.alertDialog.dismiss()
            }
            else{
                if (it.exception is FirebaseAuthUserCollisionException){
                    val exception = it.exception as FirebaseAuthUserCollisionException
                    if (exception.errorCode == "ERROR_EMAIL_ALREADY_IN_USE"){
                        dialog.showErrorMessage(DialogHandler.EMAIL_INPUT, activity.getString(R.string.error_email_already_in_use))
                    }
                }
                else if(it.exception is FirebaseAuthInvalidCredentialsException){
                    val exception = it.exception as FirebaseAuthInvalidCredentialsException
                    if (exception.errorCode == "ERROR_INVALID_EMAIL"){
                        dialog.showErrorMessage(DialogHandler.EMAIL_INPUT, activity.getString(R.string.error_invalid_email))
                    }
                }
                else if(it.exception is FirebaseAuthWeakPasswordException){
                    val exception = it.exception as FirebaseAuthWeakPasswordException
                    if (exception.errorCode == "ERROR_WEAK_PASSWORD"){
                        dialog.showErrorMessage(DialogHandler.PASSWORD_INPUT, activity.getString(R.string.error_weak_password))
                    }
                }
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

    fun logInWithEmail (email: String, password: String, dialog: SignDialog){
        val auth = activity.auth
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if(it.isSuccessful){
                activity.updateUi(it.result.user)
                dialog.alertDialog.dismiss()
            }
            else{
                if(it.exception is FirebaseAuthInvalidCredentialsException){
                    val exception = it.exception as FirebaseAuthInvalidCredentialsException
                    if (exception.errorCode == "ERROR_INVALID_EMAIL"){
                        dialog.showErrorMessage(DialogHandler.PASSWORD_INPUT, activity.getString(R.string.error_invalid_email))
                    }
                    else if (exception.errorCode == "ERROR_WRONG_PASSWORD"){
                        dialog.showErrorMessage(DialogHandler.PASSWORD_INPUT, activity.getString(R.string.error_wrong_password))
                    }
                }
            }
        }
    }

    fun signInWithGoogle(){
        signInClient = getSignInClient()
        val intent = signInClient.signInIntent
        activity.startActivityForResult(intent, GOOGLE_SIGN_IN_REQUEST_CODE)
    }

    fun signInFirebaseWithGoogle(token: String){
        val credential = GoogleAuthProvider.getCredential(token, null)
        activity.auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful){
                Log.d("MyLog", "SignInWithGoogle is successful")
                activity.updateUi(it.result.user)
            }
            else{
                Log.d("MyLog", "Google sign in exception: ${it.exception}")
            }
        }
    }

    private fun getSignInClient(): GoogleSignInClient{
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("508834376573-0pq42sk28j6kubvb9g3j5j29r2ptfdcm.apps.googleusercontent.com")
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(activity, options)
    }
}