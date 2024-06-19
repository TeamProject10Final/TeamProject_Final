package com.example.donotlate.feature.mypromise.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.donotlate.core.domain.usecase.GetCurrentUserUseCase
import com.example.donotlate.core.domain.usecase.GetMyDataFromFireStoreUseCase
import com.example.donotlate.core.domain.usecase.GetUserDataUseCase
import com.example.donotlate.core.domain.usecase.LoadToMyPromiseListUseCase
import com.example.donotlate.feature.directionRoute.domain.usecase.GetDirectionsUseCase
import com.example.donotlate.feature.directionRoute.presentation.DirectionsModel
import com.example.donotlate.feature.directionRoute.presentation.toModel
import com.example.donotlate.feature.mypromise.domain.usecase.MessageReceivingUseCase
import com.example.donotlate.feature.mypromise.domain.usecase.MessageSendingUseCase
import com.example.donotlate.feature.mypromise.presentation.mapper.toMessageEntity
import com.example.donotlate.feature.mypromise.presentation.mapper.toMessageModel
import com.example.donotlate.feature.mypromise.presentation.mapper.toModel
import com.example.donotlate.feature.mypromise.presentation.mapper.toPromiseModelList
import com.example.donotlate.feature.mypromise.presentation.mapper.toViewType
import com.example.donotlate.feature.mypromise.presentation.model.MessageModel
import com.example.donotlate.feature.mypromise.presentation.model.MessageViewType
import com.example.donotlate.feature.mypromise.presentation.model.PromiseModel
import com.example.donotlate.feature.mypromise.presentation.model.UserModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MyPromiseViewModel(
    private val loadToMyPromiseListUseCase: LoadToMyPromiseListUseCase,
    private val messageSendingUseCase: MessageSendingUseCase,
    private val messageReceivingUseCase: MessageReceivingUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val getMyDataFromFireStoreUseCase: GetMyDataFromFireStoreUseCase,
    private val firebaseAuth: FirebaseAuth,
    private val getDirectionsUseCase: GetDirectionsUseCase
) : ViewModel() {

    private var currentUId: String? = null

    private val _closestPromiseTitle = MutableStateFlow<String?>(null)
    val closestPromiseTitle: StateFlow<String?> get() = _closestPromiseTitle

    private val _promiseRoomList = MutableStateFlow<List<PromiseModel>>(emptyList())
    val promiseRoomModel: StateFlow<List<PromiseModel>> get() = _promiseRoomList

    private val _message = MutableStateFlow<List<MessageViewType>>(listOf())
    val message: StateFlow<List<MessageViewType>> get() = _message

    private val _currentUserId = MutableStateFlow<String>("")
    val currentUserId: StateFlow<String> get() = _currentUserId

    private val _currentUserData = MutableStateFlow<UserModel?>(null)
    val currentUserData: StateFlow<UserModel?> get() = _currentUserData

    private val _messageSendResults = MutableStateFlow<Boolean>(false)
    val messageSendResult: StateFlow<Boolean> get() = _messageSendResults

    val mAuth = firebaseAuth.currentUser?.uid

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _userLocation = MutableLiveData<LatLng>()
    val userLocation: LiveData<LatLng> get() = _userLocation


    private val _directionsResult = MutableLiveData<DirectionsModel>()
    val directionsResult: LiveData<DirectionsModel> get() = _directionsResult
    private val _origin = MutableLiveData<String>()
    val origin: LiveData<String> get() = _origin

    private val _destination = MutableLiveData<String>()
    val destination: LiveData<String> get() = _destination

    private val _mode = MutableLiveData<String>()
    val mode: LiveData<String> get() = _mode

    private val _shortExplanations = MutableLiveData<String>()
    val shortExplanations: LiveData<String> get() = _shortExplanations

    fun setUserLocation(location: LatLng) {
        _userLocation.value = location
    }

    // 사용자 위치를 문자열로 반환하는 메서드 추가
    fun getUserLocationString(delimiter: String = ","): String? {
        val location = _userLocation.value
        return location?.let {
            "${it.latitude}$delimiter${it.longitude}"
        }
    }

    fun getDirections(origin: String, destination: String, mode: String) {
        Log.d("확인", "$origin, $destination, $mode")
        viewModelScope.launch {
            try {
                updateODM(origin, destination, mode)
                val result = getDirectionsUseCase(origin, destination, mode)
                _directionsResult.value = result.toModel()
                setShortDirectionsResult()
                Log.d("확인", "viewmodel: ${_directionsResult.value}")
            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
    }
    fun setShortDirectionsResult() {
        if (_directionsResult.value != null) {
            formatShortDirectionsExplanations(_directionsResult.value!!)
        } else {
            _error.postValue("_direction null")
            Log.d("확인 setDirections", "null")
        }
    }
    private fun formatShortDirectionsExplanations(directions: DirectionsModel) {
        val resultText = StringBuilder()

        directions.routes.forEach { route ->
            route.legs.forEach { leg ->
                resultText.append("🗺️목적지까지 ${leg.totalDistance.text},\n")
                resultText.append("앞으로 ${leg.totalDuration.text} 뒤인\n")
                resultText.append("🕐${leg.totalArrivalTime.text}에 도착 예정입니다.")
            }
        }

        _shortExplanations.value = resultText.toString()
    }

    private fun updateODM(origin: String, destination: String, mode: String) {
        _origin.value = origin
        _destination.value = destination
        _mode.value = mode
    }
    init {
        getCurrentUId()
        getCurrentUserData()
    }

    private fun getCurrentUId() {
        viewModelScope.launch {
            currentUId = _currentUserId.value
        }
    }

    private fun getCurrentUserData() {
        viewModelScope.launch {
            getMyDataFromFireStoreUseCase().collect { userData ->
                _currentUserData.value = userData?.toModel()
            }
        }
    }

    fun loadPromiseRooms(uid: String) {
        viewModelScope.launch {
            loadToMyPromiseListUseCase(uid).collect {
                val promiseRooms = it.toPromiseModelList()
                _promiseRoomList.value = promiseRooms
                Log.d("PromiseList", "${_promiseRoomList.value}")

                val closestPromise = promiseRooms.minByOrNull {  it.promiseDate }
                _closestPromiseTitle.value = closestPromise?.roomTitle
            }
        }
    }

    fun loadMessage(roomId: String) {
        viewModelScope.launch {
            messageReceivingUseCase(roomId).collect { entities ->
                val model = entities.map { it.toMessageModel() }
                Log.d("tttt", "$currentUId")
                _message.value = model.map { it.toViewType(mAuth ?: "") }

            }
        }
    }

    fun sendMessage(roomId: String, message: MessageModel) {
        try {
            viewModelScope.launch {
                Log.d("ddddddd3", "$roomId")
                messageSendingUseCase(roomId, message.toMessageEntity()).collect { result ->
                    _messageSendResults.value = result
                }
                Log.d("ddddddd4", "$roomId")
            }
        } catch (e: Exception) {
            Log.d("ddddddd8", "rror: Send To Message Error: $e")
        }

    }

    fun getCurrentUserId() {
        viewModelScope.launch {
            getCurrentUserUseCase().collect { uId ->
                _currentUserId.value = uId
            }
        }
    }
}

class MyPromiseViewModelFactory(
    private val loadToMyPromiseListUseCase: LoadToMyPromiseListUseCase,
    private val messageSendingUseCase: MessageSendingUseCase,
    private val messageReceivingUseCase: MessageReceivingUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val getMyDataFromFireStoreUseCase: GetMyDataFromFireStoreUseCase,
    private val firebaseAuth: FirebaseAuth,
    private val getDirectionsUseCase: GetDirectionsUseCase
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyPromiseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MyPromiseViewModel(
                loadToMyPromiseListUseCase,
                messageSendingUseCase,
                messageReceivingUseCase,
                getCurrentUserUseCase,
                getUserDataUseCase,
                getMyDataFromFireStoreUseCase,
                firebaseAuth,
                getDirectionsUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
