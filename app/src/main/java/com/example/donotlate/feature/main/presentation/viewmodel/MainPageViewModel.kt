package com.example.donotlate.feature.main.presentation.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.donotlate.core.domain.usecase.GetCurrentUserUseCase
import com.example.donotlate.core.domain.usecase.GetUserDataUseCase
import com.example.donotlate.feature.main.domain.usecase.GetUserUseCase
import com.example.donotlate.feature.main.presentation.mapper.toModel
import com.example.donotlate.feature.main.presentation.model.UserModel
import com.example.donotlate.feature.room.domain.usecase.GetAllUsersUseCase
import com.example.donotlate.feature.setting.domain.usecase.ImageUploadUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MainPageViewModel(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val getCurrentGetUserUseCase: GetCurrentUserUseCase,
    private val imageUploadUseCase: ImageUploadUseCase,
) : ViewModel() {

    private val _getUserData = MutableStateFlow<Result<UserModel>?>(null)
    val getUserData: StateFlow<Result<UserModel>?> = _getUserData

    private val _getCurrentUser = MutableStateFlow<Result<String?>>(Result.success(""))
    val getCurrentUser: StateFlow<Result<String?>> get() = _getCurrentUser

    private val _getAllUserData = MutableStateFlow<List<UserModel>>(listOf())
    val getAllUserData: StateFlow<List<UserModel>> = _getAllUserData

    private val _profileImageUrl = MutableStateFlow<String>("")
    val profileImageUrl: StateFlow<String> get() = _profileImageUrl

    fun getUserFromFireStore(uId: String?) {
        try {
            viewModelScope.launch {
                if(uId != null){getUserDataUseCase(uId).collect { userEntity ->
                    val userModel = userEntity?.toModel()!!
                    _getUserData.value = Result.success(userModel)}
                }
            }
        } catch (e: Exception) {
            _getUserData.value = Result.failure(e)
        }
    }

     fun getCurrentUserUId() {
            viewModelScope.launch {
                getCurrentGetUserUseCase()
                    .catch { e->
                        _getCurrentUser.value = Result.failure(e)
                    }
                    .collect {result ->
                        _getCurrentUser.value = Result.success(result)
                        Log.d("getCurrent", "${result.toString()}")
                    }
            }
    }


    fun getAllUserData(uId: String) {
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

    fun updateProfile(uri: Uri){

        viewModelScope.launch {
            imageUploadUseCase(uri)

        }
    }
}

class MainPageViewModelFactory(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val getCurrentGetUserUseCase: GetCurrentUserUseCase,
    private val imageUploadUseCase: ImageUploadUseCase,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainPageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainPageViewModel(
                getUserDataUseCase,
                getAllUsersUseCase,
                getCurrentGetUserUseCase,
                imageUploadUseCase

            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

