package com.example.donotlate

import android.app.Application

class DoNotLateApplication: Application() {

    val appContainer = AppContainer()

    override fun onCreate() {
        APPINSTANCE = this
        super.onCreate()
    }

    companion object{
        private var APPINSTANCE: DoNotLateApplication?= null
        fun getInstance() = APPINSTANCE
    }
}