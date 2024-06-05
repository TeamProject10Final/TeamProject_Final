package com.example.donotlate.feature.searchPlace.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.donotlate.feature.searchPlace.api.NetWorkClient
import com.example.donotlate.feature.searchPlace.data.repository.GooglePlacesRepository
import com.example.donotlate.feature.searchPlace.data.repository.GooglePlacesRepositoryImpl
import com.example.donotlate.feature.searchPlace.domain.model.toModel
import com.example.donotlate.feature.searchPlace.presentation.GooglePlacesModel
import com.example.donotlate.feature.searchPlace.presentation.data.PlaceModel
import kotlinx.coroutines.launch

class PlaceViewModel(
    private val googlePlacesRepository: GooglePlacesRepository
) : ViewModel() {

    private val _getSearchType = MutableLiveData<GooglePlacesModel>()
    val getSearchType: LiveData<GooglePlacesModel> get() = _getSearchType

    private val _searchMap = MutableLiveData<List<PlaceModel>>()
    val searchMap: LiveData<List<PlaceModel>> = _searchMap

    fun getSearchType(location: String, types: String) = viewModelScope.launch {
        val result = googlePlacesRepository.getPlaceTypeList(location = location, types)

        result.toModel().let {
            _getSearchType.value
        }
    }


    class SearchPlaceViewModelFactory : ViewModelProvider.Factory {
        private val repository =
            GooglePlacesRepositoryImpl(googlePlacesApiService = NetWorkClient.googleNetWork)

        override fun <T : ViewModel> create(
            modelClass: Class<T>,
            extras: CreationExtras
        ): T = PlaceViewModel(
            repository
        ) as T

    }
}