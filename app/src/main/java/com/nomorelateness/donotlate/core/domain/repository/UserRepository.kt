package com.nomorelateness.donotlate.core.domain.repository

interface UserRepository {
    suspend fun deleteUserData(userId: String)
    suspend fun deleteUserAccount()
}