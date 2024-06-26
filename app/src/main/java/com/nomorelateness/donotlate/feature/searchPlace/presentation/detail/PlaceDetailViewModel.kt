package com.nomorelateness.donotlate.feature.searchPlace.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.nomorelateness.donotlate.feature.searchPlace.presentation.mapper.PlaceModel

class PlaceDetailViewModel : ViewModel() {

    private val _data = MutableLiveData<PlaceModel>()
    val data: LiveData<PlaceModel> get() = _data

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun setSelectedItem(selectedItem: PlaceModel) {
        _data.value = selectedItem
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