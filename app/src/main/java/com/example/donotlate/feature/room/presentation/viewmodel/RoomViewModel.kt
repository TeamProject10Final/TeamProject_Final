package com.example.donotlate.feature.room.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.donotlate.feature.room.domain.usecase.GetAllUsersUseCase
import com.example.donotlate.feature.room.domain.usecase.MakeAPromiseRoomUseCase
import com.example.donotlate.feature.room.presentation.mapper.toModel
import com.example.donotlate.feature.room.presentation.model.RoomModel
import com.example.donotlate.feature.room.presentation.model.RoomUserModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RoomViewModel(
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val makeAPromiseRoomUseCase: MakeAPromiseRoomUseCase
) : ViewModel() {

//    private val _getAllUserData = MutableStateFlow<List<UserModel>>(listOf())
//    val getAllUserData: StateFlow<List<UserModel>> = _getAllUserData
    private val _getAllUserData = MutableStateFlow<List<RoomUserModel>>(emptyList())
    val getAllUserData: StateFlow<List<RoomUserModel>> = _getAllUserData

    private val _makeARoomResult = MutableStateFlow<Boolean>(false)
    val makeARoomResult: StateFlow<Boolean> get() = _makeARoomResult

    private val _selectedUserNames = MutableLiveData<List<String>>()
    val selectedUserNames: LiveData<List<String>> get() = _selectedUserNames

    fun updateSelectedUserNames(userNames:List<String>){
        _selectedUserNames.value = userNames
    }

    private val _selectedUserUIds = MutableLiveData<List<String>>()
    val selectedUserUIds: LiveData<List<String>> get() = _selectedUserUIds

    fun updateSelectedUserUIds(userIds: List<String>) {
        _selectedUserUIds.value = userIds

        Log.d("seletededed", "${userIds}")
        Log.d("seletededed", "${_selectedUserUIds.value}")
        Log.d("seletededed", "${selectedUserUIds.value}")

    }

    private val _inputText = MutableLiveData<RoomModel>()
//    val inputText: LiveData<RoomModel> get() = _inputText
    val inputText: LiveData<RoomModel> = _inputText

    fun getData(): MutableLiveData<RoomModel> = _inputText

    fun updateText(input: RoomModel) {
        _inputText.value = input
        Log.d("selectededed", "$input")
    }



    fun getAllUserData(){
        viewModelScope.launch {
            try {
                getAllUsersUseCase().collect{ userEntity ->
                    val userModelList = userEntity.map {it.toModel()}
                    _getAllUserData.value = userModelList
                }
            }catch (e:Exception){
                _getAllUserData.value = emptyList()
            }
        }
    }

    fun makeAPromiseRoom(
        roomTitle: String,
        promiseTime: String,
        promiseDate: String,
        destination: String,
        destinationLat: Double,
        destinationLng: Double,
        penalty: String,
        participants: List<String>
    ) {
        viewModelScope.launch {
            Log.d("makeAChatroom2", "title: ${roomTitle}")
            makeAPromiseRoomUseCase(
                roomTitle,
                promiseTime,
                promiseDate,
                destination,
                destinationLat,
                destinationLng,
                penalty,
                participants
            ).collect {
                _makeARoomResult.value = it
            }
        }
    }
}

class RoomViewModelFactory(
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val makeAPromiseRoomUseCase: MakeAPromiseRoomUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoomViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RoomViewModel(
                getAllUsersUseCase,
                makeAPromiseRoomUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
