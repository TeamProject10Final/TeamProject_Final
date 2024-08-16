package com.nomorelateness.donotlate.core.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kakao.sdk.user.UserApiClient
import com.nomorelateness.donotlate.core.domain.repository.UserRepository
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UserRepositoryImpl(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) : UserRepository {
    override suspend fun deleteUserData() {
        // batch - transaction 일괄 쓰기, 처리

        val batch = db.batch()
        val userId = auth.currentUser?.uid
        val kakaoUid = getKakaoUid()

        val userIdentifier = when {
            userId != null -> userId
            kakaoUid != null -> kakaoUid
            else -> null
        }

        if (userIdentifier != null) {
            // 모든 유저들 친구 정보 삭제
            val friendsQuerySnapshot = db.collection("users")
                .whereArrayContains("friends", userIdentifier)
                .get()
                .await()

            friendsQuerySnapshot.documents.forEach { document ->
                val friendsList = document.get("friends") as MutableList<String>
                friendsList.remove(userId)
                batch.update(document.reference, "friends", friendsList)
            }
            // 방 탈출 (? 방에서 나가기 처리)
            val roomsQuerySnapshot = db.collection("PromiseRooms")
                .whereArrayContains("participants", userIdentifier)
                .get()
                .await()

            roomsQuerySnapshot.documents.forEach { document ->

                val participantsList = document.get("participants") as MutableList<String>
                participantsList.remove(userId)
                val arrivalStatusMap = document.get("hasArrived") as MutableMap<String, Boolean>
                arrivalStatusMap.remove(userId)
                val departureStatusMap = document.get("hasDeparture") as MutableMap<String, Boolean>
                departureStatusMap.remove(userId)
                val participantsNameList =
                    document.get("participantsNames") as MutableMap<String, String>
                participantsNameList.remove(userId)

                batch.update(document.reference, "participants", participantsList)
                batch.update(document.reference, "hasArrived", arrivalStatusMap)
                batch.update(document.reference, "hasDeparture", departureStatusMap)
                batch.update(document.reference, "participantsNames", participantsNameList)

            }

            batch.delete(db.collection("users").document(userIdentifier))

            UserApiClient.instance.unlink { error ->
                if (error != null) {
                    Log.e("KakaoUnlink", "Unlink failed", error)
                } else {
                    Log.i("KakaoUnlink", "Unlink successful")
                }
            }

            batch.commit().await()
        }


    }

    suspend fun getKakaoUid(): String? {
        return suspendCoroutine { continuation ->
            UserApiClient.instance.me { user, error ->
                if (error != null) {
                    continuation.resume(null)
                } else if (user != null) {
                    continuation.resume(user.id.toString())
                } else {
                    continuation.resume(null)
                }
            }
        }
    }

    override suspend fun deleteUserAccount() {
        val user = auth.currentUser
        user?.delete()?.await()
    }
}