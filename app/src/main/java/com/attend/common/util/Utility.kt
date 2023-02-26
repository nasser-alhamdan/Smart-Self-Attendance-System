package com.attend.common.util

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.attend.R
import com.attend.common.base.App
import com.attend.data.models.users.Admin
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.exitProcess
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.DurationUnit


object Utility {
    fun isInternetAvailable(): Boolean {
        var result = 0 // Returns connection type. 0: none; 1: mobile data; 2: wifi
        val connectivityManager =
            App.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        connectivityManager?.run {
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.run {
                when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        result = 2
                    }
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        result = 1
                    }
                    hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> {
                        result = 3
                    }
                }
            }
        }
        if (result == 0) {
            Alert.toast(App.context, R.string.please_check_your_connection_and_try_again)
        }
        Alert.log("isInternetAvailable", result.toString())
        return result != 0
    }

    fun setDirection(context: Context, lang: String) {
        context.setAppLocale(lang)
    }

    fun Context.setAppLocale(language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        return createConfigurationContext(config)
    }

    fun convertStringToDateTime(dateTime: String, format: String? = "yyyy-MM-dd hh:mm:ss"): Date {
        return try {
            val mFormat = SimpleDateFormat(format, Locale.ENGLISH)
            mFormat.parse(dateTime) as Date
        } catch (e: ParseException) {
            e.printStackTrace()
            Date()
        }
    }


    fun formatDateTimeToString(
        dateTime: String,
        formatTo: String? = "EEEE, dd MMMM, yyyy - hh:mm a",
        locale: String = "en"
    ): String {
        return try {
            val calendar = Calendar.getInstance()
            calendar.time = convertStringToDateTime(dateTime, "yyyy-MM-dd hh:mm:ss")
            val mFormat = SimpleDateFormat(formatTo, Locale(locale))
            mFormat.format(calendar.time)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            dateTime
        }
    }

    fun convertTimestampToString(
        dateTime: Long,
        formatTo: String? = "EEEE, dd MMMM, yyyy - hh:mm a",
        locale: String = "en"
    ): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = dateTime
        val mFormat = SimpleDateFormat(formatTo, Locale(locale))
        return mFormat.format(calendar.time)
    }

    fun addDaysToDate(
        oldDate: String,
        numberOfDays: Int = 1,
        format: String? = "yyyy-MM-dd"
    ): String {
        val dateFormat = SimpleDateFormat(format, Locale.ENGLISH)
        val calendar = Calendar.getInstance()
        try {
            calendar.time = dateFormat.parse(oldDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        calendar.add(Calendar.DAY_OF_YEAR, numberOfDays)
        val newDate = Date(calendar.timeInMillis)
        return dateFormat.format(newDate)
    }

    fun getDate(year: Int, month: Int, dayOfMonth: Int): String {
        return "$dayOfMonth-${(if ((month + 1) < 10) "0" else "")}${month + 1}-$year"
    }

    fun getDatesBetweenToDays(
        dateFrom: String,
        dateTo: String,
        format: String? = "yyyy-MM-dd"
    ): MutableList<String?> {
        var date = dateFrom
        val dates = mutableListOf<String?>()
        dates.add(date)

        var i = 1;
        while (date != dateTo.trim()) {
            date = addDaysToDate(date, 1, format)
            dates.add(date)
            ++i;
            if (i >= 10) break
        }

        Log.e("TAG", "dates: $dates")
        return dates
    }

    fun getDaysDeferenceBetween(date: String?, format: String? = "yyyy-MM-dd"): Double {
        val calendar = Calendar.getInstance()
        if (date != null) {
            val dateFormat = SimpleDateFormat(format, Locale.ENGLISH)
            calendar.time = dateFormat.parse(date) ?: Date()
        }
        return calendar.daysTillNow()
    }

    fun Calendar.daysTillNow(): Double {
        val diffInMillis = timeInMillis - Date().time
        //return diffInMillis.milliseconds.inWholeDays
        return diffInMillis.milliseconds.toDouble(DurationUnit.DAYS)
    }

    fun askToLoginInAgain(context: Context) {
        // Remove User session
        UserPreference.getInstance(context).saveUser(null as Admin)

        AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.sign_in))
            .setMessage(context.getString(R.string.you_must_log_in_to_your_account_first))
            .setPositiveButton(context.getString(R.string.sign_in)) { _: DialogInterface?, _: Int ->
                startAppAgain(context)
            }
            .setNegativeButton(R.string.cancel) { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
            .show()
    }

    fun startAppAgain(context: Context) {
        try {
            //RESTART APPLICATION FOR DEPENDENT THE NEW LANGUAGE
            val packageManager = context.packageManager
            val intent = packageManager?.getLaunchIntentForPackage(context.packageName)
            if (intent != null) {
                val componentName = intent.component
                val mainIntent = Intent.makeRestartActivityTask(componentName)
                context.startActivity(mainIntent)
                //System.exit(0);
            }
        } catch (e: Exception) {
            e.printStackTrace()
            exitProcess(0) // System.exit(0)
        }
    }

    fun call(context: Activity, phone: String) {
        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context,
                arrayOf(Manifest.permission.CALL_PHONE), 989
            )
        } else {
            // user has already accepted.And make your phone call here.
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phone"))
            context.startActivity(intent)
        }
    }

    fun chat(context: Context, phone: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/$phone"))
        context.startActivity(intent)
    }

    fun getCurrentDate(): String? {
        return try {
            val calendar = Calendar.getInstance()
            val mFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            mFormat.format(calendar.time)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun formatDate(stringDate: String?): String? {
        return try {
            val format = SimpleDateFormat("yyyy-M-dd HH:mm:ss", Locale.ENGLISH)
            val date = format.parse(stringDate)
            val format2 = SimpleDateFormat("EEEE, dd MMMM, yyyy - hh:mm a", Locale.ENGLISH)
            format2.format(date.time)
        } catch (e: ParseException) {
            e.printStackTrace()
            stringDate
        }
    }

    fun showMsg(msg: String) {
        Toast.makeText(App.context.applicationContext, msg, Toast.LENGTH_SHORT).show()
    }
}