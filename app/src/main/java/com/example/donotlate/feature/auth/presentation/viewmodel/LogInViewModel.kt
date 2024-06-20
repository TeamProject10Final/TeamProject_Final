package com.example.donotlate.feature.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.donotlate.feature.auth.domain.useCase.LogInUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LogInViewModel(private val logInUseCase: LogInUseCase) : ViewModel() {

    private val _channel = Channel<LoginEvent> { }
    val channel = _channel.receiveAsFlow()

    fun logIn(email: String, password: String) {
        viewModelScope.launch {
            val result = logInUseCase(email = email, password = password)
            if (result.isSuccess) {
                _channel.send(element = LoginEvent.LoginSuccess)
            } else {
                _channel.send(element = LoginEvent.LoginFail)
            }
        }
    }
}

sealed interface LoginEvent {
    data object LoginFail : LoginEvent
    data object LoginSuccess : LoginEvent
}

class LogInViewModelFactory(private val logInUseCase: LogInUseCase) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LogInViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LogInViewModel(logInUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}