package com.attend.common.util

import android.content.Context
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by Omairtech on 06/03/02021.
 */
class Validator {

    companion object {
        @Volatile
        private var instance: Validator? = null
        fun getInstance(): Validator? {
            if (instance == null) {
                instance = Validator()
            }
            return instance
        }
    }

    fun isEmpty(editText: EditText, error: String?): Boolean {
        if (editText.text.toString().trim { it <= ' ' }.isEmpty()) {
            editText.error = error
            editText.requestFocus()
            return true
        }
        return false
    }

    fun isNotEqual(editText: EditText, value: String, error: String?): Boolean {
        if (editText.text.toString().trim { it <= ' ' } != value) {
            editText.error = error
            editText.requestFocus()
            return true
        }
        return false
    }

    var emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z]+\\.+[a-zA-Z]+"
    fun isEmailNotValid(editText: EditText, error: String?): Boolean {
        if (editText.text.toString().trim { it <= ' ' }.isNotEmpty()
            && !editText.text.toString().trim { it <= ' ' }.matches(Regex(emailPattern))
        ) {
            editText.error = error
            editText.requestFocus()
            return true
        }
        return false
    }

    fun isPhoneNotValid(editText: EditText, error: String?): Boolean {
        var phoneNumber = editText.text.toString().trim { it <= ' ' }
        if (phoneNumber.startsWith("+966-") ||
            phoneNumber.startsWith("+966") ||
            phoneNumber.startsWith("00966") ||
            phoneNumber.startsWith("966")
        ) phoneNumber = phoneNumber
            .replace("+966-", "")
            .replace("+966", "")
            .replace("00966", "")
            .replace("966", "")

        val phoneRegex: Array<Regex> = arrayOf(
            Regex("050[0-9]{7}"),
            Regex("051[0-9]{7}"),
            Regex("053[0-9]{7}"),
            Regex("054[0-9]{7}"),
            Regex("055[0-9]{7}"),
            Regex("056[0-9]{7}"),
            Regex("057[0-9]{7}"),
            Regex("058[0-9]{7}"),
            Regex("059[0-9]{7}")
        )
        for (regex in phoneRegex) {
            if (phoneNumber.matches(regex))
                return false
        }
        editText.error = error
        editText.requestFocus()
        return true
    }


    fun notSelected(spinner: Spinner, error: String?, context: Context?): Boolean {
        if (spinner.selectedItemPosition == 0) {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            // Open the Spinner...
            spinner.performClick()
            return true
        }
        return false
    }

    @Throws(JSONException::class)
    fun hasError(editText: EditText, field: String, jsonObject: JSONObject?): Boolean {
        if (jsonObject!!.has(field)) {
            editText.error = jsonObject.getString(field)
            editText.requestFocus()
            return true
        }
        return false
    }

    fun hasError(editText: EditText?, field: String, jsonObject: Map<String, Any>?): Boolean {
        if (editText != null && jsonObject!!.containsKey(field)) {
            editText.error = jsonObject[field].toString() + ""
            editText.requestFocus()
            return true
        }
        return false
    }
}