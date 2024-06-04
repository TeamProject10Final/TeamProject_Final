package com.example.donotlate.feature.searchPlace.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.donotlate.feature.searchPlace.data.repository.GooglePlacesRepository
import com.example.donotlate.feature.searchPlace.domain.model.toModel
import kotlinx.coroutines.launch

class SearchPlaceViewModel(
    private val googlePlacesRepository: GooglePlacesRepository
) : ViewModel() {

    private val _getSearchType = MutableLiveData<GooglePlacesModel>()
    val getSearchType: LiveData<GooglePlacesModel> get() = _getSearchType

    fun getSearchType(location:String, types: String) = viewModelScope.launch {
        val result = googlePlacesRepository.getPlaceTypeList(location = location, types)

        result.toModel().let {
            _getSearchType.value
        }
    }
}