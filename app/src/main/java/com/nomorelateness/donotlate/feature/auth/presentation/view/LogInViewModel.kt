package com.nomorelateness.donotlate.feature.auth.presentation.view

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.nomorelateness.donotlate.core.domain.session.SessionManager
import com.nomorelateness.donotlate.core.domain.usecase.GetCurrentUserDataUseCase
import com.nomorelateness.donotlate.feature.auth.domain.useCase.CheckUserEmailVerificationUseCase
import com.nomorelateness.donotlate.feature.auth.domain.useCase.DeleteUseCase
import com.nomorelateness.donotlate.feature.auth.domain.useCase.GetCurrentUserWithKakaoUseCase
import com.nomorelateness.donotlate.feature.auth.domain.useCase.LogInUseCase
import com.nomorelateness.donotlate.feature.auth.domain.useCase.SendEmailVerificationUseCase
import com.nomorelateness.donotlate.feature.auth.domain.useCase.SignUpWithKakaoUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LogInViewModel(
    private val logInUseCase: LogInUseCase,
    private val checkUserEmailVerificationUseCase: CheckUserEmailVerificationUseCase,
    private val sendVerificationUseCase: SendEmailVerificationUseCase,
    private val sessionManager: SessionManager,
    private val deleteUseCase: DeleteUseCase,
    private val getCurrentUserDataUseCase: GetCurrentUserDataUseCase,
    private val signUpWithKakaoUseCase: SignUpWithKakaoUseCase,
    private val getCurrentUserWithKakaoUseCase: GetCurrentUserWithKakaoUseCase,
) : ViewModel() {

    private val _channel = Channel<LoginEvent> { }
    val channel = _channel.receiveAsFlow()

    fun logIn(email: String, password: String) {
        viewModelScope.launch {
            Log.d("LogInViewModel", "Attempting to log in with email: $email")
            val result = logInUseCase(email = email, password = password)
            if (result.isSuccess) {
                Log.d("LogInViewModel", "Login successful")
                val isVerified = checkUserEmailVerificationUseCase()
                Log.d("LogInViewModel", "Email verification status: $isVerified")
                if (isVerified) {
                    Log.d("LogInViewModel", "Email is verified")
                    _channel.send(element = LoginEvent.LoginSuccess)
                } else {
                    Log.d("LogInViewModel", "Email is not verified")
                    _channel.send(element = LoginEvent.EmailNotVerified)
                }
            } else {
                Log.d("LogInViewModel", "Login failed")
                _channel.send(element = LoginEvent.LoginFail)
            }
        }
    }

    fun sendEmailVerification() {
        viewModelScope.launch {
            try {
                // 재전송을 위해 유저를 다시 로그인
                Log.d("LogInViewModel", "Sending verification email")
                sendVerificationUseCase.invoke()
                Log.d("LogInViewModel", "Verification email sent successfully")
            } catch (e: Exception) {
                Log.e("LogInViewModel", "Error sending verification email: ${e.message}")
            }
        }
    }

    fun deleteUser() {
        viewModelScope.launch {
            Log.d("LogInViewModel", "Deleting user")
            deleteUseCase()
        }
    }

    fun handleKakaoLoginResult(token: OAuthToken?, error: Throwable?) {
        viewModelScope.launch {
            if (error != null) {
                Log.e("KakaoLogin", "Error: ${error.message}")
                _channel.send(LoginEvent.LoginFail)
            } else if (token != null) {
                Log.d("KakaoLogin", "Token received: ${token.accessToken}")
                fetchKakaoUserInfo()
            }
        }
    }

    private fun fetchKakaoUserInfo() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                viewModelScope.launch {
                    _channel.send(element = LoginEvent.LoginFail)
                    Log.e("KakaoLogin", "Error fetching user info: ${error.message}")
                }
            } else if (user != null) {
                Log.d("KakaoLogin", "${user}")
                val email = user.kakaoAccount?.email ?: ""
                val nickname = user.kakaoAccount?.profile?.nickname ?: ""
                val uid = user.id.toString()

                Log.d(
                    "KakaoLogin",
                    "Id: ${user.id.toString()}, email: ${user.kakaoAccount?.email}, nickname: ${user.kakaoAccount?.profile?.nickname}"
                )

                viewModelScope.launch {
                    Log.d("KakaoLogin", "viewmodel scope")
                    try {
                        getCurrentUserWithKakaoUseCase(uid).collect { documents ->
                            Log.d("KakaoLogin", "Fetched documents: $documents")
                            if (documents != null && documents.exists()) {
                                Log.d("KakaoLogin", "document is NotNul")
                                _channel.send(LoginEvent.KakaoLoginSuccess)

                            } else {
                                Log.d("KakaoLogin", "document is null")
                                signUpWithKakao(name = nickname, email = email, uid = uid)

                            }
                        }

                    } catch (e: Exception) {
                        Log.d("KakaoLogin", "${e.message}")
                        _channel.send(LoginEvent.LoginFail)

                    }
                }

            }
        }
    }

    private fun signUpWithKakao(name: String, email: String, uid: String) {
        viewModelScope.launch {
            try {
                val result = signUpWithKakaoUseCase(name, email, uid)
                if (result.isSuccess) {
                    Log.d("KakaoLogin", "Sign up with Kakao successful")
                    _channel.send(LoginEvent.KakaoLoginSuccess)
                } else {
                    Log.d("KakaoLogin", "Sign up with Kakao failed: ${result.exceptionOrNull()?.message}")
                    _channel.send(LoginEvent.KakaoSignUpFail)
                }
            }catch (e: Exception){
                Log.d("KakaoLogin", "${e.message}")
            }

        }
    }
}

sealed interface LoginEvent {
    data object LoginFail : LoginEvent
    data object LoginSuccess : LoginEvent
    data object EmailNotVerified : LoginEvent
    data object KakaoLoginSuccess : LoginEvent
    data object KakaoSignUpFail : LoginEvent
}

class LogInViewModelFactory(
    private val logInUseCase: LogInUseCase,
    private val checkUserEmailVerificationUseCase: CheckUserEmailVerificationUseCase,
    private val sendVerificationUseCase: SendEmailVerificationUseCase,
    private val sessionManager: SessionManager,
    private val deleteUseCase: DeleteUseCase,
    private val getCurrentUserDataUseCase: GetCurrentUserDataUseCase,
    private val signUpWithKakaoUseCase: SignUpWithKakaoUseCase,
    private val getCurrentUserWithKakaoUseCase: GetCurrentUserWithKakaoUseCase,
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LogInViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LogInViewModel(
                logInUseCase = logInUseCase,
                checkUserEmailVerificationUseCase = checkUserEmailVerificationUseCase,
                sendVerificationUseCase = sendVerificationUseCase,
                sessionManager = sessionManager,
                deleteUseCase = deleteUseCase,
                getCurrentUserDataUseCase = getCurrentUserDataUseCase,
                signUpWithKakaoUseCase = signUpWithKakaoUseCase,
                getCurrentUserWithKakaoUseCase = getCurrentUserWithKakaoUseCase,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}