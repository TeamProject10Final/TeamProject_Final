package com.example.donotlate.feature.consumption.presentation

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.donotlate.feature.consumption.presentation.ConsumptionActivity.Companion.addCommas
import com.example.donotlate.feature.consumption.domain.repository.ConsumptionRepository
import com.example.donotlate.feature.consumption.domain.usecase.DeleteConsumptionUseCase
import com.example.donotlate.feature.consumption.domain.usecase.GetConsumptionByCategoryUseCase
import com.example.donotlate.feature.consumption.domain.usecase.GetConsumptionByIdUseCase
import com.example.donotlate.feature.consumption.domain.usecase.GetConsumptionDataUseCase
import com.example.donotlate.feature.consumption.domain.usecase.GetDataCountUseCase
import com.example.donotlate.feature.consumption.domain.usecase.GetFinishedConsumptionUseCase
import com.example.donotlate.feature.consumption.domain.usecase.GetTotalPriceUseCase
import com.example.donotlate.feature.consumption.domain.usecase.GetUnfinishedConsumptionUseCase
import com.example.donotlate.feature.consumption.domain.usecase.InsertConsumptionUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
//import kotlinx.coroutines.flow.internal.NopCollector.emit
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.time.Duration
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class SharedViewModel
constructor(

//    private val getFinishedConsumptionUseCase: GetFinishedConsumptionUseCase,
//    private val getUnfinishedConsumptionUseCase: GetUnfinishedConsumptionUseCase,
    private val insertConsumptionUseCase: InsertConsumptionUseCase,
    private val deleteConsumptionUseCase: DeleteConsumptionUseCase,
//    private val getConsumptionByCategoryUseCase: GetConsumptionByCategoryUseCase,
//    private val getConsumptionByIdUseCase: GetConsumptionByIdUseCase,
//    private val getConsumptionDataUseCase: GetConsumptionDataUseCase,
//    private val getTotalPriceUseCase: GetTotalPriceUseCase,
    private val getDataCountUseCase: GetDataCountUseCase
) : ViewModel() {

    private val _historyId = MutableLiveData<Int>()
    val historyId: LiveData<Int> = _historyId

    private val _detail = MutableLiveData<String?>()
    val detail: LiveData<String?> = _detail

    private val _date = MutableLiveData<String?>()
    val date: LiveData<String?> = _date

    private val _category = MutableLiveData<String?>()
    val category: LiveData<String?> = _category

    private val _total = MutableLiveData<String?>()
    val total: LiveData<String?> = _total

    private val _isPenalty = MutableLiveData<Boolean>()
    val isPenalty: LiveData<Boolean> = _isPenalty

    private val _penalty = MutableLiveData<String?>()
    val penalty: LiveData<String?> = _penalty

    private val _number = MutableLiveData<String?>()
    val number: LiveData<String?> = _number

    private val _price = MutableLiveData<Int>()
    val price: LiveData<Int> = _price

    private val _isFinished = MutableLiveData<Boolean>()
    val isFinished: LiveData<Boolean> = _isFinished

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _errorState = MutableSharedFlow<String>()
    val errorState = _errorState.asSharedFlow()

    private val _modelCurrent = MutableLiveData<Int>()
    val modelCurrent: LiveData<Int> = _modelCurrent


    private val _mediatorLiveData = MediatorLiveData<String>()
    val mediatorLiveData : MediatorLiveData<String> = _mediatorLiveData


    init {
        //historyId 는 database의 개수 세어서 넣기!!!!
        viewModelScope.launch {
            _historyId.value = getDataCountUseCase()
        }
        _detail.value = null
        _date.value = null
        _category.value = null
        _total.value = null
        _isPenalty.value = false
        _penalty.value = null
        _number.value = null
        _price.value = 0
        _isFinished.value = false
        setMediatorLiveData()
    }

    //mediatorLiveData에 데이터 추가
    private fun setMediatorLiveData(){
        mediatorLiveData.addSource(total) {
            mediatorLiveData.value = it
        }
        mediatorLiveData.addSource(penalty) {
            mediatorLiveData.value = it
        }
        mediatorLiveData.addSource(number) {
            mediatorLiveData.value = it
        }
    }

    fun setCurrentItem(current: Int) {
        _modelCurrent.value = current
    }

    fun setDetail(detail: String) {
        _detail.value = detail
    }

    fun setDate(date: String) {
        _date.value = date
    }

    fun setCategory(category: String) {
        _category.value = category
    }

    fun setTotal(total: String) {
        _total.value = total
    }

    fun setIsPenalty(isPenalty: Boolean) {
        _isPenalty.value = isPenalty
    }

    fun setPenalty(penalty: String?) {
        _penalty.value = penalty
    }

    fun changeIsPenalty(isPenalty: Boolean) {
        _isPenalty.value = !isPenalty
    }

    fun setNumber(number: String) {
        _number.value = number
    }

    fun setPrice(price: Int) {
        _price.value = price
    }

    fun setIsFinished(check: Boolean) {
        _isFinished.value = check
    }

    fun insertConsumption(consumption: ConsumptionModel) {
        viewModelScope.launch {
            try {
                insertConsumptionUseCase(consumption.toEntity())
            } catch (e: Exception) {
                _errorState.emit("Failed to insert consumption: ${e.message}")
            }
        }
    }

    fun deleteConsumption(consumption: ConsumptionModel) {
        viewModelScope.launch {
            try {
                deleteConsumptionUseCase(consumption.toEntity())
            } catch (e: Exception) {
                _errorState.emit("Failed to delete consumption: ${e.message}")
            }
        }
    }

    fun saveConsumption(context: Context) {
        val newConsumption = ConsumptionModel(
            _historyId.value ?: 0,
            _detail.value ?: "",
            _date.value ?: "",
            _category.value ?: "",
            _total.value ?: "",
            _isPenalty.value ?: false,
            _penalty.value ?: "",
            _number.value ?: "",
            _price.value ?: 0,
            _isFinished.value ?: false
        ).toEntity()
        viewModelScope.launch {
            try {
                insertConsumptionUseCase(newConsumption)
            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }

    }

    fun buildShareMessage(): String {
        val price = _price.value!!
        val penalty = _penalty.value?.toIntOrNull() ?: 0
        val total = price + penalty

        val message = StringBuilder()
        message.append("📌${_date.value} 의 \"${_detail.value}\" 정산\n\n")
            .append("1인 💰${price.addCommas()}\n\n")

        if (penalty != 0) {
            message.append("지각자는 💸").append(total.addCommas())
        }

        return message.toString()
    }
}

class SharedViewModelFactory(
//    private val repository: ConsumptionRepository,
//    private val getFinishedConsumptionUseCase: GetFinishedConsumptionUseCase,
//    private val getUnfinishedConsumptionUseCase: GetUnfinishedConsumptionUseCase,
    private val insertConsumptionUseCase: InsertConsumptionUseCase,
    private val deleteConsumptionUseCase: DeleteConsumptionUseCase,
//    private val getConsumptionByCategoryUseCase: GetConsumptionByCategoryUseCase,
//    private val getConsumptionByIdUseCase: GetConsumptionByIdUseCase,
//    private val getConsumptionDataUseCase: GetConsumptionDataUseCase,
//    private val getTotalPriceUseCase: GetTotalPriceUseCase,
    private val getDataCountUseCase: GetDataCountUseCase
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SharedViewModel::class.java)) {
            return SharedViewModel(
//                getFinishedConsumptionUseCase,
//                getUnfinishedConsumptionUseCase,
                insertConsumptionUseCase,
                deleteConsumptionUseCase,
//                getConsumptionByCategoryUseCase,
//                getConsumptionByIdUseCase,
//                getConsumptionDataUseCase,
//                getTotalPriceUseCase,
                getDataCountUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}