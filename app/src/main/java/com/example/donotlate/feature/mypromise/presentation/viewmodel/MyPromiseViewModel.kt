package com.example.donotlate.feature.mypromise.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.donotlate.core.domain.usecase.GetCurrentUserUseCase
import com.example.donotlate.core.domain.usecase.GetMyDataFromFireStoreUseCase
import com.example.donotlate.core.domain.usecase.GetUserDataUseCase
import com.example.donotlate.core.domain.usecase.LoadToMyPromiseListUseCase
import com.example.donotlate.feature.mypromise.domain.usecase.MessageReceivingUseCase
import com.example.donotlate.feature.mypromise.domain.usecase.MessageSendingUseCase
import com.example.donotlate.feature.mypromise.presentation.mapper.toMessageEntity
import com.example.donotlate.feature.mypromise.presentation.mapper.toMessageModel
import com.example.donotlate.feature.mypromise.presentation.mapper.toModel
import com.example.donotlate.feature.mypromise.presentation.mapper.toPromiseModelList
import com.example.donotlate.feature.mypromise.presentation.mapper.toViewType
import com.example.donotlate.feature.mypromise.presentation.model.MessageModel
import com.example.donotlate.feature.mypromise.presentation.model.MessageViewType
import com.example.donotlate.feature.mypromise.presentation.model.PromiseModel
import com.example.donotlate.feature.mypromise.presentation.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MyPromiseViewModel(
    private val loadToMyPromiseListUseCase: LoadToMyPromiseListUseCase,
    private val messageSendingUseCase: MessageSendingUseCase,
    private val messageReceivingUseCase: MessageReceivingUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val getMyDataFromFireStoreUseCase: GetMyDataFromFireStoreUseCase
) : ViewModel() {

    private var currentUId: String? = null


    private val _promiseRoomList = MutableStateFlow<List<PromiseModel>>(emptyList())
    val promiseRoomModel: StateFlow<List<PromiseModel>> get() = _promiseRoomList

    private val _message = MutableStateFlow<List<MessageViewType>>(listOf())
    val message: StateFlow<List<MessageViewType>> get() = _message

    private val _currentUserId = MutableStateFlow<String>("")
    val currentUserId: StateFlow<String> get() = _currentUserId

    private val _currentUserData = MutableStateFlow<UserModel?>(null)
    val currentUserData: StateFlow<UserModel?> get() = _currentUserData

    private val _messageSendResults = MutableStateFlow<Boolean>(false)
    val messageSendResult: StateFlow<Boolean> get() = _messageSendResults

    val mAuth = FirebaseAuth.getInstance().currentUser?.uid

    init {
        getCurrentUId()
        getCurrentUserData()
    }

    private fun getCurrentUId() {
        viewModelScope.launch {
            currentUId = _currentUserId.value
        }
    }

    private fun getCurrentUserData() {
        viewModelScope.launch {
            getMyDataFromFireStoreUseCase().collect { userData ->
                _currentUserData.value = userData?.toModel()
            }
        }
    }

    fun loadPromiseRooms() {
        viewModelScope.launch {
            loadToMyPromiseListUseCase().collect {
                val promiseRooms = it.toPromiseModelList()
                _promiseRoomList.value = promiseRooms
                Log.d("PromiseList", "${_promiseRoomList.value}")
            }
        }
    }

    fun loadMessage(roomId: String) {
        viewModelScope.launch {
            messageReceivingUseCase(roomId).collect { entities ->
                val model = entities.map { it.toMessageModel() }
                Log.d("tttt", "$currentUId")
                _message.value = model.map { it.toViewType(mAuth ?: "") }

            }
        }
    }

    fun sendMessage(roomId: String, message: MessageModel) {
        try {
            viewModelScope.launch {
                Log.d("ddddddd3", "$roomId")
                messageSendingUseCase(roomId, message.toMessageEntity()).collect { result ->
                    _messageSendResults.value = result
                }
                Log.d("ddddddd4", "$roomId")
            }
        } catch (e: Exception) {
            Log.d("ddddddd8", "rror: Send To Message Error: $e")
        }

    }

    fun getCurrentUserId() {
        viewModelScope.launch {
            getCurrentUserUseCase().collect { uId ->
                _currentUserId.value = uId
            }
        }
    }
}

class MyPromiseViewModelFactory(
    private val loadToMyPromiseListUseCase: LoadToMyPromiseListUseCase,
    private val messageSendingUseCase: MessageSendingUseCase,
    private val messageReceivingUseCase: MessageReceivingUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val getMyDataFromFireStoreUseCase: GetMyDataFromFireStoreUseCase
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyPromiseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MyPromiseViewModel(
                loadToMyPromiseListUseCase,
                messageSendingUseCase,
                messageReceivingUseCase,
                getCurrentUserUseCase,
                getUserDataUseCase,
                getMyDataFromFireStoreUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
