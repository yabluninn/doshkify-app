package com.yablunin.doshkify.presentative.dialogs

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.View
import com.yablunin.doshkify.presentative.MainActivity
import com.yablunin.doshkify.R
import com.yablunin.doshkify.data.AuthHandler
import com.yablunin.doshkify.databinding.SignDialogBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SignDialog(_activity: MainActivity) {

    private val activity = _activity
    private val authHandler = AuthHandler(activity)

    private lateinit var dialogBinding: SignDialogBinding
    lateinit var alertDialog: AlertDialog

    fun createSignDialog(dialogId: Int){
        val builder = AlertDialog.Builder(activity)
        dialogBinding = SignDialogBinding.inflate(activity.layoutInflater)
        val view = dialogBinding.root
        builder.setView(view)
        alertDialog = builder.create()

        setSignDialogId(dialogId, alertDialog)

        dialogBinding.signDialogButton.setOnClickListener{
            setOnClickSignButton(dialogId, alertDialog)
        }

        dialogBinding.signDialogGoogleSignInButton.setOnClickListener {
            authHandler.signInWithGoogle()
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun setSignDialogId(dialogId: Int, dialog: AlertDialog){
        when(dialogId) {
            DialogHandler.SIGN_UP_DIALOG -> {
                dialogBinding.signDialogTitle.text = activity.resources.getString(R.string.sign_dialog_title)
                dialogBinding.signDialogButton.text = activity.resources.getString(R.string.sign_dialog_button)
                dialogBinding.signDialogForgotPasswordButton.visibility = View.GONE
                dialogBinding.signDialogGoogleSignInButton.text = activity.getString(R.string.sign_dialog_sign_in_with_google)
            }
            DialogHandler.LOG_IN_DIALOG -> {
                dialogBinding.signDialogTitle.text = activity.resources.getString(R.string.log_in_dialog_title)
                dialogBinding.signDialogButton.text = activity.resources.getString(R.string.log_in_dialog_button)
                dialogBinding.signDialogForgotPasswordButton.visibility = View.VISIBLE
                dialogBinding.signDialogForgotPasswordButton.setOnClickListener {
                    setOnClickPassRecoverButton(dialog)
                }
                dialogBinding.signDialogGoogleSignInButton.text = activity.getString(R.string.sign_dialog_log_in_with_google)
            }
        }
    }

    private fun setOnClickSignButton(dialogId: Int, dialog: AlertDialog){
        when(dialogId){
            DialogHandler.SIGN_UP_DIALOG -> {
                val email: String = dialogBinding.signDialogEmailInput.text.toString()
                val password: String = dialogBinding.signDialogPasswordInput.text.toString()
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    authHandler.signUpWithEmail(email, password, this)
                }
                else{
                    showErrorMessage(DialogHandler.EMAIL_INPUT,"Incorrect email")
                    showErrorMessage(DialogHandler.PASSWORD_INPUT, "Incorrect password")
                }
            }
            DialogHandler.LOG_IN_DIALOG -> {
                val email: String = dialogBinding.signDialogEmailInput.text.toString()
                val password: String = dialogBinding.signDialogPasswordInput.text.toString()
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    authHandler.logInWithEmail(email, password, this)
                }
                else{
                    showErrorMessage(DialogHandler.EMAIL_INPUT,"Incorrect email")
                    showErrorMessage(DialogHandler.PASSWORD_INPUT, "Incorrect password")
                }
            }
        }
    }

    private fun setOnClickPassRecoverButton(dialog: AlertDialog){
        val email = dialogBinding.signDialogEmailInput.text.toString()
        if (email.isNotEmpty()){
            activity.auth.sendPasswordResetEmail(email).addOnCompleteListener {
                if (it.isSuccessful){
                    // Show successful message
                }
            }
            dialog.dismiss()
        }
        else{
            showErrorMessage(DialogHandler.EMAIL_INPUT, "Incorrect email")
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun showErrorMessage(input: Int, message: String){
        val mainScope = MainScope()
        when(input){
            DialogHandler.EMAIL_INPUT -> {
                dialogBinding.signDialogEmptyMailError.visibility = View.VISIBLE
                dialogBinding.signDialogEmptyMailError.text = message
                dialogBinding.signDialogEmailInput.background = activity.getDrawable(R.drawable.custom_input_error)
                mainScope.launch {
                    delay(4000)
                    dialogBinding.signDialogEmptyMailError.visibility = View.GONE
                    dialogBinding.signDialogEmailInput.background = activity.getDrawable(R.drawable.custom_input)
                }
            }
            DialogHandler.PASSWORD_INPUT -> {
                dialogBinding.signDialogEmptyPasswordError.visibility = View.VISIBLE
                dialogBinding.signDialogEmptyPasswordError.text = message
                dialogBinding.signDialogPasswordInput.background = activity.getDrawable(R.drawable.custom_input_error)
                mainScope.launch {
                    delay(4000)
                    dialogBinding.signDialogEmptyPasswordError.visibility = View.GONE
                    dialogBinding.signDialogPasswordInput.background = activity.getDrawable(R.drawable.custom_input)
                }
            }
        }
    }
}