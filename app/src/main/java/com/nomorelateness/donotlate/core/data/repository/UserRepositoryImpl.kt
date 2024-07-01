package com.nomorelateness.donotlate.core.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nomorelateness.donotlate.core.domain.repository.UserRepository
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) : UserRepository {
    override suspend fun deleteUserData() {
        // batch - transaction 일괄 쓰기, 처리

        val batch = db.batch()
        val userId = auth.currentUser?.uid

        if (userId != null) {
            // 모든 유저들 친구 정보 삭제
            val friendsQuerySnapshot = db.collection("users")
                .whereArrayContains("friends", userId)
                .get()
                .await()

            friendsQuerySnapshot.documents.forEach { document ->
                val friendsList = document.get("friends") as MutableList<String>
                friendsList.remove(userId)
                batch.update(document.reference, "friends", friendsList)
            }
            // 방 탈출 (? 방에서 나가기 처리)
            val roomsQuerySnapshot = db.collection("PromiseRooms")
                .whereArrayContains("participants", userId)
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

            batch.delete(db.collection("users").document(userId))

            batch.commit().await()
        }
    }

    override suspend fun deleteUserAccount() {
        val user = auth.currentUser
        user?.delete()?.await()
    }
}