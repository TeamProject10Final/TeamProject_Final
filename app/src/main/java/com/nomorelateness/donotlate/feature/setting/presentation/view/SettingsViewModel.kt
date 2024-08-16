package com.nomorelateness.donotlate.feature.setting.presentation.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nomorelateness.donotlate.core.domain.session.SessionManager
import com.nomorelateness.donotlate.feature.consumption.domain.usecase.ClearAllConsumptionsUseCase
import com.nomorelateness.donotlate.feature.main.presentation.model.Result
import com.nomorelateness.donotlate.feature.setting.domain.usecase.DeleteUserUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val sessionManager: SessionManager,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val clearAllConsumptionsUseCase: ClearAllConsumptionsUseCase
) : ViewModel() {
    private val _channel = Channel<SettingsEvent> { }
    val channel = _channel.receiveAsFlow()
    fun logout() {
        viewModelScope.launch {
            val kakaoToken = sessionManager.get1()
            val firebaseUid = try{
                sessionManager.get()
            }catch (e: Exception){
                null
            }
            try {
                when {
                    kakaoToken != null -> {
                        sessionManager.logOutWithKakao()
                        clearAllConsumptionsUseCase.invoke()
                        _channel.send(element = SettingsEvent.LoggedOut)
                    }
                    firebaseUid != null -> {
                        sessionManager.logOut()
                        clearAllConsumptionsUseCase.invoke()
                        _channel.send(element = SettingsEvent.LoggedOut)
                    }
                }
            } catch (e: Exception) {
                _channel.send(
                    element = SettingsEvent.Error(
                        errorMessage = e.message ?: "Unknown Error"
                    )
                )
            }
        }
    }

    fun deleteUser() {
        viewModelScope.launch {
            deleteUserUseCase().collect { result ->
                when (result) {
                    is Result.Success -> {
                        sessionManager.logOut()
                        _channel.send(SettingsEvent.UserDeleted)
                    }

                    is Result.Failure -> {
                        _channel.send(
                            SettingsEvent.Error(
                                errorMessage = result.exception.message ?: "Unknown Error"
                            )
                        )
                    }
                }
            }
        }
    }

}

sealed interface SettingsEvent {
    data object LoggedOut : SettingsEvent
    data object UserDeleted : SettingsEvent
    data class Error(val errorMessage: String) : SettingsEvent
}

class SettingsViewModelFactory(
    private val sessionManager: SessionManager,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val clearAllConsumptionsUseCase: ClearAllConsumptionsUseCase
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(
                sessionManager = sessionManager,
                deleteUserUseCase = deleteUserUseCase,
                clearAllConsumptionsUseCase = clearAllConsumptionsUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}