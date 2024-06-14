package com.example.donotlate.feature.mypromise.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.donotlate.core.domain.usecase.LoadToMyPromiseListUseCase
import com.example.donotlate.feature.mypromise.presentation.mapper.toPromiseModelList
import com.example.donotlate.feature.mypromise.presentation.model.PromiseModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MyPromiseViewModel(
    private val loadToMyPromiseListUseCase: LoadToMyPromiseListUseCase
) : ViewModel() {


    private val _promiseRoomList = MutableStateFlow<List<PromiseModel>>(emptyList())
    val promiseRoomModel: StateFlow<List<PromiseModel>> get() = _promiseRoomList


    fun loadPromiseRooms() {
        viewModelScope.launch {
            loadToMyPromiseListUseCase().collect {
                val promiseRooms = it.toPromiseModelList()
                _promiseRoomList.value = promiseRooms
                Log.d("PromiseList", "${_promiseRoomList.value}")
            }
        }
    }
}

class MyPromiseViewModelFactory(private val loadToMyPromiseListUseCase: LoadToMyPromiseListUseCase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyPromiseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MyPromiseViewModel(
                loadToMyPromiseListUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
