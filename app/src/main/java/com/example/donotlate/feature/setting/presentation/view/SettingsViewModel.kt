package com.example.donotlate.feature.setting.presentation.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.donotlate.core.domain.session.SessionManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SettingsViewModel(private val sessionManager: SessionManager) : ViewModel() {
    private val _channel = Channel<SettingsEvent> { }
    val channel = _channel.receiveAsFlow()
    fun logout() {
        viewModelScope.launch {
            try {
                sessionManager.logOut()
                _channel.send(element = SettingsEvent.LoggedOut)
            } catch (e: Exception) {
                _channel.send(
                    element = SettingsEvent.Error(
                        errorMessage = e.message ?: "Unknown Error"
                    )
                )
            }
        }
    }

}

sealed interface SettingsEvent {
    data object LoggedOut : SettingsEvent
    data class Error(val errorMessage: String) : SettingsEvent
}

class SettingsViewModelFactory(private val sessionManager: SessionManager) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(sessionManager = sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}