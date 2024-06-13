package com.example.donotlate.feature.friends.presentation.viewmodel

import android.util.Log
import android.widget.Toast
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
import com.example.donotlate.feature.friends.presentation.mapper.toModel
import com.example.donotlate.feature.friends.presentation.mapper.toModelList
import com.example.donotlate.feature.friends.presentation.model.FriendRequestModel
import com.example.donotlate.feature.friends.presentation.model.FriendRequestWithUserDataModel

import com.example.donotlate.feature.friends.presentation.model.FriendsUserModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FriendsViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getFriendsListFromFirebaseUseCase: GetFriendsListFromFirebaseUseCase,
    private val searchUserByIdUseCase: SearchUserByIdUseCase,
    private val makeAFriendRequestUseCase: MakeAFriendRequestUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val getFriendRequestsStatusUseCase: GetFriendRequestsStatusUseCase,
    private val getFriendRequestsListUseCase: GetFriendRequestsListUseCase,
    private val acceptFriendRequestsUseCase: AcceptFriendRequestsUseCase,
) : ViewModel() {

    private val _friendsList = MutableStateFlow<List<FriendsUserModel>>(listOf())
    val friendsList: StateFlow<List<FriendsUserModel>> get() = _friendsList

    private val _searchUserList = MutableStateFlow<List<FriendsUserModel>>(listOf())
    val searchUserList: StateFlow<List<FriendsUserModel>> get() = _searchUserList

    private val _friendRequest = MutableStateFlow<Boolean>(false)
    val friendRequest: StateFlow<Boolean> get() = _friendRequest

    private val _getCurrentUser = MutableStateFlow<String?>("")
    val getCurrentUser: StateFlow<String?> get() = _getCurrentUser

    private val _currentUserdata = MutableStateFlow<FriendsUserModel?>(null)
    val currentUserData: StateFlow<FriendsUserModel?> get() = _currentUserdata

    private val _checkRequestStatus = MutableStateFlow<Map<String, FriendRequestModel?>>(emptyMap())
    val checkRequestStatus: StateFlow<Map<String, FriendRequestModel?>> get() = _checkRequestStatus

    private val _friendRequestList =
        MutableStateFlow<List<FriendRequestWithUserDataModel>>(emptyList())
    val friendRequestList: StateFlow<List<FriendRequestWithUserDataModel>> get() = _friendRequestList

    private val _requestsResult = MutableStateFlow<Boolean> (false)
    val requestResult: StateFlow<Boolean> get() = _requestsResult

    init {
        getCurrentUserData()
        loadFriendRequestList()
    }

    fun getFriendsList() {
        viewModelScope.launch {
            getCurrentUserUseCase().collect { uid ->
                _getCurrentUser.value = uid

                if (uid.isNotBlank()) {
                    Log.d("FriendsViewModel", "UID is not blank: $uid")
                    getFriendsListFromFirebaseUseCase(uid).collect { friends ->
                        Log.d("FriendsViewModel", "Fetched friends: $friends")
                        _friendsList.value = friends.map { it.toModel() }
                    }
                }
            }
        }
    }

    private fun getCurrentUserData() {
        viewModelScope.launch {
            getCurrentUserUseCase().collect { uid ->
                if (uid.isNotBlank()) {
                    getUserDataUseCase(uid).collect { currentUser ->
                        _currentUserdata.value = currentUser?.toModel()

                        Log.d("getCurrentUserData", "${currentUserData.value}")
                    }
                }
            }
        }
    }

    fun searchUserById(searchId: String) {
        viewModelScope.launch {
            searchUserByIdUseCase(searchId).collect { result ->
                Log.d("FriendsViewModel", "Search Results: $result")
                _searchUserList.value = result.map { it.toModel() }
            }
        }
    }

    fun sendFriendRequest(toId: String, fromId: String, fromUserName: String, requestId: String) {
        viewModelScope.launch {
            makeAFriendRequestUseCase(toId, fromId, fromUserName, requestId).collect { it ->
                _friendRequest.value = it
            }
        }
    }

    fun checkFriendRequestStatus(requestId: String) {
        viewModelScope.launch {
            getFriendRequestsStatusUseCase(requestId).collect { status ->
                _checkRequestStatus.update { it + (requestId to status?.toModel()) }
            }
        }
    }

    private fun loadFriendRequestList() {
        viewModelScope.launch {
            getCurrentUserUseCase().collect { toId ->
                getFriendRequestsListUseCase(toId).collect { requestList ->
                    _friendRequestList.value = requestList.toModelList()
                }
            }
        }
    }
    fun acceptToFriendRequest(requestId: String){
        viewModelScope.launch {
            acceptFriendRequestsUseCase(requestId).collect{
                }
            }
            Log.d("requestIdTest", "${requestId}")
        }
    }


class FriendsViewModelFactory(
    private val getFriendsListFromFirebaseUseCase: GetFriendsListFromFirebaseUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val searchUserByIdUseCase: SearchUserByIdUseCase,
    private val makeAFriendRequestUseCase: MakeAFriendRequestUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val getFriendRequestsStatusUseCase: GetFriendRequestsStatusUseCase,
    private val getFriendRequestsListUseCase: GetFriendRequestsListUseCase,
    private val acceptFriendRequestsUseCase: AcceptFriendRequestsUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FriendsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FriendsViewModel(
                getCurrentUserUseCase,
                getFriendsListFromFirebaseUseCase,
                searchUserByIdUseCase,
                makeAFriendRequestUseCase,
                getUserDataUseCase,
                getFriendRequestsStatusUseCase,
                getFriendRequestsListUseCase,
                acceptFriendRequestsUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
