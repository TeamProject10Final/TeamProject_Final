package com.nomorelateness.donotlate.feature.consumption.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nomorelateness.donotlate.core.domain.usecase.GetCurrentUserDataUseCase
import com.nomorelateness.donotlate.feature.consumption.domain.usecase.DeleteConsumptionUseCase
import com.nomorelateness.donotlate.feature.consumption.domain.usecase.GetConsumptionByCategoryUseCase
import com.nomorelateness.donotlate.feature.consumption.domain.usecase.GetConsumptionByIdUseCase
import com.nomorelateness.donotlate.feature.consumption.domain.usecase.GetConsumptionDataUseCase
import com.nomorelateness.donotlate.feature.consumption.domain.usecase.GetDataCountUseCase
import com.nomorelateness.donotlate.feature.consumption.domain.usecase.GetFinishedConsumptionUseCase
import com.nomorelateness.donotlate.feature.consumption.domain.usecase.GetLiveDataCountUseCase
import com.nomorelateness.donotlate.feature.consumption.domain.usecase.GetTotalPriceUseCase
import com.nomorelateness.donotlate.feature.consumption.domain.usecase.GetUnfinishedConsumptionUseCase
import com.nomorelateness.donotlate.feature.consumption.domain.usecase.InsertConsumptionUseCase
import com.nomorelateness.donotlate.feature.consumption.domain.usecase.ToggleIsFinishedUseCase
import com.nomorelateness.donotlate.feature.main.presentation.mapper.toModel
import com.nomorelateness.donotlate.feature.main.presentation.model.UserModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ConsumptionViewModel(
    private val getFinishedConsumptionUseCase: GetFinishedConsumptionUseCase,
    private val getUnfinishedConsumptionUseCase: GetUnfinishedConsumptionUseCase,
    private val insertConsumptionUseCase: InsertConsumptionUseCase,
    private val deleteConsumptionUseCase: DeleteConsumptionUseCase,
    private val getConsumptionByCategoryUseCase: GetConsumptionByCategoryUseCase,
    private val getConsumptionByIdUseCase: GetConsumptionByIdUseCase,
    private val getConsumptionDataUseCase: GetConsumptionDataUseCase,
    private val getTotalPriceUseCase: GetTotalPriceUseCase,
    private val getDataCountUseCase: GetDataCountUseCase,
    private val getLiveDataCountUseCase: GetLiveDataCountUseCase,
    private val toggleIsFinishedUseCase: ToggleIsFinishedUseCase,
    private val getCurrentUserDataUseCase: GetCurrentUserDataUseCase


) : ViewModel() {

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _errorState = Channel<String>()
    val errorState = _errorState.receiveAsFlow()

    private val _finishedConsumptions = MutableStateFlow<List<ConsumptionModel>>(emptyList())
    val finishedConsumptions: StateFlow<List<ConsumptionModel>> get() = _finishedConsumptions

    private val _unfinishedConsumptions = MutableStateFlow<List<ConsumptionModel>>(emptyList())
    val unfinishedConsumptions: StateFlow<List<ConsumptionModel>> get() = _unfinishedConsumptions

    private val _totalPrice = MutableStateFlow<Long>(0L)
    val totalPrice: StateFlow<Long> get() = _totalPrice

    private val _liveDataCount = MutableStateFlow(0)
    val liveDataCount: StateFlow<Int> get() = _liveDataCount

    // 수정 하기
    private val _currentUserData = MutableStateFlow<UserModel?>(null)
    val currentUserData: StateFlow<UserModel?> get() = _currentUserData

    init {

        fetchFinishedConsumptions()
        fetchUnfinishedConsumptions()
        fetchLiveDataCount()
        fetchTotalPrice()
        getCurrentUserData()

    }

    private fun getCurrentUserData() {
        viewModelScope.launch {
            getCurrentUserDataUseCase().collect { userData ->
                _currentUserData.value =
                    userData?.toModel() ?: throw NullPointerException("user data null")
            }
        }
    }

    private fun fetchFinishedConsumptions() {
        viewModelScope.launch {
            getFinishedConsumptionUseCase().collect { entities ->
                _finishedConsumptions.value = entities.map { it.toModel() }
            }
        }
    }

    private fun fetchUnfinishedConsumptions() {
        viewModelScope.launch {
            getUnfinishedConsumptionUseCase().collect { entities ->
                _unfinishedConsumptions.value = entities.map { it.toModel() }
            }
        }
    }

    private fun fetchTotalPrice() {

        getTotalPriceUseCase()
            .catch {
                //위에 있는 usecase에 관해 catch
            }

            .onStart {
                // Loading state 처리
            }.onCompletion {
                // 끝났을 때 (로딩 끝내기) - 오류가 나와도 실행됨
            }.onEach { price ->
                _totalPrice.value = price
                Log.d("확인 longerror Notnull", "${price}")
                //이 위에 있는
            }.catch {
                _totalPrice.value = 0L
            }.launchIn(viewModelScope)


    }

    private fun fetchLiveDataCount() {
        viewModelScope.launch {
            getLiveDataCountUseCase()
                .onStart {
                    // Loading state 처리
                }
                .catch { exception ->
                    Log.d("확인 error2", "${exception.message}")
                    _errorState.send(exception.message ?: "Unknown error")
                }
                .collect { count ->
                    _liveDataCount.value = count
                }//collect 안에서 오류 발생 시 위처럼 변경하기
        }
    }

    fun toggleIsFinished(consumption: ConsumptionModel) {
        viewModelScope.launch {
            try {
                toggleIsFinishedUseCase(consumption.toEntity())
            } catch (e: Exception) {
                _error.value = e.message ?: "An unexpected error occurred"
            }
        }
    }

    fun deleteConsumption(consumption: ConsumptionModel) {
        viewModelScope.launch {
            try {
                Log.d("확인 delete 성공", "${consumption.toEntity()}")
                deleteConsumptionUseCase(consumption.toEntity())
            } catch (e: Exception) {
                _error.postValue("Failed to delete consumption: ${e.message}")
                Log.d("확인 delete", "${e.message}")
            }
        }
    }

//나중에 category 등 처리 여기서 하기!!!!


}

class ConsumptionViewModelFactory(
    private val getFinishedConsumptionUseCase: GetFinishedConsumptionUseCase,
    private val getUnfinishedConsumptionUseCase: GetUnfinishedConsumptionUseCase,
    private val insertConsumptionUseCase: InsertConsumptionUseCase,
    private val deleteConsumptionUseCase: DeleteConsumptionUseCase,
    private val getConsumptionByCategoryUseCase: GetConsumptionByCategoryUseCase,
    private val getConsumptionByIdUseCase: GetConsumptionByIdUseCase,
    private val getConsumptionDataUseCase: GetConsumptionDataUseCase,
    private val getTotalPriceUseCase: GetTotalPriceUseCase,
    private val getDataCountUseCase: GetDataCountUseCase,
    private val getLiveDataCountUseCase: GetLiveDataCountUseCase,
    private val toggleIsFinishedUseCase: ToggleIsFinishedUseCase,
    private val getCurrentUserDataUseCase: GetCurrentUserDataUseCase
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConsumptionViewModel::class.java)) {
            return ConsumptionViewModel(
                getFinishedConsumptionUseCase,
                getUnfinishedConsumptionUseCase,
                insertConsumptionUseCase,
                deleteConsumptionUseCase,
                getConsumptionByCategoryUseCase,
                getConsumptionByIdUseCase,
                getConsumptionDataUseCase,
                getTotalPriceUseCase,
                getDataCountUseCase,
                getLiveDataCountUseCase,
                toggleIsFinishedUseCase,
                getCurrentUserDataUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
