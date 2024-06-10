package com.example.donotlate

import android.app.Application

class MyApp:Application() {

    val appContainer = AppContainer()

    override fun onCreate() {
        APPINSTANCE = this
        super.onCreate()
    }
    companion object{
        private var APPINSTANCE: MyApp?=null
        fun getInstance() = APPINSTANCE!!
    }
}