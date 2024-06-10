package com.example.donotlate.feature.room.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.donotlate.feature.room.domain.usecase.GetAllUsersUseCase
import com.example.donotlate.feature.room.presentation.mapper.toModel
import com.example.donotlate.feature.room.presentation.model.RoomModel
import com.example.donotlate.feature.room.presentation.model.UserModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RoomViewModel(
    private val getAllUsersUseCase: GetAllUsersUseCase
) : ViewModel() {

    private val _getAllUserData = MutableStateFlow<List<UserModel>>(listOf())
    val getAllUserData: StateFlow<List<UserModel>> = _getAllUserData

    private val _inputText = MutableLiveData<RoomModel>()
    val inputText: LiveData<RoomModel>
        get() = _inputText

    fun getData(): MutableLiveData<RoomModel> = _inputText

    fun updateText(input: RoomModel) {
        _inputText.value = input
    }

    fun getAllUserData() {
        try {
            viewModelScope.launch {
                getAllUsersUseCase().collect { userEntity ->
                    val userModelList = userEntity.map { it.toModel() }
                    _getAllUserData.value = userModelList

                }
            }
        } catch (e: Exception) {
            _getAllUserData.value = listOf()
        }
    }
}

class RoomViewModelFactory(
    private val getAllUsersUseCase: GetAllUsersUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoomViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RoomViewModel(
                getAllUsersUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
