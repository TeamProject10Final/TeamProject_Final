package com.nomorelateness.donotlate.core.data.session

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.nomorelateness.donotlate.core.domain.session.FirebaseUid
import com.nomorelateness.donotlate.core.domain.session.KakaoUid
import com.nomorelateness.donotlate.core.domain.session.SessionManager
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SessionManagerImpl : SessionManager {
    override suspend fun get(): FirebaseUid {
        return FirebaseAuth.getInstance().uid
    }

    override suspend fun logOut() {
        FirebaseAuth.getInstance().signOut()
    }

    override suspend fun logOutWithKakao() {
        UserApiClient.instance.logout { error ->
            if(error != null){
                Log.d("Kakao LoggedOut", "Kakao LogOut fails")
            }else{
                Log.d("Kakao LoggedOut", "Kakao LogOut Success")
            }
        }

    }

    override suspend fun get1(): OAuthToken? {
        val token = AuthApiClient.instance.tokenManagerProvider.manager.getToken()

        Log.d("kakaoToken", "${token}확인")
        if (token != null) {
            return token
        }else {
            return null
        }
    }
}