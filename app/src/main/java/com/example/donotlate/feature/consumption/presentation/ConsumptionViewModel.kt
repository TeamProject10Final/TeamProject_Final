package com.example.donotlate.feature.consumption.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.donotlate.feature.consumption.domain.usecase.DeleteConsumptionUseCase
import com.example.donotlate.feature.consumption.domain.usecase.GetConsumptionByCategoryUseCase
import com.example.donotlate.feature.consumption.domain.usecase.GetConsumptionByIdUseCase
import com.example.donotlate.feature.consumption.domain.usecase.GetConsumptionDataUseCase
import com.example.donotlate.feature.consumption.domain.usecase.GetDataCountUseCase
import com.example.donotlate.feature.consumption.domain.usecase.GetFinishedConsumptionUseCase
import com.example.donotlate.feature.consumption.domain.usecase.GetLiveDataCountUseCase
import com.example.donotlate.feature.consumption.domain.usecase.GetTotalPriceUseCase
import com.example.donotlate.feature.consumption.domain.usecase.GetUnfinishedConsumptionUseCase
import com.example.donotlate.feature.consumption.domain.usecase.InsertConsumptionUseCase
import com.example.donotlate.feature.consumption.domain.usecase.ToggleIsFinishedUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
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
    private val toggleIsFinishedUseCase: ToggleIsFinishedUseCase


) : ViewModel() {

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _errorState = Channel<String>()
    val errorState = _errorState.receiveAsFlow()

    private val _finishedConsumptions = MutableStateFlow<List<ConsumptionModel>>(emptyList())
    val finishedConsumptions: StateFlow<List<ConsumptionModel>> get() = _finishedConsumptions

    private val _unfinishedConsumptions = MutableStateFlow<List<ConsumptionModel>>(emptyList())
    val unfinishedConsumptions: StateFlow<List<ConsumptionModel>> get() = _unfinishedConsumptions

    private val _totalPrice = MutableStateFlow(0L)
    val totalPrice: StateFlow<Long> get() = _totalPrice

    private val _liveDataCount = MutableStateFlow(0)
    val liveDataCount: StateFlow<Int> get() = _liveDataCount

    init {

        fetchFinishedConsumptions()
        fetchUnfinishedConsumptions()
        fetchTotalPrice()
        fetchLiveDataCount()

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
        viewModelScope.launch {
            getTotalPriceUseCase()
                .onStart {
                    // Loading state 처리
                }
                .catch { exception ->
                    _errorState.send(exception.message ?: "Unknown error")
                }
                .collect { price ->
                    _totalPrice.value = price
                }
        }
    }

    private fun fetchLiveDataCount() {
        viewModelScope.launch {
            getLiveDataCountUseCase()
                .onStart {
                    // Loading state 처리
                }
                .catch { exception ->
                    _errorState.send(exception.message ?: "Unknown error")
                }
                .collect { count ->
                    _liveDataCount.value = count
                }
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
    private val toggleIsFinishedUseCase: ToggleIsFinishedUseCase
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
                toggleIsFinishedUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
