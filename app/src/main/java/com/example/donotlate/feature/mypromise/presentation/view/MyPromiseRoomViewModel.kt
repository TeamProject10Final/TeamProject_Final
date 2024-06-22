package com.example.donotlate.feature.mypromise.presentation.view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.donotlate.core.presentation.CurrentUser
import com.example.donotlate.feature.directionRoute.domain.usecase.GetDirectionsUseCase
import com.example.donotlate.feature.directionRoute.presentation.DirectionsModel
import com.example.donotlate.feature.directionRoute.presentation.toModel
import com.example.donotlate.feature.mypromise.domain.usecase.MessageReceivingUseCase
import com.example.donotlate.feature.mypromise.domain.usecase.MessageSendingUseCase
import com.example.donotlate.feature.mypromise.presentation.mapper.toMessageEntity
import com.example.donotlate.feature.mypromise.presentation.mapper.toMessageModel
import com.example.donotlate.feature.mypromise.presentation.mapper.toViewType
import com.example.donotlate.feature.mypromise.presentation.model.MessageModel
import com.example.donotlate.feature.mypromise.presentation.model.MessageViewType
import com.example.donotlate.feature.mypromise.presentation.model.PromiseModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class MyPromiseRoomViewModel(
    private val messageSendingUseCase: MessageSendingUseCase,
    private val messageReceivingUseCase: MessageReceivingUseCase,
    private val getDirectionsUseCase: GetDirectionsUseCase
) : ViewModel() {

    private var currentRoomId: String? = null

    private val _closestPromiseTitle = MutableStateFlow<String?>(null)
    val closestPromiseTitle: StateFlow<String?> get() = _closestPromiseTitle

    private val _promiseRoomList = MutableStateFlow<List<PromiseModel>>(emptyList())
    val promiseRoomModel: StateFlow<List<PromiseModel>> get() = _promiseRoomList

    private val _message = MutableStateFlow<List<MessageViewType>>(listOf())
    val message: StateFlow<List<MessageViewType>> get() = _message

    private val _messageSendResults = MutableStateFlow<Boolean>(false)
    val messageSendResult: StateFlow<Boolean> get() = _messageSendResults

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _originString = MutableLiveData<String>()
    val originString: LiveData<String> get() = _originString

    private val _userLocationLatLng = MutableLiveData<LatLng>()
    val userLocationLatLng: LiveData<LatLng> get() = _userLocationLatLng

    private val _destinationLatLng = MutableLiveData<LatLng>()
    val destinationLatLng: LiveData<LatLng> get() = _destinationLatLng

    private val _destinationString = MutableLiveData<String>()
    val destinationString: LiveData<String> get() = _destinationString

    private val _distanceBetween = MutableLiveData<Double>()
    val distanceBetween: LiveData<Double> get() = _distanceBetween

    private val _directionsResult = MutableLiveData<DirectionsModel>()
    val directionsResult: LiveData<DirectionsModel> get() = _directionsResult

    //수정하기 TODO
    private val _mode = MutableLiveData<String>("transit")
    val mode: LiveData<String> get() = _mode

    private val _shortExplanations = MutableLiveData<String>()
    val shortExplanations: LiveData<String> get() = _shortExplanations

    private val _isDepart = MutableLiveData<Boolean>(false)
    val isDepart: LiveData<Boolean> get() = _isDepart

    //경로 선택하기 전 보여줄 간단한 소개들
    private val _routeSelectionText = MutableLiveData<List<String>>()
    val routeSelectionText: LiveData<List<String>> get() = _routeSelectionText

    //검색 결과 중 선택한 경로
    private val _selectedRouteIndex = MutableLiveData<Int>(0)
    val selectedRouteIndex: LiveData<Int> get() = _selectedRouteIndex

    private val _country = MutableLiveData<String>()
    val country: LiveData<String> = _country

    fun getCountry(): String? {
        if (country.value != null) {
            return country.value!!
        } else {
            _error.postValue("다시 시도해 주세요.")
            return null
        }
    }

    fun setCountry(country: String) {
        _country.value = country
    }

    fun refreshIndex() {
        _selectedRouteIndex.value = 0
    }

    fun setSelectedRouteIndex(indexNum: Int) {
        _selectedRouteIndex.value = indexNum ?: 0
        Log.d("123123", "${indexNum}")
    }



    fun setUserLocation(location: LatLng) {
        _userLocationLatLng.value = location
        if (userLocationLatLng.value != null) {
            _originString.value = getLocationString(userLocationLatLng.value!!)
        } else {
            Log.d("확인 setUserLoca", "null")
        }
    }

    // LatLng 위치를 문자열로 반환하는 메서드 추가
    private fun getLocationString(latLng: LatLng, delimiter: String = ","): String {
        Log.d("확인 userLocaString", "${originString.value}")
        return "${latLng.latitude}$delimiter${latLng.longitude}"
    }

    fun setIsDepart(status: Boolean) {
        _isDepart.value = status
    }

    fun getIsDepart(): Boolean {
        return isDepart.value!!
    }

    fun calDistance2() {
        //지구 반지름 (km)
        val earthRadius = 6371.0

        val userLocationVal = userLocationLatLng.value
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
            Log.d("확인 거리", "${distanceBetween.value}")
        }
    }

    fun setDestinationLatLng(lat: Double, lng: Double) {
        _destinationLatLng.value = LatLng(lat, lng)
        Log.d("확인 목적지", "목적지 ${lat}, ${lng}")
        if (destinationLatLng.value != null) {
            _destinationString.value = getLocationString(destinationLatLng.value!!)
        } else {
            Log.d("확인 setDestLoca", "null")
        }
    }

    fun getDirections() {
        viewModelScope.launch {
            try {
                val result = getDirectionsUseCase(
                    originString.value.toString(),
                    destinationString.value.toString(),
                    mode.value.toString()
                )
                _directionsResult.value = result.toModel()
                setRouteSelectionText()
            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
    }
//
//    //TODO
//    fun setMode(position: Int) {
//        when (position) {
//            0 -> _mode.value = mode.key
//            FirstModeEnum.DRIVING -> _mode.value = mode.key
//            FirstModeEnum.WALKING -> _mode.value = mode.key
//            FirstModeEnum.BICYCLING -> _mode.value = mode.key
//            FirstModeEnum.NOT_SELECTED -> _mode.value = mode.key
//        }
//    }

    fun setMode(key: String) {
        _mode.value = key
    }

    fun getMode(): String {
        return mode.value!!
        //위에 디폴트값 넣어둠
    }


    //index 정해진 뒤에 text를 만들어야 함
    fun afterSelecting() {
        viewModelScope.launch {
            setShortDirectionsResult()
        }
    }

    fun getSelectionList(): List<String> {
        return if (routeSelectionText.value?.isEmpty() == true) {
            emptyList()
        } else {
            routeSelectionText.value!!
            //Log.d("확인 getSelecL", "${routeSelectionText.value}, ${routeSelectionText.value.toString().toList()}")
        }
    }
    fun setShortDirectionsResult() {
        if (directionsResult.value != null) {
            formatShortDirectionsExplanations(directionsResult.value!!)
        } else {
            _error.postValue("_direction null")
            Log.d("확인 setDirections", "null")
        }
    }

    private fun formatShortDirectionsExplanations(directions: DirectionsModel) {
        //선택하면 그거에 대해 1번 출력되게
        val resultText = StringBuilder()
        //아래 코드로 수정하기
        val temp = directions.routes[_selectedRouteIndex.value!!].legs[0]
//        val temp = directions.routes[0].legs[0]
//TODO 아래 코드 삭제하기
        resultText.append("${temp.totalStartLocation.lat}, ${temp.totalStartLocation.lng}\n")
        resultText.append("출발 주소 ${temp.totalStartAddress}\n")
        resultText.append("이 부분 확인 후 주소 출력 부분 삭제하기@@@@@\n")
//
        resultText.append("🗺️목적지까지 ${temp.totalDistance.text},\n")
        resultText.append("앞으로 ${temp.totalDuration.text} 뒤")
        if (mode.value == "transit") {
            resultText.append("인\n🕐${temp.totalArrivalTime.text}에 도착 예정입니다.")
        } else {
            resultText.append(" 도착 예정입니다.")
        }
        _shortExplanations.value = resultText.toString()

        Log.d("확인 short", "${resultText}")
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
        refreshIndex()

        directions.routes.size
        var routeIndex = 1
        directions.routes.forEach { route ->
            val resultText = StringBuilder()
            val resultText1 = StringBuilder()

            resultText.append("🔵경로 ${routeIndex}\n")
            route.legs.forEach { leg ->
//                resultText1.append("  예상 소요 시간 : ${leg.totalDuration.text}")
//                if (mode.value == "transit") {
//                    resultText.append("\n🕐${leg.totalArrivalTime.text}에 도착 예정입니다.\n")
//                } else {
//                    resultText.append("\n")
//                }
//                resultText1.append("\n")

                val resultText2 = StringBuilder()

                var num = 1
                leg.steps.forEach { step ->
                    resultText2.append(" ✴︎${num}:")
                    if (step.travelMode == "TRANSIT") {
                        if (step.transitDetails.line.shortName != "") {
                            resultText2.append(" [${step.transitDetails.line.shortName}]")
                        } else if (step.transitDetails.line.name != "") {
                            resultText2.append(" [${step.transitDetails.line.name}]")
                        } else {
                            //
                        }
                    }
                    Log.d("확인 travelMode", step.travelMode.toString())

                    resultText2.append(" ${step.htmlInstructions} (${step.stepDuration.text})\n")
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

    fun loadMessage(roomId: String) {
        val mAuth = CurrentUser.userData?.uId
        if (currentRoomId != roomId) {
            currentRoomId = roomId
            _message.value = emptyList()

            viewModelScope.launch {
                messageReceivingUseCase(roomId).collect { entities ->
                    val model = entities.map { it.toMessageModel() }
                    if (mAuth != null) {
                        _message.value = model.map { it.toViewType(mAuth) }
                    }
                }
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

    fun clearMessage() {
        _message.value = emptyList()
    }
}

class MyPromiseRoomViewModelFactory(
    private val messageSendingUseCase: MessageSendingUseCase,
    private val messageReceivingUseCase: MessageReceivingUseCase,
    private val getDirectionsUseCase: GetDirectionsUseCase
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyPromiseRoomViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MyPromiseRoomViewModel(
                messageSendingUseCase,
                messageReceivingUseCase,
                getDirectionsUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
