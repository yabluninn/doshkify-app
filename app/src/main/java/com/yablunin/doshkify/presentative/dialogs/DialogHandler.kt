package com.yablunin.doshkify.presentative.dialogs

import com.yablunin.doshkify.presentative.MainActivity

class DialogHandler(_activity: MainActivity) {
    companion object{
        const val SIGN_UP_DIALOG = 0
        const val LOG_IN_DIALOG = 1

        const val EMAIL_INPUT = 0
        const val PASSWORD_INPUT = 1
    }

    private val activity = _activity
    private val signDialog = SignDialog(activity)

    fun showSignDialog(dialogId: Int){
        signDialog.createSignDialog(dialogId)
    }
}