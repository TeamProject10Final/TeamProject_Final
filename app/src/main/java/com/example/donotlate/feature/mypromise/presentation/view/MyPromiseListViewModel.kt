package com.example.donotlate.feature.mypromise.presentation.view

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.donotlate.core.domain.usecase.LoadToMyPromiseListUseCase
import com.example.donotlate.core.presentation.CurrentUser
import com.example.donotlate.core.util.parseTime
import com.example.donotlate.feature.mypromise.presentation.mapper.toPromiseModelList
import com.example.donotlate.feature.mypromise.presentation.model.PromiseModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MyPromiseListViewModel(
    private val loadToMyPromiseListUseCase: LoadToMyPromiseListUseCase,
) : ViewModel() {

    private var currentUId: String? = null
    private var promiseJob: Job? = null

    private val _closestPromiseTitle = MutableStateFlow<String?>(null)
    val closestPromiseTitle: StateFlow<String?> get() = _closestPromiseTitle

    private val _promiseRoomList = MutableStateFlow<List<PromiseModel>>(emptyList())
    val promiseRoomModel: StateFlow<List<PromiseModel>> get() = _promiseRoomList


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

//                         약속 날짜와 시간을 String -> LocalDate 타입으로 변경하기 위한 포맷팅
                        val now = LocalDateTime.now()
                        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

                        // 약속 시간과 날짜를 현재 시간과 날짜와 비교한 값
                        // '현재 시간과 날짜'보다 이후의 약속들을 필터링 하기 위한 코드.
                        val upcomingPromise = promiseRooms.filter { rooms ->
                            val promiseDate = LocalDate.parse(rooms.promiseDate, dateFormatter)
                            val promiseTime = parseTime(rooms.promiseTime)

                            val promiseDateTime = LocalDateTime.of(promiseDate, promiseTime)
                            promiseDateTime.isAfter(now)
                        }

                        // 필터링 된 약속 방 중에 날짜와 시간중 가장 임박한 약속 가져오기.
                        val closestPromise = upcomingPromise.minByOrNull {
                            // 파싱을 위한 코드.
                            val promiseDate = LocalDate.parse(it.promiseDate, dateFormatter)
                            val promiseTime = parseTime(it.promiseTime)
                            Log.d("시간확인", "$promiseTime")

                            LocalDateTime.of(promiseDate, promiseTime)
                        }
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

