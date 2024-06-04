package com.example.donotlate.feature.auth.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.donotlate.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.launch

class LogInViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _logInResult = MutableLiveData<Result<String>>()
    val logInResult: LiveData<Result<String>> get() = _logInResult

    fun logIn(email: String, password: String) {
        viewModelScope.launch {
            val result = authRepository.logIn(email, password)
            _logInResult.value = result
        }
    }
}

class LogInViewModelFactory(private val authRepository: AuthRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LogInViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LogInViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}