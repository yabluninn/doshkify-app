package com.yablunin.doshkify.data

import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.yablunin.doshkify.R
import com.yablunin.doshkify.presentative.MainActivity
import com.yablunin.doshkify.presentative.dialogs.DialogHandler
import com.yablunin.doshkify.presentative.dialogs.SignDialog
import com.yablunin.doshkify.presentative.toasts.ErrorToast
import com.yablunin.doshkify.presentative.toasts.SuccessfulToast

class AuthHandler(activity: MainActivity) {
    private val _activity = activity
    private lateinit var signInClient: GoogleSignInClient
    companion object{
        const val GOOGLE_SIGN_IN_REQUEST_CODE = 123
        const val GOOGLE_DEFAULT_WEB_CLIENT_ID = "508834376573-0pq42sk28j6kubvb9g3j5j29r2ptfdcm.apps.googleusercontent.com"
    }

    fun signUpWithEmail (email: String, password: String, dialog: SignDialog){
        val auth = _activity.auth
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if(it.isSuccessful){
                sendEmailVerification(it.result.user!!)
                _activity.updateUi(it.result.user)
                dialog.alertDialog.dismiss()
            }
            else{
                if (it.exception is FirebaseAuthUserCollisionException){
                    val exception = it.exception as FirebaseAuthUserCollisionException
                    if (exception.errorCode == "ERROR_EMAIL_ALREADY_IN_USE"){
                        linkEmailToGoogle(email, password, dialog)
                    }
                }
                else if(it.exception is FirebaseAuthInvalidCredentialsException){
                    val exception = it.exception as FirebaseAuthInvalidCredentialsException
                    if (exception.errorCode == "ERROR_INVALID_EMAIL"){
                        dialog.showErrorMessage(
                            DialogHandler.EMAIL_INPUT,
                            _activity.getString(R.string.error_invalid_email)
                        )
                    }
                }
                else if(it.exception is FirebaseAuthWeakPasswordException){
                    val exception = it.exception as FirebaseAuthWeakPasswordException
                    if (exception.errorCode == "ERROR_WEAK_PASSWORD"){
                        dialog.showErrorMessage(
                            DialogHandler.PASSWORD_INPUT,
                            _activity.getString(R.string.error_weak_password)
                        )
                    }
                }
            }
        }
    }

    private fun sendEmailVerification(user: FirebaseUser){
        user.sendEmailVerification().addOnCompleteListener {
            if (it.isSuccessful){
                val successfulToast = SuccessfulToast(
                    _activity,
                    _activity.getString(R.string.success_email_verification_send),
                    Toast.LENGTH_LONG
                )
                successfulToast.show()
            }
        }
    }

    fun logInWithEmail (email: String, password: String, dialog: SignDialog){
        val auth = _activity.auth
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if(it.isSuccessful){
                _activity.updateUi(it.result.user)
                dialog.alertDialog.dismiss()
                val successfulToast = SuccessfulToast(
                    _activity,
                    _activity.getString(R.string.success_log_in),
                    Toast.LENGTH_LONG
                )
                successfulToast.show()
            }
            else{
                if(it.exception is FirebaseAuthInvalidCredentialsException){
                    val exception = it.exception as FirebaseAuthInvalidCredentialsException
                    if (exception.errorCode == "ERROR_INVALID_EMAIL"){
                        dialog.showErrorMessage(
                            DialogHandler.PASSWORD_INPUT,
                            _activity.getString(R.string.error_invalid_email)
                        )
                    }
                    else if (exception.errorCode == "ERROR_WRONG_PASSWORD"){
                        dialog.showErrorMessage(
                            DialogHandler.PASSWORD_INPUT,
                            _activity.getString(R.string.error_wrong_password)
                        )
                    }
                }
                else if (it.exception is FirebaseAuthInvalidUserException){
                    val exception = it.exception as FirebaseAuthInvalidUserException
                    if (exception.errorCode == "ERROR_USER_NOT_FOUND"){
                        val errorToast = ErrorToast(
                            _activity,
                            _activity.getString(R.string.error_user_not_found),
                            Toast.LENGTH_LONG
                        )
                        errorToast.show()
                    }
                }
            }
        }
    }

    fun signInWithGoogle(){
        signInClient = getSignInClient()
        val intent = signInClient.signInIntent
        _activity.startActivityForResult(intent, GOOGLE_SIGN_IN_REQUEST_CODE)
    }

    fun signInFirebaseWithGoogle(token: String){
        val credential = GoogleAuthProvider.getCredential(token, null)
        _activity.auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful){
                Log.d("MyLog", "SignInWithGoogle is successful")
                _activity.updateUi(it.result.user)
                val successfulToast = SuccessfulToast(
                    _activity,
                    _activity.getString(R.string.success_log_in),
                    Toast.LENGTH_LONG
                )
                successfulToast.show()
            }
            else{
                Log.d("MyLog", "Google sign in exception: ${it.exception}")
            }
        }
    }

    private fun getSignInClient(): GoogleSignInClient{
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(GOOGLE_DEFAULT_WEB_CLIENT_ID)
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(_activity, options)
    }

    fun signOutFromGoogle(){
        getSignInClient().signOut()
    }

    private fun linkEmailToGoogle(email: String, password: String, dialog: SignDialog){
        val credential = EmailAuthProvider.getCredential(email, password)
        if (_activity.auth.currentUser != null){
            _activity.auth.currentUser?.linkWithCredential(credential)?.addOnCompleteListener {
                if (it.isSuccessful){
                    dialog.alertDialog.dismiss()
                    val successfulToast = SuccessfulToast(
                        _activity,
                        _activity.getString(R.string.success_linked_email),
                        Toast.LENGTH_LONG
                    )
                    successfulToast.show()
                }
            }
        }
        else{
            val errorToast = ErrorToast(
                _activity,
                _activity.getString(R.string.error_linked_mail),
                Toast.LENGTH_LONG
            )
            errorToast.show()
        }
    }
}