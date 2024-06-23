package com.example.donotlate.feature.mypromise.presentation.view

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.donotlate.core.domain.usecase.LoadToMyPromiseListUseCase
import com.example.donotlate.core.presentation.CurrentUser
import com.example.donotlate.feature.mypromise.presentation.mapper.toPromiseModelList
import com.example.donotlate.feature.mypromise.presentation.model.PromiseModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MyPromiseListViewModel(
    private val loadToMyPromiseListUseCase: LoadToMyPromiseListUseCase,
) : ViewModel() {

    private var currentUId: String? = null
    private var promiseJob: Job? = null

    private val _closestPromiseTitle = MutableStateFlow<String?>(null)
    val closestPromiseTitle: StateFlow<String?> get() = _closestPromiseTitle

    private val _promiseRoomList = MutableStateFlow<List<PromiseModel>>(emptyList())
    val promiseRoomModel: StateFlow<List<PromiseModel>> get() = _promiseRoomList


    fun formatUnixTimeStamp() {

    }

    fun loadPromiseRooms() {
        val uid = CurrentUser.userData?.uId
        if (currentUId != uid) {
            currentUId = uid
            _promiseRoomList.value = emptyList()
            _closestPromiseTitle.value = null

            if (uid != null) {
                promiseJob?.cancel()

                promiseJob = viewModelScope.launch {
                    loadToMyPromiseListUseCase(uid).collect {
                        val promiseRooms = it.toPromiseModelList()
                        _promiseRoomList.value = promiseRooms
                        Log.d("PromiseList", "${_promiseRoomList.value}")

                        val closestPromise = promiseRooms.minByOrNull { it.promiseDate }
                        _closestPromiseTitle.value = closestPromise?.roomTitle
                    }
                }
            }
        }
    }
}

class MyPromiseListViewModelFactory(
    private val loadToMyPromiseListUseCase: LoadToMyPromiseListUseCase,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyPromiseListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MyPromiseListViewModel(
                loadToMyPromiseListUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}