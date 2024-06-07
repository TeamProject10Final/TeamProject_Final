package com.example.donotlate


import com.example.donotlate.core.data.repository.FirebaseDataSourceImpl

import com.example.donotlate.feature.auth.data.repository.AuthRepositoryImpl
import com.example.donotlate.feature.auth.domain.repository.AuthRepository
import com.example.donotlate.feature.auth.domain.useCase.LogInUseCase
import com.example.donotlate.feature.auth.domain.useCase.SignUpUseCase
import com.example.donotlate.feature.auth.presentation.viewmodel.LogInViewModelFactory
import com.example.donotlate.feature.auth.presentation.viewmodel.SignUpViewmodelFactory
import com.example.donotlate.feature.main.domain.usecase.GetCurrentUserUseCase
import com.example.donotlate.feature.main.domain.usecase.GetUserUseCase
import com.example.donotlate.feature.main.presentation.MainPageViewModelFactory
import com.example.donotlate.feature.room.domain.usecase.GetAllUsersUseCase
import com.example.donotlate.feature.room.presentation.viewmodel.RoomViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AppContainer {

    var logInContainer: LogInContainer? = null
    var signUpContainer: SignUpContainer? = null


    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val firebaseFireStore by lazy {
        FirebaseFirestore.getInstance()
    }

    private val userRepository by lazy {
        FirebaseDataSourceImpl(firebaseFireStore)
    }
//    val getUserUseCase1 by lazy {
//        GetUserUseCase(userRepository)
//    }

    val getUserUseCase by lazy{
        GetUserUseCase(userRepository)
    }

    val getAllUsersUseCase by lazy {
        GetAllUsersUseCase(userRepository)
    }

    val getCurrentUserUseCase by lazy {
        GetCurrentUserUseCase(authRepository)
    }

    val authRepository by lazy {
        AuthRepositoryImpl(firebaseAuth, firebaseFireStore, MyApp.getInstance())
    }

    val logInUseCase by lazy {
        LogInUseCase(authRepository)
    }

    val signUpUseCase by lazy {
        SignUpUseCase(authRepository)
    }

    class LogInContainer(
        private val logInUseCase: LogInUseCase
    ) {
        val logInViewModelFactory = LogInViewModelFactory(logInUseCase)
    }

    class SignUpContainer(
        private val signUpUseCase: SignUpUseCase
    ) {
        val signUpViewModelFactory = SignUpViewmodelFactory(signUpUseCase)
    }

    class MainPageContainer(
        private val getUserUseCase: GetUserUseCase,
        private val getAllUsersUseCase: GetAllUsersUseCase,
        private val getCurrentUserUseCase: GetCurrentUserUseCase
    ){
        val mainPageViewModelFactory = MainPageViewModelFactory(
            getUserUseCase, getAllUsersUseCase, getCurrentUserUseCase
        )
    }

    class RoomContainer(
        private val getAllUsersUseCase: GetAllUsersUseCase
    ){
        val roomViewModelFactory = RoomViewModelFactory(getAllUsersUseCase)
    }
}