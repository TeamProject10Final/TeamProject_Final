package com.example.donotlate.feature.room.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.donotlate.core.domain.usecase.GetCurrentUserUseCase
import com.example.donotlate.core.domain.usecase.GetFriendsListFromFirebaseUseCase
import com.example.donotlate.core.domain.usecase.LoadToCurrentUserDataUseCase
import com.example.donotlate.feature.room.domain.usecase.GetAllUsersUseCase
import com.example.donotlate.feature.room.domain.usecase.MakeAPromiseRoomUseCase
import com.example.donotlate.feature.room.presentation.mapper.toRoomUserModel
import com.example.donotlate.feature.room.presentation.model.RoomModel
import com.example.donotlate.feature.room.presentation.model.RoomUserModel
import com.example.donotlate.feature.searchPlace.api.NetWorkClient
import com.example.donotlate.feature.searchPlace.domain.usecase.GetSearchListUseCase
import com.example.donotlate.feature.searchPlace.presentation.data.PlaceModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RoomViewModel(
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val getSearchListUseCase: GetSearchListUseCase,
    private val makeAPromiseRoomUseCase: MakeAPromiseRoomUseCase,
    private val loadToCurrentUserDataUseCase: LoadToCurrentUserDataUseCase,
    private val getFriendsListFromFirebaseUseCase: GetFriendsListFromFirebaseUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
) : ViewModel() {

    //    private val _getAllUserData = MutableStateFlow<List<UserModel>>(listOf())
//    val getAllUserData: StateFlow<List<UserModel>> = _getAllUserData
    private val _getAllUserData = MutableStateFlow<List<RoomUserModel>>(emptyList())
    val getAllUserData: StateFlow<List<RoomUserModel>> = _getAllUserData

    private val _makeARoomResult = MutableStateFlow<Boolean>(false)
    val makeARoomResult: StateFlow<Boolean> get() = _makeARoomResult

    private val _selectedUserUIds = MutableLiveData<List<String>>(emptyList())
    val selectedUserUIds: LiveData<List<String>> get() = _selectedUserUIds


    private val _searchMapList = MutableLiveData<List<PlaceModel>>()
    val searchMapList: LiveData<List<PlaceModel>> = _searchMapList

    private val _selectedUserNames = MutableLiveData<List<String>>()
    val selectedUserNames: LiveData<List<String>> get() = _selectedUserNames

    private val _getCurrentUserData = MutableStateFlow<RoomUserModel?>(null)
    val getCurrentUserData: StateFlow<RoomUserModel?> = _getCurrentUserData

    private val _friendsList = MutableStateFlow<List<RoomUserModel>>(listOf())
    val friendsList: StateFlow<List<RoomUserModel>> get() = _friendsList

    private val _getCurrentUser = MutableStateFlow<String?>("")
    val getCurrentUser: StateFlow<String?> get() = _getCurrentUser

    fun updateSelectedUserNames(userNames: List<String>) {
        _selectedUserNames.value = userNames
    }

    init {
        loadToCurrentUseData()
    }

    //검색 쿼리
    private var inputQuery: MutableLiveData<String> = MutableLiveData()
    fun getQuery(): LiveData<String> = inputQuery
    fun updateQuery(input: String) {
        inputQuery.value = input
    }

    //텍스트 정보
    private val _inputText = MutableLiveData<RoomModel>()
    val inputText: LiveData<RoomModel> get() = _inputText
    fun updateText(input: RoomModel) {
        _inputText.value = input
        Log.d("data55", "${_inputText.value}")
    }

    //넘겨준 위치 정보
    private val _locationData = MutableLiveData<PlaceModel>()
    val locationData: LiveData<PlaceModel> get() = _locationData
    fun setMapData(location: PlaceModel) {
        _locationData.value = location
        Log.d("data55", "${_locationData.value}")
    }

    fun getAllUserData() {
        viewModelScope.launch {
            try {
                getAllUsersUseCase().collect { userEntity ->
                    val userModelList = userEntity.map { it.toRoomUserModel() }
                    _getAllUserData.value = userModelList
                }
            } catch (e: Exception) {
                _getAllUserData.value = emptyList()
            }
        }
    }

    fun setSelectedUserUIds(uIds: List<String>) {
        _selectedUserUIds.value = uIds
        Log.d("selectUid", "${_selectedUserUIds.value}")
    }

    private fun loadToCurrentUseData() {
        viewModelScope.launch {
            loadToCurrentUserDataUseCase().collect { currentUserData ->
                val currentUser = currentUserData?.toRoomUserModel()
                _getCurrentUserData.value = currentUser
            }
        }
    }


    fun getSearchMapList(query: String) {
        viewModelScope.launch {
            runCatching {
                val response = getSearchListUseCase.invoke(
                    query = query,
                    language = "ko",
                    pageSize = 10,
                )
                val models = response.places!!.map {
                    PlaceModel(
                        lat = it.location?.latitude!!,
                        lng = it.location?.longitude!!,
                        name = it.displayName?.text,
                        address = it.formattedAddress,
                        rating = it.rating,
                        phoneNumber = it.nationalPhoneNumber,
                        img = "https://places.googleapis.com/v1/${it.photos?.get(0)?.name}/media?key=${NetWorkClient.API_KEY}&maxHeightPx=500&maxWidthPx=750",
                        description = it.regularOpeningHours?.weekdayDescriptions
                    )
                }
                _searchMapList.postValue(models)
            }.onFailure { e ->
                Log.d("실패", e.toString())
            }
        }
    }

    fun makeAPromiseRoom(
        roomId: String,
        roomTitle: String,
        promiseTime: String,
        promiseDate: String,
        destination: String,
        destinationLat: Double,
        destinationLng: Double,
        penalty: String,
        participants: List<String>
    ) {
        viewModelScope.launch {
            Log.d("makeAChatroom2", "title: ${participants}")
            makeAPromiseRoomUseCase(
                roomId,
                roomTitle,
                promiseTime,
                promiseDate,
                destination,
                destinationLat,
                destinationLng,
                penalty,
                participants
            ).collect {
                _makeARoomResult.value = it
            }
        }
    }

    fun getFriendsList() {
        viewModelScope.launch {
            getCurrentUserUseCase().collect { uid ->
                _getCurrentUser.value = uid

                if (uid.isNotBlank()) {
                    getFriendsListFromFirebaseUseCase(uid).collect { friends ->
                        Log.d("FriendsViewModel1", "Fetched friends: $friends")
                        _friendsList.value = friends.map { it.toRoomUserModel() }
                    }
                }
            }
        }
    }
}


class RoomViewModelFactory(
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val getSearchListUseCase: GetSearchListUseCase,
    private val makeAPromiseRoomUseCase: MakeAPromiseRoomUseCase,
    private val loadToCurrentUserDataUseCase: LoadToCurrentUserDataUseCase,
    private val getFriendsListFromFirebaseUseCase: GetFriendsListFromFirebaseUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoomViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RoomViewModel(
                getAllUsersUseCase,
                getSearchListUseCase,
                makeAPromiseRoomUseCase,
                loadToCurrentUserDataUseCase,
                getFriendsListFromFirebaseUseCase,
                getCurrentUserUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}



