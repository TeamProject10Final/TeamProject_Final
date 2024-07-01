package com.nomorelateness.donotlate.core.domain.repository

interface UserRepository {
    suspend fun deleteUserData()
    suspend fun deleteUserAccount()
}