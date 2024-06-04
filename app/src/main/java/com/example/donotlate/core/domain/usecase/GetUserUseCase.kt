package com.example.donotlate.core.domain.usecase

import com.example.donotlate.core.domain.model.User
import com.example.donotlate.core.domain.repository.UserRepository

class GetUserUseCase (private val userRepository: UserRepository){
    suspend operator fun invoke(userId:String): Result<User>{
        return userRepository.getUserById(userId)
    }
}