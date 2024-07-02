package com.nomorelateness.donotlate.feature.auth.presentation.view

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nomorelateness.donotlate.core.domain.session.SessionManager
import com.nomorelateness.donotlate.feature.auth.domain.useCase.CheckUserEmailVerificationUseCase
import com.nomorelateness.donotlate.feature.auth.domain.useCase.DeleteUseCase
import com.nomorelateness.donotlate.feature.auth.domain.useCase.LogInUseCase
import com.nomorelateness.donotlate.feature.auth.domain.useCase.SendEmailVerificationUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LogInViewModel(
    private val logInUseCase: LogInUseCase,
    private val checkUserEmailVerificationUseCase: CheckUserEmailVerificationUseCase,
    private val sendVerificationUseCase: SendEmailVerificationUseCase,
    private val sessionManager: SessionManager,
    private val deleteUseCase: DeleteUseCase
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
}

sealed interface LoginEvent {
    data object LoginFail : LoginEvent
    data object LoginSuccess : LoginEvent
    data object EmailNotVerified : LoginEvent
}

class LogInViewModelFactory(
    private val logInUseCase: LogInUseCase,
    private val checkUserEmailVerificationUseCase: CheckUserEmailVerificationUseCase,
    private val sendVerificationUseCase: SendEmailVerificationUseCase,
    private val sessionManager: SessionManager,
    private val deleteUseCase: DeleteUseCase
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LogInViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LogInViewModel(
                logInUseCase,
                checkUserEmailVerificationUseCase,
                sendVerificationUseCase,
                sessionManager,
                deleteUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}