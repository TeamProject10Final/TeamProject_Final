package com.example.donotlate.feature.searchPlace.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.donotlate.feature.searchPlace.presentation.mapper.PlaceModel
import com.google.android.gms.maps.model.LatLng

class PlaceDetailViewModel : ViewModel() {

    private val _data = MutableLiveData<PlaceModel>()
    val data: LiveData<PlaceModel> get() = _data

    private val _userLocation = MutableLiveData<LatLng>()
    val userLocation: LiveData<LatLng> get() = _userLocation

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun setSelectedItem(selectedItem: PlaceModel) {
        _data.value = selectedItem
    }
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

    class PlaceDetailViewModelFactory : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(
            modelClass: Class<T>,
            extras: CreationExtras
        ): T {
            if (modelClass.isAssignableFrom(PlaceDetailViewModel::class.java)) {
                return PlaceDetailViewModel() as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}