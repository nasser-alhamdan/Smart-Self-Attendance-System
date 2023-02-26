package com.attend.common.base

import android.util.Log
import com.attend.common.util.Utility
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson

open class BaseRepository {
    val db: FirebaseFirestore by lazy { Firebase.firestore }

    // Utils  ======================================================================================
    private fun isInternetAvailable(viewProgress: Boolean = true): Boolean {
        if (Utility.isInternetAvailable()) {
            if (viewProgress)
                AppContainer.getInstance(App.context).progress?.show()

            return true
        }
        return false
    }

    fun hideProgress(error: Exception? = null) {
        AppContainer.getInstance(App.context).progress?.hide()
        error?.let {
            it.printStackTrace()
            Log.d("Firebase", "failure", error)
        }
    }

    private fun <T> convertToModel(data: DocumentSnapshot, model: Class<T>): T? {
        data.data?.let {
            it["id"] = data.id
            val gson = Gson()
            return gson.fromJson(gson.toJson(it), model)
        }
        return null
    }


    // DB  ======================================================================================
    fun <T> dbRead(ref: CollectionReference, model: Class<T>, response: (ArrayList<T>) -> Unit) {
        if (isInternetAvailable(false)) ref.get()
            .addOnSuccessListener { snapshot ->
                Log.d("Firebase", "data fetched")
                hideProgress()
                val list = ArrayList<T>()
                snapshot.documents.forEach { documentSnapshot ->
                    convertToModel(documentSnapshot, model)?.let { list.add(it) }
                }
                response(list)
            }
            .addOnFailureListener {
                hideProgress(it)
            }
    }

    fun <T> dbRead(reference: Query, model: Class<T>, response: (ArrayList<T>) -> Unit) {
        if (isInternetAvailable(false)) reference.get()
            .addOnSuccessListener { snapshot ->
                Log.d("Firebase", "data fetched")
                hideProgress()
                val list = ArrayList<T>()
                snapshot.documents.forEach { documentSnapshot ->
                    convertToModel(documentSnapshot, model)?.let { list.add(it) }
                }
                response(list)
            }
            .addOnFailureListener {
                hideProgress(it)
            }
    }

    fun <T> dbShow(reference: DocumentReference, model: Class<T>, response: (T?) -> Unit) {
        if (isInternetAvailable(false)) reference.get()
            .addOnSuccessListener { snapshot ->
                Log.d("Firebase", "data fetched")
                hideProgress()
                if (snapshot != null)
                    convertToModel(snapshot, model)?.let {
                        response(it)
                    }
                else response(null)
            }
            .addOnFailureListener {
                hideProgress(it)
            }
    }

    fun <T> dbShow(reference: Query, model: Class<T>, response: (T?) -> Unit) {
        if (isInternetAvailable(false)) reference.get()
            .addOnSuccessListener { snapshot ->
                Log.d("Firebase", "data fetched")
                hideProgress()
                if (snapshot.documents.isNotEmpty())
                    convertToModel(snapshot.documents.first(), model)?.let {
                        response(it)
                    }
                else response(null)
            }
            .addOnFailureListener {
                hideProgress(it)
            }
        else Utility.showMsg("No internet connection, please check your connection and try again")
    }

    fun dbInsert(reference: CollectionReference, data: Any, response: (String) -> Unit) {
        if (isInternetAvailable()) reference.add(data)
            .addOnSuccessListener {
                Log.d("Firebase", "data added ${it.id}")
                hideProgress()
                response(it.id)
            }
            .addOnFailureListener {
                hideProgress(it)
            }
    }

    fun dbUpdate(reference: DocumentReference, data: Any, response: (Boolean) -> Unit) {
        if (isInternetAvailable()) reference.set(data)
            .addOnSuccessListener {
                Log.d("Firebase", "data updated")
                hideProgress()
                response(true)
            }
            .addOnFailureListener {
                hideProgress(it)
                response(false)
            }
    }

    fun dbDelete(reference: DocumentReference, response: (Boolean) -> Unit) {
        if (isInternetAvailable()) reference.delete()
            .addOnSuccessListener {
                Log.d("Firebase", "data deleted")
                hideProgress()
                response(true)
            }
            .addOnFailureListener {
                hideProgress(it)
                response(false)
            }
    }
}