package com.example.donotlate.feature.searchPlace.presentation.search

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
import com.example.donotlate.feature.searchPlace.domain.model.PlacesEntity
import com.example.donotlate.feature.searchPlace.presentation.data.PlaceModel
import kotlinx.coroutines.launch

class PlaceSearchViewModel(
    private val googlePlacesRepository: GooglePlacesRepository
) : ViewModel() {

    private val _searchMapList = MutableLiveData<List<PlaceModel>>()
    val searchMapList: LiveData<List<PlaceModel>> = _searchMapList

    private var inputText: MutableLiveData<String> = MutableLiveData()




    fun getData(): MutableLiveData<String> = inputText

    fun updateText(input: String) {
        inputText.value = input
    }


    fun getSearchMapList(query: String) {
        viewModelScope.launch {
            runCatching {
                val response = googlePlacesRepository.searchPlacesList(
                    query = query,
                    language = "ko"
                )
                val models = response.places!!.map {
                    PlaceModel(
                        lat = it.location?.latitude.toString(),
                        lng = it.location?.longitude.toString(),
                        name = it.displayName?.text,
                        address = it.formattedAddress,
                        rating = it.rating,
                        phoneNumber = it.nationalPhoneNumber,
                        img = "https://places.googleapis.com/v1/${it.photos?.get(0)?.name }/media?key=${NetWorkClient.API_KEY}&maxHeightPx=500&maxWidthPx=750"
                    )
                }
                _searchMapList.postValue(models)
            }.onFailure { e ->
                Log.d("실패", e.toString())
            }
        }
    }




    class SearchViewModelFactory : ViewModelProvider.Factory {
        private val repository =
            GooglePlacesRepositoryImpl(googlePlacesApiService = NetWorkClient.searchNetWork)

        override fun <T : ViewModel> create(
            modelClass: Class<T>,
            extras: CreationExtras
        ): T = PlaceSearchViewModel(
            repository
        ) as T
    }
}