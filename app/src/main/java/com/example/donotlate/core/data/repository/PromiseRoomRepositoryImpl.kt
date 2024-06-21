package com.example.donotlate.core.data.repository

import android.util.Log
import com.example.donotlate.core.domain.repository.PromiseRoomRepository
import com.google.firebase.auth.FirebaseAuth
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

                db.runTransaction { transaction ->

                    val snapshot = transaction.get(roomRef)
                    val participants = snapshot.get("participants") as? List<String> ?: listOf()
                    val mutableParticipants = participants.toMutableList()

                    mutableParticipants.remove(participantId)

                    if (mutableParticipants.isEmpty()) {
                        transaction.delete(roomRef)
                    } else {
                        transaction.update(roomRef, "participants", mutableParticipants)
                    }
                }.await()
                emit(true)
            } catch (e: Exception) {
                emit(false)
            }
        }
}