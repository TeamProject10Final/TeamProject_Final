package com.nomorelateness.donotlate.core.domain.session

import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient


typealias FirebaseUid = String?
typealias KakaoUid = String?

interface SessionManager {

    suspend fun get(): FirebaseUid

    suspend fun logOut()

    suspend fun logOutWithKakao()

    suspend fun get1(): OAuthToken?

}