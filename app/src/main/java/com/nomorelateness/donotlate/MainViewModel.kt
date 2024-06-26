package com.nomorelateness.donotlate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nomorelateness.donotlate.core.domain.session.SessionManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainViewModel(private val sessionManager: SessionManager) : ViewModel() {


    private val _channel = Channel<com.nomorelateness.donotlate.MainAction>()
    val channel = _channel.receiveAsFlow()

    init {
        viewModelScope.launch {
            checkUserLoginStatus()
        }
    }

    private suspend fun checkUserLoginStatus() {
        if (sessionManager.get() != null) {
            _channel.send(element = com.nomorelateness.donotlate.MainAction.LoggedIn)
            return
        }
        _channel.send(element = com.nomorelateness.donotlate.MainAction.NotLoggedIn)
    }
}

sealed interface MainAction {
    data object LoggedIn : com.nomorelateness.donotlate.MainAction
    data object NotLoggedIn : com.nomorelateness.donotlate.MainAction
}

class MainViewModelFactory(private val sessionManager: SessionManager) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(com.nomorelateness.donotlate.MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return com.nomorelateness.donotlate.MainViewModel(sessionManager = sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}