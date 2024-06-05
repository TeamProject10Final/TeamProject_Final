package com.example.donotlate.feature.searchPlace.presentation

import android.util.Log
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
import kotlinx.coroutines.launch

class SearchPlaceViewModel(
    private val googlePlacesRepository: GooglePlacesRepository
) : ViewModel() {

    private val _getSearchType = MutableLiveData<GooglePlacesModel>()
    val getSearchType: LiveData<GooglePlacesModel> get() = _getSearchType

    private val _searchMap = MutableLiveData<List<SearchModel>>()
    val searchMap: MutableLiveData<List<SearchModel>> = _searchMap

    fun getSearchType(location: String, types: String) = viewModelScope.launch {
        val result = googlePlacesRepository.getPlaceTypeList(location = location, types)

        result.toModel().let {
            _getSearchType.value
        }
    }

    fun getSearchMap(query: String) {
        viewModelScope.launch {
            runCatching {
                val response = googlePlacesRepository.searchList(
                    query = query,
                    radius = 1500,
                    apiKey = "AIzaSyAl7nz1KScbyyDNKUeYz4rrePkFZBDvhkc",
                    language = "ko"
                )
                val models = response.results!!.map {
                    SearchModel(
                        lat = it.geometry?.location?.lat.toString(),
                        lng = it.geometry?.location?.lng.toString(),
                        name = it.name,
                        address = it.formattedAddress,
                        rating = it.rating.toString(),
                        phoneNumber = it.phoneNumber,
                        img = it.photos?.map { it.htmlAttributions!! }
                    )
                }
                _searchMap.postValue(models)
            }.onFailure { e ->
                Log.d("실패", e.toString())
            }
        }
    }

    class SearchPlaceViewModelFactory : ViewModelProvider.Factory {
        private val repository =
            GooglePlacesRepositoryImpl(googlePlacesApiService = NetWorkClient.googleNetWork)

        override fun <T : ViewModel> create(
            modelClass: Class<T>,
            extras: CreationExtras
        ): T = SearchPlaceViewModel(
            repository
        ) as T

    }
}