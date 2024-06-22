package com.example.donotlate.feature.friends.data.repository

import android.content.Context
import android.util.Log
import com.example.donotlate.feature.friends.domain.repository.FriendRequestRepository
import com.example.donotlate.feature.friends.presentation.model.FriendsUserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FriendRequestRepositoryImpl(
    private val db: FirebaseFirestore,
) : FriendRequestRepository {

    override suspend fun makeAFriendRequest(
        toId: String,
        fromId: String,
        fromUserName: String
    ): Flow<Boolean> = flow {
        try {
            val request = hashMapOf(
                "requestId" to "",
                "toId" to toId,
                "fromId" to fromId,
                "status" to "request",
                "requestTime" to FieldValue.serverTimestamp(),
                "fromUserName" to fromUserName
            )
            db.collection("friendRequests").add(request).await()
            emit(true)
        } catch (e: Exception) {
            emit(false)
        }
    }
}


