package com.example.donotlate

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class DoNotLateApplication:Application() {

    val appContainer = AppContainer()

    override fun onCreate() {
        APPINSTANCE = this
        FirebaseApp.initializeApp(this)
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(false) // 캐시를 비활성화하여 최신 데이터 사용
            .build()
        FirebaseFirestore.getInstance().firestoreSettings = settings

        super.onCreate()
    }
    companion object{
        private var APPINSTANCE: DoNotLateApplication?=null
        fun getInstance() = APPINSTANCE!!
    }
}