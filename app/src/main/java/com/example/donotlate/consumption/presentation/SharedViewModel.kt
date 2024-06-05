package com.example.donotlate.consumption.presentation

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.donotlate.consumption.presentation.ConsumptionActivity.Companion.addCommas
import com.example.donotlate.consumption.domain.repository.ConsumptionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.internal.NopCollector.emit
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.Duration
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class SharedViewModel(
    private val repository: ConsumptionRepository,
//    private val getFinishedConsumptionUseCase: GetFinishedConsumptionUseCase,
//    private val getUnfinishedConsumptionUseCase: GetUnfinishedConsumptionUseCase,
//    private val insertConsumptionUseCase: InsertConsumptionUseCase
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

    // 데이터베이스에서 데이터의 총 가격을 실시간으로 관찰
    val totalPrice: LiveData<Long> = repository.getTotalPrice().asLiveData()

    // 데이터베이스에서 데이터의 개수를 실시간으로 관찰
    val dataCount: LiveData<Int> = repository.getLiveDataCount().asLiveData()


    val recentFinishedConsumption: LiveData<List<ConsumptionModel>> =
        repository.getRecentFinishedConsumption().asLiveData().map { entities ->
            entities.map { mapper.toModel(it) }
        }

    val recentUnfinishedConsumption: LiveData<List<ConsumptionModel>> =
        repository.getRecentUnfinishedConsumption().asLiveData().map { entities ->
            entities.map { mapper.toModel(it) }
        }


    private val mapper = ConsumptionMapper()

    // nullable을 어디까지 넣어줘야 하지...??? 리스트 안에만 넣어주면 되나...? 아닌데...

    private val _finishedConsumption: MutableLiveData<List<ConsumptionModel>> = MutableLiveData()
    val finishedConsumption: MutableLiveData<List<ConsumptionModel>> get() = _finishedConsumption


//    private val _finishedConsumption: MutableLiveData<List<ConsumptionModel>> = MutableLiveData()
//    val finishedConsumption: MutableLiveData<List<ConsumptionModel>> get() = _finishedConsumption

    private val _unfinishedConsumption: MutableLiveData<List<ConsumptionModel>> = MutableLiveData()
    val unfinishedConsumption: MutableLiveData<List<ConsumptionModel>> get() = _unfinishedConsumption


    private fun fetchFinishedConsumption1(){
        val finishedEntities = repository.getRecentFinishedConsumption()


    }
    private fun fetchFinishedConsumption() {
        viewModelScope.launch {
            try {
                val finishedEntities = repository.getRecentFinishedConsumption().asLiveData()
                finishedEntities.observeForever { entities ->
                    val models = entities?.map { mapper.toModel(it) }
                    _finishedConsumption.value = models ?: emptyList()
                }
            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
    }

    private fun fetchUnfinishedConsumption() {
        viewModelScope.launch {
            try {
                val unfinishedEntities = repository.getRecentUnfinishedConsumption().asLiveData()
                unfinishedEntities.observeForever { entities ->
                    val models = entities?.map { mapper.toModel(it) }
                    _unfinishedConsumption.value = models ?: emptyList()
                }
            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
    }
//    private fun fetchFinishedConsumption() {
//        viewModelScope.launch {
//            try {
//                val finishedEntities = repository.getFinishedConsumption()
//                finishedEntities.observeForever { entities ->
//                    val models = entities?.map { mapper.toModel(it) }
//                    _finishedConsumption.value = models
//                }
//            } catch (e: Exception) {
//                _error.postValue(e.message)
//            }
//        }
//    }
//
//    private fun fetchUnfinishedConsumption() {
//        viewModelScope.launch {
//            try {
//                val unfinishedEntities = repository.getUnfinishedConsumption()
//                unfinishedEntities.observeForever { entities ->
//                    val models = entities?.map { mapper.toModel(it) }
//                    _unfinishedConsumption.value = models
//                }
//            } catch (e: Exception) {
//                _error.postValue(e.message)
//            }
//        }
//    }

    //위 코드에서 observeForever를 해제해야한다고...? viewModel이 죽을 때 사라지는 게 아님??

    init {
        // ViewModel이 초기화될 때 null로 초기화


        //historyId 는 database의 개수 세어서 넣기!!!!
        // 나머지들 null값 아닌거 확실하면 null 빼기... penalty 뺴고
        viewModelScope.launch {
            _historyId.value = repository.getDataCount()
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

        fetchFinishedConsumption()
        fetchUnfinishedConsumption()
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
            _price.value?: 0,
            _isFinished.value ?: false
        ).toEntity()
        viewModelScope.launch {
            try {
                repository.insertConsumptionData(newConsumption)
            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
        //이거 위에 _price는 왜 앞에 안붙지....?? 잘못썼나??

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

//class SharedViewModelFactory(
//    private val repository: ConsumptionRepository,
//    private val getFinishedConsumptionUseCase: GetFinishedConsumptionUseCase,
//    private val getUnfinishedConsumptionUseCase: GetUnfinishedConsumptionUseCase,
//    private val insertConsumptionUseCase: InsertConsumptionUseCase
//) :
//    ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(SharedViewModel::class.java)) {
//            return SharedViewModel(
//                repository,
//                getFinishedConsumptionUseCase,
//                getUnfinishedConsumptionUseCase,
//                insertConsumptionUseCase
//            ) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}

class SharedViewModelFactory(
    private val repository: ConsumptionRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SharedViewModel::class.java)) {
            return SharedViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
