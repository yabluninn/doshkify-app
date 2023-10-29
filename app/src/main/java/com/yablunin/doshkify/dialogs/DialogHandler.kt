package com.yablunin.doshkify.dialogs

import android.app.AlertDialog
import android.view.View
import com.yablunin.doshkify.MainActivity
import com.yablunin.doshkify.R
import com.yablunin.doshkify.data.AuthHandler
import com.yablunin.doshkify.databinding.SignDialogBinding

class DialogHandler(_activity: MainActivity) {
    companion object{
        const val SIGN_UP_DIALOG = 0;
        const val LOG_IN_DIALOG = 1;
    }

    private val activity = _activity
    private val authHandler = AuthHandler(activity)

    fun createSignDialog(dialogId: Int){
        val builder = AlertDialog.Builder(activity)
        val bindingDialog = SignDialogBinding.inflate(activity.layoutInflater)
        val view = bindingDialog.root
        builder.setView(view)
        val dialog = builder.create()

        when(dialogId) {
            SIGN_UP_DIALOG -> {
                bindingDialog.signDialogTitle.text = activity.resources.getString(R.string.sign_dialog_title)
                bindingDialog.signDialogButton.text = activity.resources.getString(R.string.sign_dialog_button)
                bindingDialog.signDialogButton.setOnClickListener {
                    val email: String = bindingDialog.signDialogEmailInput.text.toString()
                    val password: String = bindingDialog.signDialogPasswordInput.text.toString()
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        authHandler.signUpWithEmail(email, password)
                        dialog.cancel()
                    }
                }
                bindingDialog.signDialogForgotPasswordButton.visibility = View.GONE
            }
            LOG_IN_DIALOG -> {
                bindingDialog.signDialogTitle.text = activity.resources.getString(R.string.log_in_dialog_title)
                bindingDialog.signDialogButton.text = activity.resources.getString(R.string.log_in_dialog_button)
                bindingDialog.signDialogButton.setOnClickListener {
                    val email: String = bindingDialog.signDialogEmailInput.text.toString()
                    val password: String = bindingDialog.signDialogPasswordInput.text.toString()
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        authHandler.logInWithEmail(email, password)
                        dialog.dismiss()
                    }
                }
                bindingDialog.signDialogForgotPasswordButton.visibility = View.VISIBLE
            }
        }
        dialog.show()
    }
}