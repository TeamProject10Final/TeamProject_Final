package com.nomorelateness.donotlate

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.user.UserApiClient
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

        val kakaoToken = sessionManager.get1()
        val firebaseUid = try {
            sessionManager.get()
        } catch (e: Exception) {
            null
        }

        when {
            kakaoToken != null -> {
                _channel.send(element = MainAction.LoggedIn)
            }
            firebaseUid != null -> {
                val isVerified = checkUserEmailVerificationUseCase()
                if (isVerified) {
                    _channel.send(element = MainAction.LoggedIn)
                } else {
                    _channel.send(element = MainAction.EmailNotVerified)
                }
            }
            else -> {
                _channel.send(element = MainAction.NotLoggedIn)
            }

        }
//        if (sessionManager.get1() != null) {
//            _channel.send(element = MainAction.LoggedIn)
//            Log.d("KakaoLogin", "1${sessionManager.get1()}")
//        } else if (sessionManager.get1() == null) {
//            Log.d("KakaoLogin", "2${sessionManager.get1()}")
//            _channel.send(element = MainAction.NotLoggedIn)
//
//        } else if (sessionManager.get() != null) {
//            val isVerified = checkUserEmailVerificationUseCase()
//            if (isVerified) {
//                _channel.send(element = MainAction.LoggedIn)
//            } else {
//                _channel.send(element = MainAction.EmailNotVerified)
//            }
//        } else {
//            _channel.send(element = MainAction.NotLoggedIn)
//        }
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