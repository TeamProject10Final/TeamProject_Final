package com.example.donotlate.core.data.repository

import android.util.Log
import com.example.donotlate.core.domain.repository.PromiseRoomRepository
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class PromiseRoomRepositoryImpl(private val db: FirebaseFirestore) : PromiseRoomRepository {
    override suspend fun requestDeleteRoom(roomId: String): Flow<Boolean> = flow {
        TODO("Not yet implemented")
    }

    override suspend fun removeParticipant(roomId: String, participantId: String): Flow<Boolean> =
        flow {
            try {
                val roomRef = db.collection("PromiseRooms").document(roomId)
                Log.d("나가기", "실행 1 ${roomRef}")

                db.runTransaction { transaction ->
                    Log.d("나가기", "실행 2")

                    val snapshot = transaction.get(roomRef)
                    val participants = snapshot.get("participants") as? List<String> ?: listOf()
                    Log.d("나가기", "실행 3 ${participants}")
                    val mutableParticipants = participants.toMutableList()

                    mutableParticipants.remove(participantId)
                    Log.d("나가기", "제거 ${mutableParticipants}")
                    Log.d("나가기", "제거 ${participantId}")

                    if (mutableParticipants.isEmpty()) {
                        transaction.delete(roomRef)
                        Log.d("나가기", "실행 4 나감")
                    } else {
                        transaction.update(roomRef, "participants", mutableParticipants)
                    }
                }.await()
                emit(true)
                Log.d("나가기", "실행 5")

            } catch (e: Exception) {
                emit(false)
            }
        }
}
