package com.yablunin.doshkify.presentative.toasts

import android.widget.TextView
import android.widget.Toast
import com.yablunin.doshkify.R
import com.yablunin.doshkify.presentative.MainActivity

class SuccessfulToast(activity: MainActivity, message: String, duration: Int) {

    private val _activity = activity
    private val _duration = duration
    private val _message = message
    fun show(){
        val layout = _activity.layoutInflater.inflate(R.layout.success_toast_layout, null)
        val toast = Toast(_activity)
        toast.duration = _duration
        toast.view = layout

        val toastText: TextView = layout.findViewById(R.id.success_toast_text)
        toastText.text = _message

        toast.show()
    }
}