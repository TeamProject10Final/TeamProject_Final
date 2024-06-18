package com.example.donotlate.feature.setting.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class SettingViewModel:ViewModel() {
    private val _getAllUserData = MutableLiveData<Boolean>()
    val getAllUserData: LiveData<Boolean> = _getAllUserData


    fun dakeModeChange(boolean: Boolean) {
        _getAllUserData.value = boolean
    }
}

class RoomViewModelFactory(
) : ViewModelProvider.Factory {

}