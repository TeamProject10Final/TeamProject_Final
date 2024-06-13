package com.example.donotlate.feature.chatroom.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.donotlate.core.domain.usecase.AcceptFriendRequestsUseCase
import com.example.donotlate.core.domain.usecase.GetCurrentUserUseCase
import com.example.donotlate.core.domain.usecase.GetFriendRequestsListUseCase
import com.example.donotlate.core.domain.usecase.GetFriendRequestsStatusUseCase
import com.example.donotlate.core.domain.usecase.GetFriendsListFromFirebaseUseCase
import com.example.donotlate.core.domain.usecase.GetUserDataUseCase
import com.example.donotlate.core.domain.usecase.MakeAFriendRequestUseCase
import com.example.donotlate.core.domain.usecase.SearchUserByIdUseCase
import com.example.donotlate.feature.chatroom.domain.usecase.MakeAPromiseRoomUseCase
import com.example.donotlate.feature.friends.presentation.mapper.toModel
import com.example.donotlate.feature.friends.presentation.model.FriendsUserModel
import com.example.donotlate.feature.friends.presentation.viewmodel.FriendsViewModel
import com.example.donotlate.feature.main.presentation.model.UserModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatRoomViewModel(
    private val makeAPromiseRoomUseCase: MakeAPromiseRoomUseCase
) : ViewModel() {

    private val _makeARoomResult = MutableStateFlow<Boolean>(false)
    val makeARoomResult: StateFlow<Boolean> get() = _makeARoomResult

    fun makeAPromiseRoom(
        roomTitle: String,
        promiseTime: String,
        promiseDate: String,
        destination: String,
        destinationLat: Double,
        destinationLng: Double,
        penalty: String,
        participants: List<UserModel>
    ) {
        viewModelScope.launch {

            makeAPromiseRoomUseCase(
                roomTitle,
                promiseTime,
                promiseDate,
                destination,
                destinationLat,
                destinationLng,
                penalty,
                participants).collect {
                _makeARoomResult.value = it
            }
        }
    }
}

class ChatRoomViewModelFactory(private val makeAPromiseRoomUseCase: MakeAPromiseRoomUseCase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatRoomViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChatRoomViewModel(
                makeAPromiseRoomUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
