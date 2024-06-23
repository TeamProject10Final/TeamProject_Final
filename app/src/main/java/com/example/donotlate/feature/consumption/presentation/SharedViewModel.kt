package com.example.donotlate.feature.consumption.presentation

//import kotlinx.coroutines.flow.internal.NopCollector.emit
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.donotlate.feature.consumption.domain.usecase.DeleteConsumptionUseCase
import com.example.donotlate.feature.consumption.domain.usecase.GetDataCountUseCase
import com.example.donotlate.feature.consumption.domain.usecase.InsertConsumptionUseCase
import com.example.donotlate.feature.consumption.presentation.ConsumptionActivity.Companion.addCommas
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

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

    private val _isPenalty = MutableLiveData<Boolean>(false)
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

    private val _calResult = MutableLiveData<Int>(0)
    val calResult: LiveData<Int> = _calResult


    private val _penaltyNumberString = MutableLiveData<String>()
    val penaltyNumberString: LiveData<String> = _penaltyNumberString

    private val _penalty3Status = MutableLiveData<Int>(0)
    val penalty3Status: LiveData<Int> = _penalty3Status

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

    fun get3PenaltyStatus(): Int {
        return penalty3Status.value!!
    }

    fun check3PenaltyStatus() {
        when (penalty3Status.value) {
            0 -> {
                //벌금 없는 상태
                setIsPenalty(false)
            }

            1 -> {
                //상대방의 벌금 ?
                setIsPenalty(false)
            }

            else -> {
                //내 벌금 ?
                setIsPenalty(true)
            }
        }
    }

    fun change3PenaltyStatus() {
        if (penalty3Status.value != null) {
            if (penalty3Status.value!! >= 2) {
                _penalty3Status.value = 0
            } else {
                _penalty3Status.value = penalty3Status.value?.plus(1)
            }
        }
        check3PenaltyStatus()
    }

    fun setIsPenalty(isPenalty: Boolean) {
        _isPenalty.value = isPenalty
    }

    fun setPenalty(penalty: String?) {
        _penalty.value = penalty
    }

//    fun changeIsPenalty(isPenalty: Boolean) {
//        _isPenalty.value = !isPenalty
//    }

    fun setNumber(number: String) {
        _number.value = number
    }

    fun setPrice(price: Int) {
        _price.value = price
    }

    fun setIsFinished(check: Boolean) {
        _isFinished.value = check
    }

    fun setPenaltyNumber(input: String) {
        _penaltyNumberString.value = input
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
        val message = StringBuilder()
        if (calResult.value != 0) {
            val price = _price.value!!
            val penalty = _penalty.value?.toIntOrNull() ?: 0
            val total = price + penalty

            //val message = StringBuilder()
            message.append("📌${_date.value} 의 \"${_detail.value}\" 정산\n\n")
                .append("1인 💰${price.addCommas()}\n\n")

            if (penalty != 0) {
                message.append("지각자는 💸").append(total.addCommas())
            }
        } else {
            message.append("정산 미완료!\n다시 정산해 주세요.")
        }
        return message.toString()
    }

    fun calculate() {


        val total = total.value?.takeIf { it.isNotBlank() }?.toIntOrNull() ?: 0
        val number = number.value?.takeIf { it.isNotBlank() }?.toIntOrNull() ?: 0
        val penaltyString = penalty.value
        val isPenalty = isPenalty.value
        val penaltyNumberString = penaltyNumberString.value
        Log.d("확인 penalty string", "$penaltyNumberString")

        // penalty가 빈칸이거나 null인 경우 0으로 간주하여 처리...
        val penalty = penaltyString?.takeIf { it.isNotBlank() }?.toIntOrNull() ?: 0
        val penaltyNumberInt = penaltyNumberString?.takeIf { it.isNotBlank() }?.toIntOrNull() ?: 0

        Log.d("확인 값들", "${total}, ${number}, ${penaltyString}, ${penalty}, ${penaltyNumberInt}")

        // number가 0인 경우에는 0으로 나누는 오류가 발생하므로 예외 처리하기 - 프래그먼트에 로직 추가함
        if (number == 0 || total == 0) {
            _calResult.value = 0
            _error.postValue("인원 수를 다시 확인해주세요.")
        }

        if (number < penaltyNumberInt) {
            _error.postValue("입력한 인원을 다시 확인해주세요.")
            _calResult.value = 0
        }

        if (penalty3Status.value == 0 && penaltyNumberInt != 0) {
            _error.postValue("입력한 내용을 다시 확인해주세요.")
            _calResult.value = 0
        }

        if (penaltyNumberInt != 0) {
            //벌금 대상자가 있는 경우
            if (isPenalty == true) {
                //내가 벌금 대상자인 경우
                val result = ((total - penalty * penaltyNumberInt) / number) + penalty
                _calResult.value = result
                _price.value = result
            } else {
                //내가 벌금 대상자가 아닌 경우
                val result = (total - penalty * penaltyNumberInt) / number
                _calResult.value = result
                _price.value = result
            }
        } else {
            //벌금 대상자가 없는 경우
            if (isPenalty == true) {
                //내가 벌금 대상자인 경우? 이건 잘못된건데
                _calResult.value = 0
                _price.value = 0
            } else {
                val result = (total - penalty) / number
                _calResult.value = result
                _price.value = result
            }
        }

        Log.d("확인 값들", "${calResult.value}")
    }
}

class SharedViewModelFactory(
    private val insertConsumptionUseCase: InsertConsumptionUseCase,
    private val deleteConsumptionUseCase: DeleteConsumptionUseCase,
    private val getDataCountUseCase: GetDataCountUseCase
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SharedViewModel::class.java)) {
            return SharedViewModel(
                insertConsumptionUseCase,
                deleteConsumptionUseCase,
                getDataCountUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}