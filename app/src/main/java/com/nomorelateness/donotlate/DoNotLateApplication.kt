package com.nomorelateness.donotlate

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.kakao.sdk.common.KakaoSdk

class DoNotLateApplication:Application() {

    val appContainer = com.nomorelateness.donotlate.AppContainer()

    override fun onCreate() {
        com.nomorelateness.donotlate.DoNotLateApplication.Companion.APPINSTANCE = this
        FirebaseApp.initializeApp(this)
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(false)
            .build()
        FirebaseFirestore.getInstance().firestoreSettings = settings

        KakaoSdk.init(this, "8287ebd1cfaacf911923f0f5d484fc38")

        super.onCreate()
    }
    companion object{
        private var APPINSTANCE: com.nomorelateness.donotlate.DoNotLateApplication? = null
        fun getInstance() =
            com.nomorelateness.donotlate.DoNotLateApplication.Companion.APPINSTANCE!!
    }
}
