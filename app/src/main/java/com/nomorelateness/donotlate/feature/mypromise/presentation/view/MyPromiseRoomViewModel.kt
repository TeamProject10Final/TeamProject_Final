package com.nomorelateness.donotlate.feature.mypromise.presentation.view

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp
import com.nomorelateness.donotlate.core.domain.usecase.GetCurrentUserDataUseCase
import com.nomorelateness.donotlate.core.domain.usecase.promiseusecase.RemoveParticipantsUseCase
import com.nomorelateness.donotlate.core.presentation.CurrentUser
import com.nomorelateness.donotlate.core.util.parseTime
import com.nomorelateness.donotlate.feature.directionRoute.domain.usecase.GetDirectionsUseCase
import com.nomorelateness.donotlate.feature.directionRoute.presentation.DirectionsModel
import com.nomorelateness.donotlate.feature.directionRoute.presentation.LocationUtils
import com.nomorelateness.donotlate.feature.directionRoute.presentation.toModel
import com.nomorelateness.donotlate.feature.main.presentation.mapper.toModel
import com.nomorelateness.donotlate.feature.mypromise.domain.usecase.MessageReceivingUseCase
import com.nomorelateness.donotlate.feature.mypromise.domain.usecase.MessageSendingUseCase
import com.nomorelateness.donotlate.feature.mypromise.domain.usecase.UpdateArrivalStatusUseCase
import com.nomorelateness.donotlate.feature.mypromise.domain.usecase.UpdateDepartureStatusUseCase
import com.nomorelateness.donotlate.feature.mypromise.presentation.mapper.toMessageEntity
import com.nomorelateness.donotlate.feature.mypromise.presentation.mapper.toMessageModel
import com.nomorelateness.donotlate.feature.mypromise.presentation.mapper.toViewType
import com.nomorelateness.donotlate.feature.mypromise.presentation.model.MessageModel
import com.nomorelateness.donotlate.feature.mypromise.presentation.model.MessageViewType
import com.nomorelateness.donotlate.feature.mypromise.presentation.model.PromiseModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class MyPromiseRoomViewModel(
    private val messageSendingUseCase: MessageSendingUseCase,
    private val messageReceivingUseCase: MessageReceivingUseCase,
    private val getDirectionsUseCase: GetDirectionsUseCase,
    private val removeParticipantsUseCase: RemoveParticipantsUseCase,
    private val updateArrivalStatusUseCase: UpdateArrivalStatusUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val updateDepartureStatusUseCase: UpdateDepartureStatusUseCase,
    private val getCurrentUserDataUseCase: GetCurrentUserDataUseCase,
) : ViewModel() {
    private var checkArrivalStatusJob: Job? = null
    private var foundCountry: String? = null
    private var destCountry: String? = null
    private var currentRoomId: String? = null
    private var isDeparted = false
    private var lastLocation: LatLng? = null
    private var userLocationLatLng: LatLng? = null
    private var destinationLatLng: LatLng? = null
    private var destinationString: String = ""
    private var directionsResult: DirectionsModel? = null

    private val locationUtils by lazy { LocationUtils() }

    //경로 선택하기 전 보여줄 간단한 소개들
    private var routeSelectionText: List<String> = emptyList()

    private var _promiseRoom = MutableStateFlow<PromiseModel?>(null)
    val promiseRoom: StateFlow<PromiseModel?> = _promiseRoom

    private val _isArrived = Channel<Boolean>()
    val isArrived = _isArrived.receiveAsFlow()

    private val _message = MutableStateFlow<List<MessageViewType>>(listOf())
    val message: StateFlow<List<MessageViewType>> get() = _message

    private val _messageSendResults = MutableStateFlow(false)
    val messageSendResult: StateFlow<Boolean> get() = _messageSendResults

    private val _error = Channel<String>()
    val error = _error.receiveAsFlow()

    private val _myPromiseRoomEvent = MutableSharedFlow<MyPromiseRoomEvent>()
    val myPromiseRoomEvent: SharedFlow<MyPromiseRoomEvent> = _myPromiseRoomEvent

    private val _originString = MutableLiveData<String>()
    val originString: LiveData<String> get() = _originString

    private val _distanceStatus = MutableStateFlow<DistanceState>(value = DistanceState.Nothing)
    val distanceStatus: StateFlow<DistanceState> = _distanceStatus

    private val _distanceDouble = MutableLiveData<Double>(0.0)
    val distanceDouble: LiveData<Double> get() = _distanceDouble

    //임시
    private val _removeParticipantIdResult = MutableStateFlow<Boolean?>(null)
    val removeParticipantIdResult: StateFlow<Boolean?> get() = _removeParticipantIdResult

    //수정하기 TODO
    private val _mode = MutableLiveData("transit")
    val mode: LiveData<String> get() = _mode

    private val _shortExplanations = MutableLiveData<String>()
    val shortExplanations: LiveData<String> get() = _shortExplanations


    //검색 결과 중 선택한 경로
    private val _selectedRouteIndex = MutableLiveData(0)
    val selectedRouteIndex: LiveData<Int> get() = _selectedRouteIndex

    private val _hasArrived = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val hasArrived: StateFlow<Map<String, Boolean>> get() = _hasArrived

    private val _widgetArrived = MutableLiveData<Pair<String, Boolean>>()
    val widgetArrived: LiveData<Pair<String, Boolean>> get() = _widgetArrived

    private val _lateUsers = MutableStateFlow<List<String>>(value = emptyList())
    val lateUsers: StateFlow<List<String>> get() = _lateUsers

    private val _departureStatus = MutableLiveData<Boolean>(false)
    val departureStatus: LiveData<Boolean> get() = _departureStatus

    private val _hasDeparture = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val hasDeparture: StateFlow<Map<String, Boolean>> get() = _hasDeparture

    private val _widgetDeparture = MutableLiveData<Pair<String, Boolean>>()
    val widgetDeparture: LiveData<Pair<String, Boolean>> get() = _widgetDeparture

    private val _isDeparture = Channel<Boolean>()
    val isDeparture = _isDeparture.receiveAsFlow()

    private val widgetFlow = savedStateHandle.getStateFlow("isWidget", false)

    private var isThisWidget: Boolean = false

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    init {
        viewModelScope.launch {
            launch {
                widgetFlow.collect {
                    Log.d("확인 widgetflow", "${it}")
                    isThisWidget = true
                }
            }
            savedStateHandle.get<PromiseModel>("promiseRoom")?.let {
                _promiseRoom.value = it
                if (CurrentUser.userData == null) {
                    getCurrentUserData()
                    Log.d("확인 null userdata", "${CurrentUser.userData}")
                }
//                    null
//                } else {
                checkIsUserHasArrived()
                startCheckingArrivalStatus()
                setInitialDepartureStatus() // 출발 상태 설정
                setInitialArrivalStatus()
                setDestinationLatLng()
                loadMessage()


            } ?: run {
                sendWrongAccessMessage("다시 시도해 주세요. 1")
            }
        }
    }

    private suspend fun sendWrongAccessMessage(message: String = "잘못된 접근입니다.") {
        _error.send(element = message)
    }

    private suspend fun checkIsUserHasArrived() {
        //        if (CurrentUser.userData?.uId == null) {
//            getCurrentUserData()
//        }
        Log.d("확인 userdata1", "${CurrentUser.userData?.uId}")
        if (this.promiseRoom.value?.hasArrived?.get(CurrentUser.userData?.uId) == true) {
            _isArrived.send(element = true)
//        } else {
//           // sendWrongAccessMessage("3rd")
        }
    }

    fun setLastLocation(location: Location?) {
        location?.let {
            val userLatLng = LatLng(it.latitude, it.longitude)
            lastLocation = userLatLng
            setUserLocation(location = it)
//            getDirections()
        }
    }

    private fun setUserLocation(location: Location) {
        val userLatLng = LatLng(location.latitude, location.longitude)
        userLocationLatLng = userLatLng
        _originString.value = getLocationString(userLatLng)
        calculateDistance()
    }

    fun setDestCountry(destCountry: String?) {
        this.destCountry = destCountry
    }

    fun setFoundCountry(foundCountry: String?) {
        this.foundCountry = foundCountry
    }

    fun showDialogSelectionAction() {
        viewModelScope.launch {
            _myPromiseRoomEvent.emit(
                value = MyPromiseRoomEvent.ShowDialogSelection(
                    routeSelections = routeSelectionText
                )
            )
        }
    }

    fun checkCountryAndGetRouteSelection() {
        viewModelScope.launch {
            if (!checkTwoCountry()) {
                sendWrongAccessMessage("출발 국가와 도착 국가가 일치하지 않습니다.")
                return@launch
            }
            Log.d("확인 foundCountry", "${this@MyPromiseRoomViewModel.foundCountry}")
            when (this@MyPromiseRoomViewModel.foundCountry) {
                SOUTH_KOREA_KR, SOUTH_KOREA_EN -> {
                    getDirections()
//                    showDialogSelectionAction()
                }

                null -> {
                    sendWrongAccessMessage(message = "다시 시도해주세요.")
                    return@launch
                }

                else -> {
                    _myPromiseRoomEvent.emit(value = MyPromiseRoomEvent.ShowModeDialog)
                }
            }
        }
    }

    private fun refreshIndex() {
        _selectedRouteIndex.value = 0
    }

    fun setSelectedRouteIndex(indexNum: Int) {
        _selectedRouteIndex.value = indexNum ?: 0
        Log.d("123123123123", "$indexNum")
    }

    // LatLng 위치를 문자열로 반환하는 메서드 추가
    private fun getLocationString(latLng: LatLng, delimiter: String = ","): String {
        Log.d("확인 userLocaString", "${originString.value}")
        return "${latLng.latitude}$delimiter${latLng.longitude}"
    }

    fun setIsDepart(status: Boolean) {
        isDeparted = status
    }

    private fun calculateDistance() {
        //지구 반지름 (km)
        val earthRadius = 6371.0

        val userLocationVal = userLocationLatLng
        val destinationLatLngVal = destinationLatLng
        if (userLocationVal != null && destinationLatLngVal != null) {
            val latDist = Math.toRadians(userLocationVal.latitude - destinationLatLngVal.latitude)
            val lngDist =
                Math.toRadians(userLocationVal.longitude - (destinationLatLngVal.longitude))

            val a = sin(latDist / 2).pow(2.0) + cos(Math.toRadians(userLocationVal.latitude)) * cos(
                Math.toRadians(destinationLatLngVal.latitude)
            ) * sin(lngDist / 2).pow(2.0)

            val c = 2 * atan2(sqrt(a), sqrt(1 - a))
            val destinationBetween = earthRadius * c
            Log.d("확인 거리", "$destinationBetween")
            calculateIsIn200Meters(distance = destinationBetween)
            updateRemainingDistance(destinationBetween)
        }
    }

    fun updateRemainingDistance(distance: Double) {
        _distanceDouble.value = distance
    }

    private fun calculateIsIn200Meters(distance: Double) {
        //        if (CurrentUser.userData?.uId == null) {
//            getCurrentUserData()
//        }
        Log.d("확인 userdata2", "${CurrentUser.userData?.uId}")
        val uid = CurrentUser.userData?.uId
        if (distance <= 0.2 && hasArrived.value[uid] != true) { // 200m
            _distanceStatus.value = DistanceState.In200Meters
        } else if (hasArrived.value[uid] == true) {
            _distanceStatus.value = DistanceState.Arrived
        } else {
            if (isDeparted) {
                _distanceStatus.value = DistanceState.Departed
            } else {
                _distanceStatus.value = DistanceState.NotDeparted
            }
        }
    }

    fun getButtonText(): String {
        return if ((_distanceDouble.value ?: Double.MAX_VALUE) <= 20.0) {
            "도착"
        } else {
            "위치공유"
        }
    }

    private fun setDestinationLatLng() {
        val lat = this.promiseRoom.value?.destinationLat ?: return
        val lng = this.promiseRoom.value!!.destinationLng
        destinationLatLng = LatLng(lat, lng)
        Log.d("확인 목적지", "목적지 ${lat}, ${lng}")
        if (destinationLatLng != null) {
            destinationString = getLocationString(latLng = destinationLatLng!!)
        } else {
            Log.d("확인 setDestLoca", "null")
        }
    }

    fun getDestinationLatLng(): LatLng? {
        destinationLatLng?.let {
            return it
        }
        return null
    }

    fun checkTwoCountry(): Boolean {
        return destCountry == foundCountry
    }

    fun getDirections() {
        viewModelScope.launch {
            try {
                val result = getDirectionsUseCase(
                    origin = originString.value.toString(),
                    destination = destinationString,
                    mode = mode.value.toString()
                )
                directionsResult = result.toModel()
                setRouteSelectionText()
                showDialogSelectionAction()
            } catch (e: Exception) {
                sendWrongAccessMessage(message = e.message ?: "Unknown Error")
            }
        }
    }


    fun setMode(key: String) {
        Log.d("확인 setmode", key)
        _mode.value = key
    }

    //index 정해진 뒤에 text를 만들어야 함
    fun afterSelecting() {
        setShortDirectionsResult()
    }

//    suspend fun getCurrentUserData() {
//        Log.d("getCurrentUserData", "stared getCurrentUserData()")
//        try {
//            viewModelScope.launch {
//                getCurrentUserDataUseCase().collect { userEntity ->
//                    val userModel = userEntity?.toModel()
//                    if (userModel != null) {
//                        CurrentUser.userData = userModel
//                        yield()
//                        Log.d("getCurrentUserData", "CurrentUser.userData :${CurrentUser.userData}")
//                    } else {
//                        //  TODO - 예외 처리 해야함.
//                    }
//                }
//            }
//        } catch (e: Exception) {
//            throw NullPointerException("오류 터짐")
//        }
//    }

    suspend fun getCurrentUserData() {
        Log.d("getCurrentUserData", "stared getCurrentUserData()")
        try {
            getCurrentUserDataUseCase().collect { userEntity ->
                val userModel = userEntity?.toModel()
                if (userModel != null) {
                    CurrentUser.userData = userModel
                    Log.d("getCurrentUserData", "CurrentUser.userData :${CurrentUser.userData}")
                } else {
                    // TODO - 예외 처리 해야함.
                }
            }
        } catch (e: Exception) {
            throw NullPointerException("오류 터짐")
        }
    }
    private fun setShortDirectionsResult() {
        viewModelScope.launch {
            if (directionsResult != null) {
                formatShortDirectionsExplanations(directions = directionsResult!!)
            } else {
                sendWrongAccessMessage(message = "_direction null")
                Log.d("확인 setDirections", "null")
            }
        }
    }

    private fun formatShortDirectionsExplanations(directions: DirectionsModel) {
        //선택하면 그거에 대해 1번 출력되게
        val resultText = StringBuilder()
        //아래 코드로 수정하기
        Log.d("확인 selected 인덱스", "${selectedRouteIndex.value}")
        val temp = directions.routes[selectedRouteIndex.value!!].legs[0]

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
        if (directionsResult != null) {
            Log.d("확인 setDirections", "$directionsResult")
            formatRouteSelectionText(directionsResult!!)
        } else {
            sendWrongAccessMessage(message = "_direction null")
            Log.d("확인 setDirections", "null")
            routeSelectionText = emptyList()
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

                val resultText2 = StringBuilder()

                var num = 1
                leg.steps.forEach { step ->
                    resultText2.append(" ✦${num}:")
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
        routeSelectionText = resultsList
        Log.d("확인 setDirections", "stringbuilder $resultsList")
        //Log.d("확인 setDirections 1", resultsList[2])
    }

    private suspend fun loadMessage() {
        val roomId = promiseRoom.value?.roomId ?: return
        //        if (CurrentUser.userData?.uId == null) {
//            getCurrentUserData()
//        }
        Log.d("확인 userdata3", "${CurrentUser.userData?.uId}")
        val mAuth = CurrentUser.userData?.uId
        if (currentRoomId != roomId) {
            currentRoomId = roomId
            _message.value = emptyList()
            messageReceivingUseCase(roomId).collect { entities ->
                val model = entities.map { it.toMessageModel() }
                if (mAuth != null) {
                    _message.value = model.map { it.toViewType(mAuth) }
                }
            }
        }
    }

    fun sendMessage(contents: String) {
//        if (CurrentUser.userData?.uId == null) {
//            getCurrentUserData()
//        }
        Log.d("확인 userdata4", "${CurrentUser.userData?.uId}")
        viewModelScope.launch {
            val currentUser = CurrentUser.userData ?: throw NullPointerException("User Data Null!")
            val message = MessageModel(
                senderName = currentUser.name,
                sendTimestamp = Timestamp.now(),
                senderId = currentUser.uId,
                contents = contents,
                messageId = "",
                senderProfileUrl = currentUser.profileImgUrl
            )

            val currentRoomId = this@MyPromiseRoomViewModel.currentRoomId
                ?: throw NullPointerException("Room Id is null.")
            messageSendingUseCase(
                roomId = currentRoomId,
                message.toMessageEntity()
            ).onEach { result ->
                _messageSendResults.value = result
            }.catch {
                sendWrongAccessMessage("다시 시도해 주세요.")
            }.launchIn(scope = viewModelScope)
        }
    }

    //임시
    fun exitRoom() {
        viewModelScope.launch {
            val roomId = currentRoomId ?: return@launch sendWrongAccessMessage()
            val uid = CurrentUser.userData?.uId ?: return@launch sendWrongAccessMessage()
            removeParticipantsUseCase(roomId = roomId, participantId = uid).onEach {
                _removeParticipantIdResult.value = it
            }.catch {
                sendWrongAccessMessage("다시 시도해 주세요.3")
            }.collect()
        }
    }

    fun updateArrived() {

        viewModelScope.launch {
            val roomId = currentRoomId ?: return@launch sendWrongAccessMessage()
            if (CurrentUser.userData?.uId == null) {
                getCurrentUserData()
            }
            val uid = CurrentUser.userData?.uId ?: return@launch sendWrongAccessMessage()

            updateArrivalStatusUseCase(roomId = roomId, uid = uid).onEach { success ->
                val currentArrivals = _hasArrived.value.toMutableMap()
                currentArrivals[uid] = true
                _hasArrived.value = currentArrivals.toMap()
                Log.d("확인 도착버튼 has", "${hasArrived.value}")

                //
                if (isThisWidget) {
                    val pairs = Pair<String, Boolean>(uid, true)
                    _widgetArrived.value = pairs
                    Log.d("확인 pairs", "${pairs.first}")
                    Log.d("확인 pairs", "${pairs.second}")
                    Log.d("확인 도착버튼 widget", "${widgetArrived.value}")
                }
            }.catch {
                sendWrongAccessMessage("다시 시도해 주세요.4")
            }.collect()
        }

    }

    private fun setInitialArrivalStatus() {
        //        if (CurrentUser.userData?.uId == null) {
//            getCurrentUserData()
//        }
        Log.d("확인 userdata5", "${CurrentUser.userData?.uId}")
        val uid = CurrentUser.userData?.uId

        val arrivalStatus = this.promiseRoom.value?.hasArrived ?: return
        _hasArrived.value = arrivalStatus

        if (uid != null) {
            if (arrivalStatus[uid] == true) {
                _distanceStatus.value = DistanceState.Arrived
                Log.d("확인 도착2", "${_distanceStatus.value}")
            }
        }


        Log.d("확인 도착1", "$arrivalStatus")
    }

    private fun checkArrivalStatus(room: PromiseModel) {
        val now = LocalDateTime.now()

        val promiseTime = parseTime(room.promiseTime)// 시간 형식 변환
        val promiseDateTime =
            LocalDateTime.parse("${room.promiseDate} $promiseTime", formatter)

        if (promiseDateTime.isBefore(now) || promiseDateTime.isEqual(now)) {
            val notArrivedUserIds = room.hasArrived.filter { !it.value }.keys.toList()
            val notArrivedUsers = notArrivedUserIds.map { room.participantsNames[it] ?: "Unknown" }

            _lateUsers.value = notArrivedUsers
        } else {
            _lateUsers.value = emptyList()
        }
    }


    private fun startCheckingArrivalStatus() {
        if (checkArrivalStatusJob != null)
            checkArrivalStatusJob?.cancelChildren()
        checkArrivalStatusJob = viewModelScope.launch {
            while (true) {
                checkArrivalStatus(room = promiseRoom.value ?: return@launch)
                delay(5_000)
            }
        }
    }

    fun stopCheckingArrivalStatus() {
        checkArrivalStatusJob?.cancelChildren()
    }

    override fun onCleared() {
        super.onCleared()
        checkArrivalStatusJob?.cancelChildren()
    }

    companion object {
        private const val SOUTH_KOREA_KR = "대한민국"
        private const val SOUTH_KOREA_EN = "South Korea"
    }

    fun updateDeparture() {
        viewModelScope.launch {
            val roomId = _promiseRoom.value?.roomId ?: return@launch
            if (CurrentUser.userData?.uId == null) {
                getCurrentUserData()
            }
            val uid = CurrentUser.userData?.uId ?: return@launch
            updateDepartureStatusUseCase(roomId, uid).collect { success ->
                if (success) {
                    val currentDeparture = _hasDeparture.value.toMutableMap()
                    currentDeparture[uid] = true
                    Log.d("확인 current departure", "${currentDeparture}")
                    _hasDeparture.value = currentDeparture.toMap()
                    Log.d("MyPromiseRoomViewModel", "Updated Departure Status: true")
                    Log.d("확인 출발버튼 has", "${hasDeparture.value}")

                    Log.d("확인 isthiswidget", "${isThisWidget}")
                    if (isThisWidget) {
                        // TODO 여기 수정하기 shared
                        val pairs = Pair<String, Boolean>(uid, true)
                        _widgetDeparture.value = pairs
                        Log.d("확인 pairs", "${pairs.first}")
                        Log.d("확인 pairs", "${pairs.second}")
                        Log.d("확인 출발버튼 widget", "${widgetDeparture.value}")
                    }
                    _isDeparture.send(element = true)

                } else {
                    Log.d("MyPromiseRoomViewModel", "Failed to update departure status")
                }
            }
        }
    }

    private fun setInitialDepartureStatus() {
        //        if (CurrentUser.userData?.uId == null) {
//            getCurrentUserData()
//        }
        Log.d("확인 userdata6", "${CurrentUser.userData?.uId}")
        val uid = CurrentUser.userData?.uId
        Log.d("확인 uid", "$uid")
        val departureStatus = _promiseRoom.value?.hasDeparture?.get(uid) ?: false
        Log.d("확인 pref room", "${promiseRoom.value?.hasDeparture?.get(uid)}")
        Log.d("MyPromiseRoomViewModel2 1", "Initial Departure Status: ${departureStatus}")

        isDeparted = departureStatus
        Log.d("MyPromiseRoomViewModel2 2", "Initial Departure Status: ${_departureStatus.value}")
    }

}

sealed interface DistanceState {
    data object In200Meters : DistanceState
    data object Departed : DistanceState
    data object NotDeparted : DistanceState
    data object Nothing : DistanceState
    data object Arrived : DistanceState
}

sealed interface MyPromiseRoomEvent {
    data class ShowDialogSelection(val routeSelections: List<String>) : MyPromiseRoomEvent
    data object ShowModeDialog : MyPromiseRoomEvent
}

class MyPromiseRoomViewModelFactory(
    private val messageSendingUseCase: MessageSendingUseCase,
    private val messageReceivingUseCase: MessageReceivingUseCase,
    private val getDirectionsUseCase: GetDirectionsUseCase,
    private val removeParticipantsUseCase: RemoveParticipantsUseCase,
    private val updateArrivalStatusUseCase: UpdateArrivalStatusUseCase,
//    private val savedStateHandle: SavedStateHandle = SavedStateHandle(),
    private val updateDepartureStatusUseCase: UpdateDepartureStatusUseCase,
    private val getCurrentUserDataUseCase: GetCurrentUserDataUseCase
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(MyPromiseRoomViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MyPromiseRoomViewModel(
                messageSendingUseCase,
                messageReceivingUseCase,
                getDirectionsUseCase,
                removeParticipantsUseCase,
                updateArrivalStatusUseCase,
                extras.createSavedStateHandle(),
//                savedStateHandle,
                updateDepartureStatusUseCase,
                getCurrentUserDataUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
