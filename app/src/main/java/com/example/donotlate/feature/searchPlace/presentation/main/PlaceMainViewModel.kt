package com.example.donotlate.feature.searchPlace.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.donotlate.feature.searchPlace.api.NetWorkClient
import com.example.donotlate.feature.searchPlace.data.repository.GooglePlacesRepository
import com.example.donotlate.feature.searchPlace.data.repository.GooglePlacesRepositoryImpl
import com.example.donotlate.feature.searchPlace.presentation.data.GooglePlacesModel
import com.example.donotlate.feature.searchPlace.presentation.data.PlaceModel

class PlaceMainViewModel(
    private val googlePlacesRepository: GooglePlacesRepository
) : ViewModel() {

    private val _getSearchType = MutableLiveData<GooglePlacesModel>()
    val getSearchType: LiveData<GooglePlacesModel> get() = _getSearchType

    private val _searchMap = MutableLiveData<List<PlaceModel>>()
    val searchMap: LiveData<List<PlaceModel>> = _searchMap



    class SearchPlaceViewModelFactory : ViewModelProvider.Factory {
        private val repository =
            GooglePlacesRepositoryImpl(googlePlacesApiService = NetWorkClient.googleNetWork)

        override fun <T : ViewModel> create(
            modelClass: Class<T>,
            extras: CreationExtras
        ): T = PlaceMainViewModel(
            repository
        ) as T

    }
}