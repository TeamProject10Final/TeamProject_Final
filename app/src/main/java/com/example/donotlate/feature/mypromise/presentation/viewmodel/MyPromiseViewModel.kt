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
import com.example.donotlate.feature.directionRoute.presentation.FirstMode
import com.example.donotlate.feature.directionRoute.presentation.FirstModeEnum
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
import kotlin.math.acos
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

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

    private val _destinationString = MutableLiveData<String>()
    val destinationString: LiveData<String> get() = _destinationString

    private val _destinationLatLng = MutableLiveData<LatLng>()
    val destinationLatLng: LiveData<LatLng> get() = _destinationLatLng

    private val _distanceBetween = MutableLiveData<Double>()
    val distanceBetween: LiveData<Double> get() = _distanceBetween

    //검색 결과 중 선택한 경로
    private val _selectedRouteIndex = MutableLiveData<Int>(0)
    val selectedRouteIndex: LiveData<Int> get() = _selectedRouteIndex

    fun setSelectedRouteIndex(indexNum: Int) {
        _selectedRouteIndex.value = indexNum ?: 0
    }

    private val _directionsResult = MutableLiveData<DirectionsModel>()
    val directionsResult: LiveData<DirectionsModel> get() = _directionsResult
    private val _origin = MutableLiveData<String>()
    val origin: LiveData<String> get() = _origin

    private val _destination = MutableLiveData<String>()
    val destination: LiveData<String> get() = _destination

    //아래 수정하기
    private val _mode = MutableLiveData<String>("transit")
    val mode: LiveData<String> get() = _mode

    private val _shortExplanations = MutableLiveData<String>()
    val shortExplanations: LiveData<String> get() = _shortExplanations

    //경로 선택하기 전 보여줄 간단한 소개들
    private val _routeSelectionText = MutableLiveData<List<String>>()
    val routeSelectionText: LiveData<List<String>> get() = _routeSelectionText


    fun setDestinationLatLng() {

        val temp = _directionsResult.value?.routes?.get(0)?.legs?.get(0)?.totalEndLocation
        if (temp != null) {
            val tempLatLng = LatLng(temp.lat, temp.lng)
            Log.d("확인 목적지", "목적지 ${tempLatLng.latitude}, ${tempLatLng.longitude}")
            _destinationLatLng.value = tempLatLng
        } else {
            _error.postValue("목적지 세팅 실패")
            Log.d("확인 목적지", "목적지 null")
        }
    }

    fun setDestination(dest: String) {
        _destinationString.value = dest
    }

    private fun calDistance2() {
        //지구 반지름 (km)
        val earthRadius = 6371.0

        val userLocationVal = userLocation.value
        val destinationLatLngVal = destinationLatLng.value
        if (userLocationVal != null && destinationLatLngVal != null) {
            val latDist = Math.toRadians(userLocationVal.latitude - destinationLatLngVal.latitude)
            val lngDist =
                Math.toRadians(userLocationVal.longitude - (destinationLatLngVal.longitude))

            val a = sin(latDist / 2).pow(2.0) + cos(Math.toRadians(userLocationVal.latitude)) * cos(
                Math.toRadians(destinationLatLngVal.latitude)
            ) * sin(lngDist / 2).pow(2.0)

            val c = 2 * atan2(sqrt(a), sqrt(1 - a))
            _distanceBetween.value = earthRadius * c
        }
    }


    private fun calDistance(): Long {
        //지구 반지름 (m)
        val EARTH_R = 6371000.0
        val rad = Math.PI / 180

        val radLat1 = rad * (userLocation.value?.latitude.toString().toLong())
        val radLat2 = rad * (destinationLatLng.value?.latitude.toString().toLong())

        val radDist = rad * (userLocation.value?.longitude.toString()
            .toLong() - destinationLatLng.value?.longitude.toString().toLong())

        var distance = sin(radLat1) * sin(radLat2)
        distance += cos(radLat1) * cos(radLat2) * cos(radDist)

        val ret = EARTH_R * acos(distance)
        Log.d("확인 거리", "$ret")
        return Math.round(ret)
    }

    private fun checkDistance() {
        val currentDistance = calDistance()

        if (currentDistance <= 10) {
            // 10미터 남음
        }
    }


    fun setUserLocation(location: LatLng) {
        _userLocation.value = location
    }

    // 사용자 위치를 문자열로 반환하는 메서드 추가
    fun getUserLocationString(delimiter: String = ","): String? {
        val location = userLocation.value
        return location?.let {
            "${it.latitude}$delimiter${it.longitude}"
        }
    }

    fun getDirections() {
        viewModelScope.launch {
            try {
                val result = getDirectionsUseCase(
                    origin.value.toString(),
                    destination.value.toString(),
                    mode.value.toString()
                )
                _directionsResult.value = result.toModel()
                //아래 로그는 bottom sheet 띄운 뒤 수정 예정
                Log.d("확인 index 개수", "${_directionsResult.value!!.routes.size}")
                setRouteSelectionText()
                setDestinationLatLng()
            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
    }

    private suspend fun setRouteSelectionText() {
        if (_directionsResult.value != null) {
            Log.d("확인 setDirections", "${_directionsResult.value}")
            formatRouteSelectionText(_directionsResult.value!!)
        } else {
            _error.postValue("_direction null")
            Log.d("확인 setDirections", "null")
            _routeSelectionText.postValue(emptyList())
            //emptyOrNull
        }
    }

    private fun formatRouteSelectionText(directions: DirectionsModel) {
        val resultsList = mutableListOf<String>()


        directions.routes.size
        var routeIndex = 1
        directions.routes.forEach { route ->
            val resultText = StringBuilder()
            val resultText1 = StringBuilder()

            resultText.append("🔵경로 ${routeIndex}\n")
            route.legs.forEach { leg ->
                resultText1.append("  예상 소요 시간 : ${leg.totalDuration.text},\n")
                resultText1.append("🕐${leg.totalArrivalTime.text}에 도착 예상.\n")
                resultText1.append("\n")

                val resultText2 = StringBuilder()

                var num = 1
                leg.steps.forEach { step ->
                    resultText2.append("🔷${num}: ${step.htmlInstructions} (${step.stepDuration.text})\n")
                    num++
                }
                resultText1.append(resultText2)
            }
            resultText.append(resultText1)
            resultsList.add(resultText.toString())
            routeIndex++
        }
        Log.d("확인 리스트 인덱스", "${resultsList.size}")
        _routeSelectionText.value = resultsList
        Log.d("확인 setDirections", "stringbuilder ${resultsList}")
        Log.d("확인 setDirections 1", "${resultsList[2]}")
    }


    //index 정해진 뒤에 보낼 메시지를 만들어야 함
    fun afterSelecting() {
        viewModelScope.launch {
            setShortDirectionsResult()
        }
    }



    fun setShortDirectionsResult() {
        if (_directionsResult.value != null) {

            formatShortDirectionsExplanations(_directionsResult.value!!)
//            formatShortDirectionsExplanations(_directionsResult.value!!)
        } else {
            _error.postValue("_direction null")
            Log.d("확인 setDirections", "null")
        }
    }

    private fun formatShortDirectionsExplanations(directions: DirectionsModel) {
        //선택하면 그거에 대해 1번 출력되게
        val resultText = StringBuilder()
        //아래 코드로 수정하기
//        val temp = directions.routes[_selectedRouteIndex.value!!].legs[0]
        val temp = directions.routes[0].legs[0]
//
        resultText.append("${temp.totalStartLocation.lat}, ${temp.totalStartLocation.lng}\n")
        resultText.append("출발 주소 ${temp.totalStartAddress}\n")
//
        resultText.append("🗺️목적지까지 ${temp.totalDistance.text},\n")
        resultText.append("앞으로 ${temp.totalDuration.text} 뒤")
        if (mode.value == "transit") {
            resultText.append("인\n🕐${temp.totalArrivalTime.text}에 도착 예정입니다.\n")
        } else {
            resultText.append(" 도착 예정입니다.\n")
        }

        //마지막에 \n 제거 확인하기!!!
        resultText.append("\n\n\n")
        _shortExplanations.value = resultText.toString()

        Log.d("확인 short", "${resultText}")
    }

    //transit | driving | walking 등
    fun setMode(mode: FirstMode) {
        when (mode.type) {
            FirstModeEnum.TRANSIT -> _mode.value = mode.key
            FirstModeEnum.DRIVING -> _mode.value = mode.key
            FirstModeEnum.WALKING -> _mode.value = mode.key
            FirstModeEnum.BICYCLING -> _mode.value = mode.key
            FirstModeEnum.NOT_SELECTED -> _mode.value = mode.key
        }
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

                val closestPromise = promiseRooms.minByOrNull { it.promiseDate }
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
