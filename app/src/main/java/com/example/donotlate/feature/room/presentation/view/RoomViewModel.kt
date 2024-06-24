package com.example.donotlate.feature.room.presentation.view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.donotlate.core.domain.usecase.GetFriendsListFromFirebaseUseCase
import com.example.donotlate.core.presentation.CurrentUser
import com.example.donotlate.feature.mypromise.presentation.mapper.toPromiseEntity
import com.example.donotlate.feature.mypromise.presentation.model.PromiseModel
import com.example.donotlate.feature.room.domain.usecase.MakeAPromiseRoomUseCase
import com.example.donotlate.feature.room.presentation.mapper.toRoomUserModel
import com.example.donotlate.feature.room.presentation.model.RoomModel
import com.example.donotlate.feature.room.presentation.model.RoomUserModel
import com.example.donotlate.feature.searchPlace.domain.usecase.GetSearchListUseCase
import com.example.donotlate.feature.searchPlace.presentation.mapper.PlaceModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RoomViewModel(
    private val getSearchListUseCase: GetSearchListUseCase,
    private val makeAPromiseRoomUseCase: MakeAPromiseRoomUseCase,
    private val getFriendsListFromFirebaseUseCase: GetFriendsListFromFirebaseUseCase,
) : ViewModel() {

    private val userData = CurrentUser.userData

    private val _makeARoomResult = MutableStateFlow<Boolean?>(null)
    val makeARoomResult: StateFlow<Boolean?> get() = _makeARoomResult

    private val _selectedUserUIds = MutableLiveData<List<String>>(emptyList())
    val selectedUserUIds: LiveData<List<String>> get() = _selectedUserUIds

    private val _searchMapList = MutableLiveData<List<PlaceModel>>()
    val searchMapList: LiveData<List<PlaceModel>> = _searchMapList

    private val _selectedUserNames = MutableLiveData<List<String>>()
    val selectedUserNames: LiveData<List<String>> get() = _selectedUserNames

    private val _friendsList = MutableStateFlow<List<RoomUserModel>>(listOf())
    val friendsList: StateFlow<List<RoomUserModel>> get() = _friendsList

    private val _modelCurrent = MutableLiveData<Int>()
    val modelCurrent: LiveData<Int> = _modelCurrent

    fun setCurrentItem(current: Int) {
        _modelCurrent.value = current
    }

    fun updateSelectedUserNames(userNames: List<String>) {
        _selectedUserNames.value = userNames
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

    fun setSelectedUserUIds(uIds: List<String>) {
        _selectedUserUIds.value = uIds
        Log.d("selectUid", "${_selectedUserUIds.value}")
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
                        img = it.photos?.get(0)?.name,
                        description = it.regularOpeningHours?.weekdayDescriptions
                    )
                }
                _searchMapList.postValue(models)
            }.onFailure { e ->
                Log.d("실패", e.toString())
            }
        }
    }

    fun makeAPromiseRoom(roomInfo: PromiseModel) {
        viewModelScope.launch {
            makeAPromiseRoomUseCase(roomInfo.toPromiseEntity()).collect {
                _makeARoomResult.value = it
            }
        }
    }

    fun getFriendsList() {
        val uid = userData?.uId

        if (uid != null) {
            viewModelScope.launch {
                getFriendsListFromFirebaseUseCase(uid).collect { friends ->
                    Log.d("FriendsViewModel1", "Fetched friends: $friends")
                    _friendsList.value = friends.map { it.toRoomUserModel() }
                }
            }
        }
    }
}


class RoomViewModelFactory(
    private val getSearchListUseCase: GetSearchListUseCase,
    private val makeAPromiseRoomUseCase: MakeAPromiseRoomUseCase,
    private val getFriendsListFromFirebaseUseCase: GetFriendsListFromFirebaseUseCase,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoomViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RoomViewModel(
                getSearchListUseCase,
                makeAPromiseRoomUseCase,
                getFriendsListFromFirebaseUseCase,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}



