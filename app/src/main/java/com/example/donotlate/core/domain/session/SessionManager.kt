package com.example.donotlate.core.domain.session


typealias FirebaseUid = String?

interface SessionManager {

    suspend fun get(): FirebaseUid

    suspend fun logOut()
}