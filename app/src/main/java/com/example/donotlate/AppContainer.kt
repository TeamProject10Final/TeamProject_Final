package com.example.donotlate



import com.example.donotlate.core.data.repository.FirebaseDataSourceImpl
import com.example.donotlate.feature.auth.data.repository.AuthRepositoryImpl
import com.example.donotlate.feature.auth.domain.repository.AuthRepository
import com.example.donotlate.feature.auth.domain.useCase.LogInUseCase
import com.example.donotlate.feature.auth.domain.useCase.SignUpUseCase
import com.example.donotlate.feature.auth.presentation.viewmodel.LogInViewModelFactory
import com.example.donotlate.feature.auth.presentation.viewmodel.SignUpViewmodelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AppContainer {

    var logInContainer: LogInContainer? = null
    var signUpContainer: SignUpContainer? = null


    private val userRepository by lazy {
        FirebaseDataSourceImpl(firebaseFireStore)
    }

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val firebaseFireStore by lazy {
        FirebaseFirestore.getInstance()
    }

    val authRepository by lazy {
        AuthRepositoryImpl(firebaseAuth,firebaseFireStore, MyApp.getInstance())
    }

    val logInUseCase by lazy{
        LogInUseCase(authRepository)
    }

    val signUpUseCase by lazy{
        SignUpUseCase(authRepository)
    }
}

class LogInContainer(
    private val authRepository: AuthRepository
){
    val logInViewModelFactory = LogInViewModelFactory(authRepository)
}

class SignUpContainer(
    private val signUpUseCase: SignUpUseCase
){
    val signUpViewModelFactory = SignUpViewmodelFactory(signUpUseCase)
}