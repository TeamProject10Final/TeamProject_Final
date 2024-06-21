package com.example.donotlate.feature.main.presentation.view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.donotlate.core.domain.usecase.GetCurrentUserDataUseCase
import com.example.donotlate.core.presentation.CurrentUser
import com.example.donotlate.feature.main.presentation.mapper.toModel
import com.example.donotlate.feature.main.presentation.model.UserModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainPageViewModel(
    private val getCurrentUserDataUseCase: GetCurrentUserDataUseCase,
) : ViewModel() {

    private val _currentUserData = MutableStateFlow<UserModel?>(null)
    val currentUserData: StateFlow<UserModel?> = _currentUserData

    private val _profileImageUrl = MutableStateFlow<String>("")
    val profileImageUrl: StateFlow<String> get() = _profileImageUrl

    private val _dakeMode = MutableLiveData<Boolean>(true)
    val dakeMode: LiveData<Boolean> = _dakeMode

    fun dakeModeChange(boolean: Boolean) {
        _dakeMode.value = boolean
    }

    fun getCurrentUserData(){
        Log.d("getCurrentUserData", "stared getCurrentUserData()")
        try {
            viewModelScope.launch {
                getCurrentUserDataUseCase().collect{userEntity->
                    val userModel = userEntity?.toModel()
                    if(userModel != null){
                        _currentUserData.value = userModel
                        Log.d("getCurrentUserData", "_currentUserData :${_currentUserData.value}")
                        CurrentUser.userData = userModel
                        Log.d("getCurrentUserData", "CurrentUser.userData :${CurrentUser.userData}")
                    }else{
                        //  TODO - 예외 처리 해야함.
                    }
                }
            }
        }catch (e: Exception){
            throw NullPointerException("오류 터짐")
        }
    }

//    fun updateProfile(uri: Uri){
//        viewModelScope.launch {
//            imageUploadUseCase(uri).collect { result ->
//                result.onSuccess { imageUrl ->
//                    _profileImageUrl.value = imageUrl
//
//                    if (currentUserId != null) {
//                        fetchUpdatedUserData(currentUserId)
//                    }
//                }.onFailure { e ->
//                    Log.e("ImageUpload", "Error uploading image :$e")
//                }
//            }
//        }
//    }
//
//    private fun fetchUpdatedUserData(uId: String) {
//        viewModelScope.launch {
//            try {
//                getUserDataUseCase(uId).collect { userEntity ->
//                    val userModel = userEntity?.toModel()
//                    if (userModel != null) {
//                       _currentUserData.value = userModel
//                    }
//                }
//            } catch (e: Exception) {
//                // TODO 예외처리 해야함
//            }
//        }
//    }

}

class MainPageViewModelFactory(
    private val getCurrentUserDataUseCase: GetCurrentUserDataUseCase,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainPageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainPageViewModel(
                getCurrentUserDataUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

