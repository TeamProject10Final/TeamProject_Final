package com.example.donotlate.feature.searchPlace.presentation.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.donotlate.feature.searchPlace.api.NetWorkClient
import com.example.donotlate.feature.searchPlace.domain.usecase.GetSearchListUseCase
import com.example.donotlate.feature.searchPlace.presentation.mapper.PlaceModel
import kotlinx.coroutines.launch

class PlaceSearchViewModel(
    private val getSearchListUseCase: GetSearchListUseCase
) : ViewModel() {

    private val _searchMapList = MutableLiveData<List<PlaceModel>>()
    val searchMapList: LiveData<List<PlaceModel>> = _searchMapList

    fun getSearch(): LiveData<List<PlaceModel>> = _searchMapList


    //query 값 넘겨주기
    private var inputText: MutableLiveData<String> = MutableLiveData()
    fun getData(): LiveData<String> = inputText
    fun updateText(input: String) {
        inputText.value = input
    }


    fun getSearchMapList(query: String) {
        viewModelScope.launch {
            runCatching {
                val response = getSearchListUseCase(query, "ko", 10)
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
}


    class PlaceSearchViewModelFactory(
        private val getSearchListUseCase: GetSearchListUseCase
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(
            modelClass: Class<T>, extras: CreationExtras
        ): T {
            if (modelClass.isAssignableFrom(PlaceSearchViewModel::class.java)) {
                return PlaceSearchViewModel(
                    getSearchListUseCase
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
    }
}