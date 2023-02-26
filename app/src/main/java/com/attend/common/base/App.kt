package com.attend.common.base

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import com.google.firebase.FirebaseApp

class App : Application() {

    companion object {
        private lateinit var application: Application
        val context: Context
            get() = application.applicationContext
    }


    override fun attachBaseContext(context: Context) {
        super.attachBaseContext(context)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        application = this

        FirebaseApp.initializeApp(this)

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


//        Fresco.initialize(
//            this,
//            ImagePipelineConfig.newBuilder(this)
//                .setMemoryChunkType(MemoryChunkType.BUFFER_MEMORY)
//                .setImageTranscoderType(ImageTranscoderType.JAVA_TRANSCODER)
//                .experiment().setNativeCodeDisabled(true)
//                .build())
    }


}