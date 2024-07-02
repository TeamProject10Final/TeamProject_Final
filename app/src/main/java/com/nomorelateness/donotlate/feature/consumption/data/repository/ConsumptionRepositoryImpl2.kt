package com.nomorelateness.donotlate.feature.consumption.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nomorelateness.donotlate.feature.consumption.domain.entity.FirebaseConsumptionEntity
import com.nomorelateness.donotlate.feature.consumption.domain.repository.ConsumptionRepository2
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class ConsumptionRepositoryImpl2(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ConsumptionRepository2 {
    override suspend fun insertConsumption(consumption: FirebaseConsumptionEntity): Flow<Result<Boolean>> =
        flow {
            try {
                val userId = auth.currentUser?.uid
                Log.d(
                    "FirestoreRepositoryImpl",
                    "Attempting to insert consumption for user: $userId"
                )
                if (userId != null) {
                    db.collection("users")
                        .document(userId)
                        .collection("consumptions")
                        .document(consumption.historyId.toString())
                        .set(consumption)
                        .await()
                    Log.d("FirestoreRepositoryImpl", "Consumption inserted successfully")
                    emit(Result.success(true))
                } else {
                    Log.e("FirestoreRepositoryImpl", "User not authenticated")
                    emit(Result.failure(Exception()))
                }
            } catch (e: Exception) {
                Log.e("FirestoreRepositoryImpl", "Exception during insert consumption", e)
                emit(Result.failure(e))

            }
        }

    override suspend fun deleteConsumption(consumption: FirebaseConsumptionEntity): Flow<Result<Boolean>> =
        flow {
            try {
                val userId = auth.currentUser?.uid
                Log.d(
                    "FirestoreRepositoryImpl",
                    "Attempting to delete consumption for user: $userId"
                )
                if (userId != null) {
                    db.collection("users")
                        .document(userId)
                        .collection("consumptions")
                        .document(consumption.historyId)
                        .delete()
                        .await()

                    Log.d("FirestoreRepositoryImpl", "Consumption deleted successfully")
                    emit(Result.success(true))
                } else {
                    Log.e("FirestoreRepositoryImpl", "User not authenticated")
                    emit(Result.failure(Exception("User not authenticated")))
                }
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }

    override suspend fun updateFinished(consumption: FirebaseConsumptionEntity) {
        try {
            val userId = auth.currentUser?.uid
            Log.d("FirebaseRepository", "Update hasFinished ${userId}")
            if (userId != null) {
                val docRef = db.collection("users").document(userId).collection("consumptions")
                    .document(consumption.historyId)

                val result = db.runTransaction { transaction ->
                    val consumptionDoc = transaction.get(docRef)
                    if (consumptionDoc.exists()) {

                        val newFinishedState = !consumption.hasFinished
                        transaction.update(docRef, "hasFinished", newFinishedState)

                        Log.d(
                            "FirebaseRepository",
                            "Update hasFinished ${!consumption.hasFinished}"
                        )
                        true
                    } else {
                        Log.d("FirebaseRepository", "dose not exist")
                        false
                    }
                }.await()
            }
        } catch (e: Exception) {

        }
    }

    override suspend fun getAllConsumption(): Flow<List<FirebaseConsumptionEntity>> = flow {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val snapshot = db.collection("users")
                .document(userId)
                .collection("consumptions")
                .get().await()
            val consumptions = snapshot.toObjects(FirebaseConsumptionEntity::class.java)
            emit(consumptions)
        }
    }
}