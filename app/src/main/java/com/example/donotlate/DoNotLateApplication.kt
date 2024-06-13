package com.example.donotlate

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class DoNotLateApplication:Application() {

    val appContainer = AppContainer()

    override fun onCreate() {
        APPINSTANCE = this

        super.onCreate()
    }
    companion object{
        private var APPINSTANCE: DoNotLateApplication?=null
        fun getInstance() = APPINSTANCE!!
    }
}
