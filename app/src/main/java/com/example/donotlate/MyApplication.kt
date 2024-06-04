package com.example.donotlate

//
//class MyApplication : Application() {
//
//    override fun onCreate() {
//        super.onCreate()
//    }
//
//    private val firebaseAuth by lazy {
//        FirebaseAuth.getInstance()
//    }
//
//    private val firebaseFireStore by lazy {
//        FirebaseFirestore.getInstance()
//    }
//
//    private val firebaseDataStore by lazy {
//        FirebaseDataSourceImpl(firebaseAuth, firebaseFireStore)
//    }
//
//    private val authRepository by lazy {
//        AuthRepositoryImpl(firebaseDataStore)
//    }
//
//    val logInUseCase by lazy {
//        LogInUseCase(authRepository)
//    }
//}