package com.example.donotlate.feature.directionRoute.presentation

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.donotlate.feature.directionRoute.domain.usecase.GetDirWithDepTmRpUseCase
import com.example.donotlate.feature.directionRoute.domain.usecase.GetDirectionsUseCase
import com.example.finaldirectionexample01.domain.usecase.GetDirWithArrTmRpUseCase
import com.example.finaldirectionexample01.domain.usecase.GetDirWithTmRpUseCase
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

class DirectionsViewModel1(
    private val getDirectionsUseCase: GetDirectionsUseCase,
    private val getDirWithDepTmRpUseCase: GetDirWithDepTmRpUseCase,
    private val getDirWithTmRpUseCase: GetDirWithTmRpUseCase,
    private val getDirWithArrTmRpUseCase: GetDirWithArrTmRpUseCase
) : ViewModel() {
    //검색결과
    private val _directionsResult = MutableLiveData<DirectionsModel>()
    val directionsResult: LiveData<DirectionsModel> get() = _directionsResult

    //검색 결과 중 선택한 경로
    private val _selectedRouteIndex = MutableLiveData<Int>()
    val selectedRouteIndex: LiveData<Int> get() = _selectedRouteIndex

    //에러처리
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    //string 형태로 바꾸거나 문자로 검색한 출발지(사용자의 위치)
    private val _origin = MutableLiveData<String>()
    val origin: LiveData<String> get() = _origin

    //string 형태로 바꾸거나 문자로 검색한 도착지(목적지)
    private val _destination = MutableLiveData<String>()
    val destination: LiveData<String> get() = _destination

    //transit, driving, walking 등
    private val _mode = MutableLiveData<String>()
    val mode: LiveData<String> get() = _mode

    //더 선호하는 대중교통 수단
    private val _transitMode = MutableLiveData<String>()
    val transitMode: LiveData<String> get() = _transitMode

    //더 선호하는 방식 (less_walking 등)
    private val _routingPreference = MutableLiveData<String>()
    val routingPreference: LiveData<String> get() = _routingPreference

    //출/도착 시간 선택한 경우
    private val _selectedTime = MutableLiveData<LocalTime>()
    val selectedTime: LiveData<LocalTime> get() = _selectedTime

    //경로 선택하기 전 보여줄 간단한 소개들
    private val _routeSelectionText = MutableLiveData<List<String>>()
    val routeSelectionText: LiveData<List<String>> get() = _routeSelectionText

    //시간 선택 창 옆 버튼...
    private val _isDepArrNone = MutableLiveData<Int>(0)
    val isDepArrNone: LiveData<Int> get() = _isDepArrNone

    private val _polyLine = MutableLiveData<List<PolylineOptions>>()
    val polyLine: LiveData<List<PolylineOptions>> get() = _polyLine

    private val _latLngBounds = MutableLiveData<List<LatLngModel>>()
    val latLngBounds: LiveData<List<LatLngModel>> get() = _latLngBounds

    private val _userLocation = MutableLiveData<LatLng>()
    val userLocation: LiveData<LatLng> get() = _userLocation

    private val _directionExplanations = MutableLiveData<String>()
    val directionExplanations: LiveData<String> get() = _directionExplanations

    private val _shortExplanations = MutableLiveData<String>()
    val shortExplanations: LiveData<String> get() = _shortExplanations

    fun setIsDepArrNone(set: Int) {
        _isDepArrNone.value = set
    }
    fun changeIsDepArrNone() {
        if (_isDepArrNone.value!! <= 0) {
            _isDepArrNone.value = isDepArrNone.value?.plus(1)
        } else {
            _isDepArrNone.value = -1
        }
        // -1 : 출발시각 (dep)
        // 0 : 시간 조건 없이
        // 1 : 도착시각 (arr)
    }

    fun setTransitMode(tm: TransitMode) {
        when (tm.type) {
            TransitModeEnum.BUS -> _transitMode.value = tm.key
            TransitModeEnum.SUBWAY -> _transitMode.value = tm.key
            TransitModeEnum.TRAIN -> _transitMode.value = tm.key
            TransitModeEnum.TRAM -> _transitMode.value = tm.key
            TransitModeEnum.RAIL -> _transitMode.value = tm.key
            TransitModeEnum.NOT_SELECTED -> _transitMode.value = ""
        }
    }

    fun setRoutingPreference(rp: TransitRoutePreference) {
        when (rp.type) {
            TransitRoutePreferenceEnum.LESS_WALKING -> _routingPreference.value = rp.key
            TransitRoutePreferenceEnum.FEWER_TRANSFER -> _routingPreference.value = rp.key
            TransitRoutePreferenceEnum.NOT_SELECTED -> _routingPreference.value = ""
        }
//        if (rp.key == "select") {
//            _routingPreference.value = ""
//        } else {
//            _routingPreference.value = rp.message
//        }
    }

    fun setSelectedRouteIndex(indexNum: Int) {
        _selectedRouteIndex.value = (indexNum - 1) ?: 0
    }

    fun setTime(hour: Int, minute: Int) {
        _selectedTime.value = LocalTime.of(hour, minute)
    }

    fun getUnixTimestamp(selectedTime: LocalTime): Long? {
        val currentDate = LocalDateTime.now().toLocalDate()
        val currentTime = LocalTime.now()
        var dateTime = LocalDateTime.of(currentDate, selectedTime ?: return null)

        if (dateTime.toLocalTime().isBefore(currentTime)) {
            dateTime = dateTime.plusDays(1)
        }

        val zonedDateTime = ZonedDateTime.of(dateTime, ZoneId.systemDefault())
        return zonedDateTime.toEpochSecond()
    }

    fun setDestination(destination: String) {
        _destination.value = destination
    }

    //세부사항 없이 transit | driving | walking | bicycling
    fun getDirections() {
        viewModelScope.launch {
            try {
                val result = getDirectionsUseCase(
                    origin.value.toString(),
                    destination.value.toString(),
                    mode.value.toString()
                )
                yield()
                _directionsResult.value = result.toModel()
                //아래 로그는 bottom sheet 띄운 뒤 수정 예정
                Log.d("확인 index 개수", "${_directionsResult.value!!.routes.size}")
                updateBounds()
                yield()
                setRouteSelectionText()
            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
    }

    //transit의 경우 시간 설정 유무에 따라 3가지로 나뉘니까
    fun getDirByTransit() {
        when (isDepArrNone.value) {
            -1 -> {
                //departure 설정
                getDirWithDep()
            }

            0 -> {
                //시간 설정 없음
                getDirWithTmRp()
            }

            else -> {
                //arrival 설정
                getDirWithArr()
            }
        }
    }

    //시간 없이 && 대중교통
    fun getDirWithTmRp() {
        Log.d("확인 transitMode", "${transitMode.value}")
        Log.d("확인 preference", "${routingPreference.value}")
        viewModelScope.launch {
            try {
                val result = getDirWithTmRpUseCase(
                    origin.value.toString(),
                    destination.value.toString(),
                    transitMode.value.toString(),
                    routingPreference.value.toString()
                )
                yield()
                _directionsResult.value = result.toModel()
                Log.d("확인 index 개수", "${_directionsResult.value!!.routes.size}")
                updateBounds()
                yield()
                Log.d("확인", "viewmodel 2: ${_directionsResult.value}")
                setRouteSelectionText()
            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
    }

    // + 출발시간 && 대중교통
    fun getDirWithDep() {
        viewModelScope.launch {
            try {
                val result = getDirWithDepTmRpUseCase(
                    origin.value.toString(),
                    destination.value.toString(),
                    getUnixTimestamp(selectedTime.value!!).toString().toInt(),
                    transitMode.value.toString(),
                    routingPreference.value.toString()
                )
                yield()
                _directionsResult.value = result.toModel()
                updateBounds()
                yield()
                Log.d("확인", "viewmodel 2: ${_directionsResult.value}")
                setRouteSelectionText()
            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
    }

    //도착시간 && 대중교통
    fun getDirWithArr() {
        viewModelScope.launch {
            try {
                val result = getDirWithArrTmRpUseCase(
                    origin.value.toString(),
                    destination.value.toString(),
                    getUnixTimestamp(selectedTime.value!!).toString().toInt(),
                    transitMode.value.toString(),
                    routingPreference.value.toString()
                )
                yield()
                _directionsResult.value = result.toModel()
                updateBounds()
                yield()
                Log.d("확인", "viewmodel 2: ${_directionsResult.value}")
                setRouteSelectionText()
            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
    }

    //index 정해진 뒤에 polyline 등을 구해야 함
    fun afterSelecting() {
        updatePolyLineWithColors()
        updateBounds()
        setShortDirectionsResult()
        setDirectionsResult()
    }

    private fun updateBounds() {
        _latLngBounds.postValue(
            _directionsResult.value?.routes?.get(0)?.bounds?.let {
                listOf(
                    it.northeast,
                    it.southwest
                )
            },
        )
    }

    //사용자 위치 업데이트... LatLng 과 String 모두 설정
    fun setUserLocation(location: LatLng) {
        _userLocation.value = location
        _origin.value = getUserLocationString()!!
    }

    private fun updatePolyLineWithColors() {
        try {
            val routes = _directionsResult.value?.routes
            val polylines = mutableListOf<PolylineOptions>()

            routes?.get(_selectedRouteIndex.value!!)?.legs?.forEach { leg ->
                leg.steps.forEach { step ->
                    val decodedPoints = PolyUtil.decode(step.polyline.points ?: "")
                    val color = hexToColorInt(step.transitDetails.line.color)

                    val coloredLine = PolylineOptions()
                        .addAll(decodedPoints)
                        .width(30f)
                        .color(color)

                    polylines.add(coloredLine)
                }
            }
            _polyLine.postValue(polylines)

        } catch (e: Exception) {
            _error.postValue(e.message)
        }

    }

    //polyline에 색 넣기 위해 변환
    private fun hexToColorInt(hexColor: String): Int {
        Log.d("확인", "컬러 int ${hexColor.removePrefix("#")}")
        return try {
            Color.parseColor("#${hexColor.removePrefix("#")}")
        } catch (e: java.lang.IllegalArgumentException) {
            Color.GRAY
        }
    }

    fun getOrigin(): LatLng {
        val lat1 =
            _directionsResult.value?.routes?.get(_selectedRouteIndex.value!!)?.legs?.get(0)?.totalStartLocation?.lat
                ?: 0.0
        val lng1 =
            _directionsResult.value?.routes?.get(_selectedRouteIndex.value!!)?.legs?.get(0)?.totalStartLocation?.lng
                ?: 0.0
        return LatLng(lat1, lng1)

    }

    fun getDestination(): LatLng {
        val lat1 =
            _directionsResult.value?.routes?.get(_selectedRouteIndex.value!!)?.legs?.get(0)?.totalEndLocation?.lat
                ?: 0.0
        val lng1 =
            _directionsResult.value?.routes?.get(_selectedRouteIndex.value!!)?.legs?.get(0)?.totalEndLocation?.lng
                ?: 0.0
        return LatLng(lat1, lng1)
    }

    // 사용자 위치를 문자열로 반환하는 메서드 추가
    fun getUserLocationString(delimiter: String = ","): String? {
        val location = _userLocation.value
        return location?.let {
            "${it.latitude}$delimiter${it.longitude}"
        }
    }

    //transit | driving | walking 등
    fun setMode(mode: String) {
        _mode.value = mode
    }

    // directionsResult를 설정하는 메서드
    fun setDirectionsResult() {
        if (_directionsResult.value != null) {
            formatDirectionsExplanations(_directionsResult.value!!)
        } else {
            _error.postValue("_direction null")
            Log.d("확인 setDirections", "null")
        }
    }

    // directionsResult를 기반으로 directionExplanations을 설정하는 메서드
    private fun formatDirectionsExplanations(directions: DirectionsModel) {
        val resultText = StringBuilder()

        directions.routes.get(_selectedRouteIndex.value!!).legs.forEach { leg ->
            resultText.append("🗺️목적지까지 ${leg.totalDistance.text},\n")
            resultText.append("앞으로 ${leg.totalDuration.text} 뒤인\n")
            resultText.append("🕐${leg.totalArrivalTime.text}에 도착 예정입니다.\n")
            resultText.append("\n")
            var num = 1
            leg.steps.forEach { step ->
                resultText.append("🔷${num}:\n")
                resultText.append("*  상세설명: ${step.htmlInstructions}\n")
                resultText.append("*  소요시간: ${step.stepDuration.text}\n")
                resultText.append("*  구간거리: ${step.distance.text}\n")
                resultText.append("*  이동수단: ${step.travelMode}")

                if (step.transitDetails != DirectionsTransitDetailsModel(
                        DirectionsTransitStopModel(LatLngModel(0.0, 0.0), ""),
                        TimeZoneTextValueObjectModel("", "", 0.0),
                        DirectionsTransitStopModel(LatLngModel(0.0, 0.0), ""),
                        TimeZoneTextValueObjectModel("", "", 0.0),
                        (""),
                        0,
                        DirectionsTransitLineModel(
                            emptyList(),
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            DirectionsTransitVehicleModel("", "", "", "")
                        ),
                        0,
                        ""
                    )
                ) {
                    resultText.append(" : ${step.transitDetails.line.shortName}, ${step.transitDetails.line.name}\n")
                    resultText.append("|    ${step.transitDetails.headSign} 행\n")
                    resultText.append("|    탑승 장소: ${step.transitDetails.departureStop.name}\n")
                    resultText.append("|    하차 장소: ${step.transitDetails.arrivalStop.name}\n")
                    resultText.append("|    ${step.transitDetails.numStops}")
                    resultText.append(categorizeTransportation(step.transitDetails.line.vehicle.type))
                    resultText.append("\n\n")
                } else {
                    resultText.append("\n\n\n")
                }

                num++
            }
        }
        _directionExplanations.value = resultText.toString()
    }

    // 교통 수단을 카테고라이즈하는 메서드
    private fun categorizeTransportation(transportationType: String): String {
        return when (transportationType) {
            "BUS" -> {
                "개 정류장 이동🚍\n"
            }

            "CABLE_CAR" -> {
                " 케이블 카 이용🚟\n"
            }

            "COMMUTER_TRAIN" -> {
                "개 역 이동🚞\n"
            }

            "FERRY" -> {
                " 페리 이용⛴️\n"
            }

            "FUNICULAR" -> {
                " 푸니큘러 이용🚋\n"
            }

            "GONDOLA_LIFT" -> {
                " 곤돌라 리프트 이용🚠\n"
            }

            "HEAVY_RAIL" -> {
                "개 역 이동🛤️\n"
            }

            "HIGH_SPEED_TRAIN" -> {
                "개 역 이동🚄\n"
            }

            "INTERCITY_BUS" -> {
                "개 정류장 이동🚌\n"
            }

            "LONG_DISTANCE_TRAIN" -> {
                "개 역 이동🚂\n"
            }

            "METRO_RAIL" -> {
                "개 역 이동🚇\n"
            }

            "MONORAIL" -> {
                "개 역 이동🚝\n"
            }

            "OTHER" -> {
                " 이동\n"
            }

            "RAIL" -> {
                "개 역 이동🚃\n"
            }

            "SHARE_TAXI" -> {
                " 공유 택시 이용🚖\n"
            }

            "SUBWAY" -> {
                "개 역 이동🚉\n"
            }

            "TRAM" -> {
                "개 역 트램으로 이동🚊\n"
            }

            "TROLLEYBUS" -> {
                "개 정류장 트롤리버스로 이동🚎\n"
            }

            else -> {
                " 이동\n"
            }
        }
    }

    //
    private fun setRouteSelectionText() {
        if (_directionsResult.value != null) {
            formatRouteSelectionText(_directionsResult.value!!)
        } else {
            _error.postValue("_direction null")
            Log.d("확인 setDirections", "null")
        }
    }

    private fun formatRouteSelectionText(directions: DirectionsModel) {
        val resultsList = mutableListOf<String>()

        directions.routes.size
        var routeIndex = 1
        directions.routes.forEach { route ->
            val resultText = StringBuilder()
            resultText.append("🔷경로 ${routeIndex}\n")
            route.legs.forEach { leg ->
                resultText.append("예상 소요 시간 : ${leg.totalDuration.text},\n")
                resultText.append("🕐${leg.totalArrivalTime.text}에 도착 예상.\n")
                resultText.append("\n")
                var num = 1
                leg.steps.forEach { step ->
                    resultText.append("🔷${num}:\n")
                    resultText.append("*  상세설명: ${step.htmlInstructions}\n")
                    resultText.append("*  소요시간: ${step.stepDuration.text}\n")
                    resultText.append("*  이동수단: ${step.travelMode}")

                    if (step.transitDetails != DirectionsTransitDetailsModel(
                            DirectionsTransitStopModel(LatLngModel(0.0, 0.0), ""),
                            TimeZoneTextValueObjectModel("", "", 0.0),
                            DirectionsTransitStopModel(LatLngModel(0.0, 0.0), ""),
                            TimeZoneTextValueObjectModel("", "", 0.0),
                            (""),
                            0,
                            DirectionsTransitLineModel(
                                emptyList(),
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                DirectionsTransitVehicleModel("", "", "", "")
                            ),
                            0,
                            ""
                        )
                    ) {
                        resultText.append(" - ${step.transitDetails.line.shortName}, ${step.transitDetails.line.name}\n")
                        resultText.append("\n\n")
                    } else {
                        resultText.append("\n\n\n")
                    }
                    resultsList.add(resultText.toString())
                    num++
                }
            }
            routeIndex++
        }
        _routeSelectionText.value = resultsList
    }


    //채팅방에서 위치 공유하는 텍스트
    private fun setShortDirectionsResult() {
        if (_directionsResult.value != null) {
            formatShortDirectionsExplanations(_directionsResult.value!!)
        } else {
            _error.postValue("_direction null")
            Log.d("확인 setDirections", "null")
        }
    }

    private fun formatShortDirectionsExplanations(directions: DirectionsModel) {
        val resultText = StringBuilder()

        directions.routes.get(_selectedRouteIndex.value!!).legs.forEach { leg ->
            resultText.append("🗺️목적지까지 ${leg.totalDistance.text},\n")
            resultText.append("앞으로 ${leg.totalDuration.text} 뒤인\n")
            resultText.append("🕐${leg.totalArrivalTime.text}에 도착 예정입니다.\n")
            resultText.append("\n")
        }
        _shortExplanations.value = resultText.toString()
    }

}

class DirectionsViewModel1Factory(
    private val getDirectionsUseCase: GetDirectionsUseCase,
    private val getDirWithDepTmRpUseCase: GetDirWithDepTmRpUseCase,
    private val getDirWithTmRpUseCase: GetDirWithTmRpUseCase,
    private val getDirWithArrTmRpUseCase: GetDirWithArrTmRpUseCase
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DirectionsViewModel1::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DirectionsViewModel1(
                getDirectionsUseCase,
                getDirWithDepTmRpUseCase,
                getDirWithTmRpUseCase,
                getDirWithArrTmRpUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

