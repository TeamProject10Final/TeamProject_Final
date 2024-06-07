package com.example.donotlate.feature.searchPlace.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.donotlate.feature.searchPlace.api.NetWorkClient
import com.example.donotlate.feature.searchPlace.data.repository.GooglePlacesRepository
import com.example.donotlate.feature.searchPlace.data.repository.GooglePlacesRepositoryImpl
import com.example.donotlate.feature.searchPlace.domain.model.PlacesEntity
import com.example.donotlate.feature.searchPlace.domain.model.toModel
import com.example.donotlate.feature.searchPlace.presentation.data.GooglePlacesModel
import com.example.donotlate.feature.searchPlace.presentation.data.PlaceModel
import kotlinx.coroutines.launch

class PlaceMainViewModel(
    private val googlePlacesRepository: GooglePlacesRepository
) : ViewModel() {

    private val _searchMap = MutableLiveData<List<PlaceModel>>()
    val searchMap: LiveData<List<PlaceModel>> = _searchMap

    private val _clickList = MutableLiveData<PlacesEntity>()
    val clickList: LiveData<PlacesEntity> = _clickList

    fun getClickMapList(types: String){
        viewModelScope.launch {
            runCatching{//에러 캐칭
                val response = googlePlacesRepository.clickChipList(
                    types = types,
                    language = "ko"
                )
            }
        }
    }//에러처리를 맞추는?


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