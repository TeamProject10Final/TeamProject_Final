package com.nomorelateness.donotlate.core.data.session

import com.google.firebase.auth.FirebaseAuth
import com.nomorelateness.donotlate.core.domain.session.FirebaseUid
import com.nomorelateness.donotlate.core.domain.session.SessionManager

class SessionManagerImpl : SessionManager {
    override suspend fun get(): FirebaseUid {
        return FirebaseAuth.getInstance().uid
    }

    override suspend fun logOut() {
        FirebaseAuth.getInstance().signOut()
    }
}