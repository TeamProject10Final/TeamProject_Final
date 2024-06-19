package com.example.donotlate.core.data.session

import com.example.donotlate.core.domain.session.FirebaseUid
import com.example.donotlate.core.domain.session.SessionManager
import com.google.firebase.auth.FirebaseAuth

class SessionManagerImpl : SessionManager {
    override suspend fun get(): FirebaseUid {
        return FirebaseAuth.getInstance().uid
    }

    override suspend fun logOut() {
        FirebaseAuth.getInstance().signOut()
    }
}