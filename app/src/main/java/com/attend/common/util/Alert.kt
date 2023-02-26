package com.attend.common.util

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.widget.Toast

object Alert {

    interface Listener {
        fun onClick(b: Boolean)
    }

    fun alert(
        context: Context,
        title: String? = null,
        message: String? = null,
        approved: ((Boolean) -> Unit)? = null,
        canceled: ((Boolean) -> Unit)? = null,
        approved_title: Int = android.R.string.ok,
        canceled_title: Int = android.R.string.cancel
    ) {
        try {
            val alert = AlertDialog.Builder(context).setCancelable(true)

            if (title != null) alert.setTitle(title)
            if (message != null) alert.setMessage(message)

            alert.setPositiveButton(approved_title) { dialog: DialogInterface?, _: Int ->
                dialog?.dismiss()
                if (approved != null) approved(true)
            }

            if (canceled != null) {
                alert.setNegativeButton(canceled_title) { dialog: DialogInterface, _: Int ->
                    dialog.dismiss()
                    canceled(true)
                }
            }
            alert.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun toast(context: Context, message: String?) {
        Toast.makeText(context, message + "", Toast.LENGTH_LONG).show()
    }

    fun toast(context: Context, messageId: Int) {
        Toast.makeText(context, messageId, Toast.LENGTH_LONG).show()
    }

    fun log(tag: String = "Smartly", message: String) {
        Log.v(tag, message)
    }

    fun log(message: String) {
        log("Smartly", message)
    }
}