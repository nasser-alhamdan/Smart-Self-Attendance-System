package com.attend.common.base

import android.content.Context
import com.attend.common.util.ProgressDialog
import com.attend.common.util.UserPreference
import com.attend.common.util.Validator
import com.attend.data.AppRepository

class AppContainer private constructor(context: Context) {

    var up: UserPreference
    var repository: AppRepository
    var validator: Validator? = null


    companion object {
        // For Singleton instantiation
        @Volatile
        private var INSTANCE: AppContainer? = null
        fun getInstance(context: Context): AppContainer {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: AppContainer(context).also { INSTANCE = it }
            }
        }
    }

    init {
        up = UserPreference.getInstance(context)
        repository = AppRepository.getInstance()
        validator = Validator.getInstance()
    }

    var progress: ProgressDialog? = null
    fun setProgress(context: Context) {
        progress = ProgressDialog(context)
    }
}