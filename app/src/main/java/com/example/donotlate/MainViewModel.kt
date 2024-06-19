package com.example.donotlate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.donotlate.core.domain.session.SessionManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainViewModel(private val sessionManager: SessionManager) : ViewModel() {


    private val _channel = Channel<MainAction>()
    val channel = _channel.receiveAsFlow()

    init {
        viewModelScope.launch {
            checkUserLoginStatus()
        }
    }

    private suspend fun checkUserLoginStatus() {
        if (sessionManager.get() != null) {
            _channel.send(element = MainAction.LoggedIn)
            return
        }
        _channel.send(element = MainAction.NotLoggedIn)
    }
}

sealed interface MainAction {
    data object LoggedIn : MainAction
    data object NotLoggedIn : MainAction
}

class MainViewModelFactory(private val sessionManager: SessionManager) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(sessionManager = sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}