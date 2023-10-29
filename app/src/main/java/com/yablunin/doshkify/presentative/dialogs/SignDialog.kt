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

    fun createSignDialog(dialogId: Int){
        val builder = AlertDialog.Builder(activity)
        val dialogBinding = SignDialogBinding.inflate(activity.layoutInflater)
        val view = dialogBinding.root
        builder.setView(view)
        val dialog = builder.create()

        setSignDialogId(dialogId, dialogBinding)

        dialogBinding.signDialogButton.setOnClickListener{
            setOnClickSignButton(dialogId, dialogBinding, dialog)
        }

        dialogBinding.signDialogForgotPasswordButton.setOnClickListener {
            setOnClickPassRecoverButton(dialogBinding, dialog)
        }

        dialog.show()
    }

    private fun setSignDialogId(dialogId: Int, dialogBinding: SignDialogBinding){
        when(dialogId) {
            DialogHandler.SIGN_UP_DIALOG -> {
                dialogBinding.signDialogTitle.text = activity.resources.getString(R.string.sign_dialog_title)
                dialogBinding.signDialogButton.text = activity.resources.getString(R.string.sign_dialog_button)
                dialogBinding.signDialogForgotPasswordButton.visibility = View.GONE
            }
            DialogHandler.LOG_IN_DIALOG -> {
                dialogBinding.signDialogTitle.text = activity.resources.getString(R.string.log_in_dialog_title)
                dialogBinding.signDialogButton.text = activity.resources.getString(R.string.log_in_dialog_button)
                dialogBinding.signDialogForgotPasswordButton.visibility = View.VISIBLE
            }
        }
    }

    private fun setOnClickSignButton(dialogId: Int, dialogBinding: SignDialogBinding, dialog: AlertDialog){
        when(dialogId){
            DialogHandler.SIGN_UP_DIALOG -> {
                val email: String = dialogBinding.signDialogEmailInput.text.toString()
                val password: String = dialogBinding.signDialogPasswordInput.text.toString()
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    authHandler.signUpWithEmail(email, password)
                    dialog.dismiss()
                }
                else{
                    showErrorMessage(DialogHandler.EMAIL_INPUT, dialogBinding)
                    showErrorMessage(DialogHandler.PASSWORD_INPUT, dialogBinding)
                }
            }
            DialogHandler.LOG_IN_DIALOG -> {
                val email: String = dialogBinding.signDialogEmailInput.text.toString()
                val password: String = dialogBinding.signDialogPasswordInput.text.toString()
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    authHandler.logInWithEmail(email, password)
                    dialog.dismiss()
                }
                else{
                    showErrorMessage(DialogHandler.EMAIL_INPUT, dialogBinding)
                    showErrorMessage(DialogHandler.PASSWORD_INPUT, dialogBinding)
                }
            }
        }
    }

    private fun setOnClickPassRecoverButton(dialogBinding: SignDialogBinding, dialog: AlertDialog){
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
            showErrorMessage(DialogHandler.EMAIL_INPUT, dialogBinding)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun showErrorMessage(input: Int, dialogBinding: SignDialogBinding){
        val mainScope = MainScope()
        when(input){
            DialogHandler.EMAIL_INPUT -> {
                dialogBinding.signDialogEmptyMailError.visibility = View.VISIBLE
                dialogBinding.signDialogEmailInput.background = activity.getDrawable(R.drawable.custom_input_error)
                mainScope.launch {
                    delay(4000)
                    dialogBinding.signDialogEmptyMailError.visibility = View.GONE
                    dialogBinding.signDialogEmailInput.background = activity.getDrawable(R.drawable.custom_input)
                }
            }
            DialogHandler.PASSWORD_INPUT -> {
                dialogBinding.signDialogEmptyPasswordError.visibility = View.VISIBLE
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