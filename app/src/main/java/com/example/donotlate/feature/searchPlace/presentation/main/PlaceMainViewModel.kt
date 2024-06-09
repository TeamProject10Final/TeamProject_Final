package com.example.donotlate.feature.searchPlace.presentation.main

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
import com.example.donotlate.feature.searchPlace.presentation.data.PlacesChipModel
import com.example.donotlate.feature.searchPlace.presentation.search.ChipType
import kotlinx.coroutines.launch

class PlaceMainViewModel(
    private val googlePlacesRepository: GooglePlacesRepository
) : ViewModel() {


    private val _clickList = MutableLiveData<List<PlacesChipModel>>()
    val clickList: LiveData<List<PlacesChipModel>> = _clickList

    fun getClickMapList(types: ChipType){
        viewModelScope.launch {
            runCatching{//에러 캐칭
                val response = googlePlacesRepository.clickChipList(
                    types = types.typeName,
                    language = "ko"
                )
                Log.d("응답", "${response}")
                val models = response.places!!.map {
                    PlacesChipModel(
                        types = it.types,
                        lat = it.location?.latitude.toString(),
                        lng = it.location?.longitude.toString(),
                        name = it.displayName?.text,
                        address = it.formattedAddress,
                        rating = it.rating,
                        phoneNumber = it.nationalPhoneNumber,
                        img = "https://places.googleapis.com/v1/${it.photos?.get(0)?.name }/media?key=${NetWorkClient.API_KEY}&maxHeightPx=500&maxWidthPx=750"
                    )
                }
                _clickList.postValue(models)
            }.onFailure { e ->
                Log.d("실패", e.toString())
            }
        }
    }


    class SearchChipPlaceViewModelFactory : ViewModelProvider.Factory {
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