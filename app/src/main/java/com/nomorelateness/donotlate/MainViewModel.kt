package com.nomorelateness.donotlate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nomorelateness.donotlate.core.domain.session.SessionManager
import com.nomorelateness.donotlate.feature.auth.domain.useCase.CheckUserEmailVerificationUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val sessionManager: SessionManager,
    private val checkUserEmailVerificationUseCase: CheckUserEmailVerificationUseCase
) : ViewModel() {


    private val _channel = Channel<MainAction>()
    val channel = _channel.receiveAsFlow()

    init {
        viewModelScope.launch {
            checkUserLoginStatus()
        }
    }

    private suspend fun checkUserLoginStatus() {
        if (sessionManager.get() != null) {
            val isVerified = checkUserEmailVerificationUseCase()
            if (isVerified) {
                _channel.send(element = MainAction.LoggedIn)
            } else {
                _channel.send(element = MainAction.EmailNotVerified)
            }
        }
        _channel.send(element = MainAction.NotLoggedIn)
    }
}

sealed interface MainAction {
    data object LoggedIn : MainAction
    data object NotLoggedIn : MainAction
    data object EmailNotVerified : MainAction
}

class MainViewModelFactory(
    private val sessionManager: SessionManager,
    private val checkUserEmailVerificationUseCase: CheckUserEmailVerificationUseCase
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(com.nomorelateness.donotlate.MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return com.nomorelateness.donotlate.MainViewModel(
                sessionManager = sessionManager,
                checkUserEmailVerificationUseCase = checkUserEmailVerificationUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}