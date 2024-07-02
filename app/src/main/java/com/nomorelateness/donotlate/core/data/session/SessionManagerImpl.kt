package com.nomorelateness.donotlate.core.data.session

import com.google.firebase.auth.FirebaseAuth
import com.nomorelateness.donotlate.core.domain.session.FirebaseUid
import com.nomorelateness.donotlate.core.domain.session.SessionManager
import javax.inject.Inject

class SessionManagerImpl
@Inject
constructor(
    private val firebaseAuth: FirebaseAuth
) : SessionManager {
    override suspend fun get(): FirebaseUid {
        return firebaseAuth.uid
    }

    override suspend fun logOut() {
        firebaseAuth.signOut()
    }
}