package com.example.donotlate

import com.example.donotlate.core.data.repository.FirebaseDataSourceImpl
import com.example.donotlate.feature.consumption.presentation.SharedViewModelFactory
import com.example.donotlate.feature.consumption.data.repository.ConsumptionRepositoryImpl
import com.example.donotlate.feature.consumption.domain.repository.ConsumptionRepository
import com.example.donotlate.feature.consumption.domain.usecase.DeleteConsumptionUseCase
import com.example.donotlate.feature.consumption.domain.usecase.GetConsumptionByCategoryUseCase
import com.example.donotlate.feature.consumption.domain.usecase.GetConsumptionByIdUseCase
import com.example.donotlate.feature.consumption.domain.usecase.GetConsumptionDataUseCase
import com.example.donotlate.feature.consumption.domain.usecase.GetDataCountUseCase
import com.example.donotlate.feature.consumption.domain.usecase.GetFinishedConsumptionUseCase
import com.example.donotlate.feature.consumption.domain.usecase.GetLiveDataCountUseCase
import com.example.donotlate.feature.consumption.domain.usecase.GetTotalPriceUseCase
import com.example.donotlate.feature.consumption.domain.usecase.GetUnfinishedConsumptionUseCase
import com.example.donotlate.feature.consumption.domain.usecase.InsertConsumptionUseCase
import com.example.donotlate.feature.consumption.domain.usecase.ToggleIsFinishedUseCase
import com.example.donotlate.feature.consumption.presentation.ConsumptionViewModelFactory
import com.example.donotlate.feature.auth.data.repository.AuthRepositoryImpl
import com.example.donotlate.feature.auth.domain.repository.AuthRepository
import com.example.donotlate.feature.auth.domain.useCase.LogInUseCase
import com.example.donotlate.feature.auth.domain.useCase.SignUpUseCase
import com.example.donotlate.feature.auth.presentation.viewmodel.LogInViewModelFactory
import com.example.donotlate.feature.auth.presentation.viewmodel.SignUpViewmodelFactory
import com.example.donotlate.feature.main.domain.usecase.GetCurrentUserUseCase
import com.example.donotlate.feature.main.domain.usecase.GetUserUseCase
import com.example.donotlate.feature.main.presentation.MainPageViewModelFactory
import com.example.donotlate.feature.room.domain.usecase.GetAllUsersUseCase
import com.example.donotlate.feature.room.presentation.viewmodel.RoomViewModelFactory
import com.example.donotlate.feature.searchPlace.api.NetWorkClient
import com.example.donotlate.feature.searchPlace.data.repository.GooglePlacesApiRepositoryImpl
import com.example.donotlate.feature.searchPlace.domain.repository.GooglePlacesApiRepository
import com.example.donotlate.feature.searchPlace.domain.usecase.GetSearchListUseCase
import com.example.donotlate.feature.searchPlace.presentation.search.PlaceSearchViewModel
import com.example.donotlate.feature.searchPlace.presentation.search.PlaceSearchViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore



class AppContainer {

    val consumptionRepository: ConsumptionRepository by lazy {
        ConsumptionRepositoryImpl(DoNotLateApplication.getInstance())
    }

    val authRepository by lazy {
        AuthRepositoryImpl(firebaseAuth, firebaseFireStore, DoNotLateApplication.getInstance())
    }

    val googlePlacesApiRepository : GooglePlacesApiRepository by lazy {
        GooglePlacesApiRepositoryImpl(googleApiService)
    }

    var calculationContainer: CalculationContainer? = null

    var consumptionContainer: ConsumptionContainer?=null


    var logInContainer: LogInContainer? = null
    var signUpContainer: SignUpContainer? = null




    val getFinishedConsumptionUseCase: GetFinishedConsumptionUseCase by lazy {
        GetFinishedConsumptionUseCase(consumptionRepository)
    }

    val getUnfinishedConsumptionUseCase: GetUnfinishedConsumptionUseCase by lazy {
        GetUnfinishedConsumptionUseCase(consumptionRepository)
    }

    val insertConsumptionUseCase: InsertConsumptionUseCase by lazy {
        InsertConsumptionUseCase(consumptionRepository)
    }

    val deleteConsumptionUseCase: DeleteConsumptionUseCase by lazy {
        DeleteConsumptionUseCase(consumptionRepository)
    }

    val getConsumptionByCategoryUseCase: GetConsumptionByCategoryUseCase by lazy {
        GetConsumptionByCategoryUseCase(consumptionRepository)
    }

    val getConsumptionByIdUseCase: GetConsumptionByIdUseCase by lazy {
        GetConsumptionByIdUseCase(consumptionRepository)
    }

    val getConsumptionDataUseCase: GetConsumptionDataUseCase by lazy {
        GetConsumptionDataUseCase(consumptionRepository)
    }

    val getTotalPriceUseCase: GetTotalPriceUseCase by lazy {
        GetTotalPriceUseCase(consumptionRepository)
    }
    val getDataCountUseCase: GetDataCountUseCase by lazy {
        GetDataCountUseCase(consumptionRepository)
    }
    val getLiveDataCountUseCase: GetLiveDataCountUseCase by lazy {
        GetLiveDataCountUseCase(consumptionRepository)
    }

    val toggleIsFinishedUseCase: ToggleIsFinishedUseCase by lazy {
        ToggleIsFinishedUseCase(consumptionRepository)
    }

    private val googleApiService = NetWorkClient.searchNetWork

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val firebaseFireStore by lazy {
        FirebaseFirestore.getInstance()
    }

    private val userRepository by lazy {
        FirebaseDataSourceImpl(firebaseFireStore)
    }
//    val getUserUseCase1 by lazy {
//        GetUserUseCase(userRepository)
//    }

    val getUserUseCase by lazy{
        GetUserUseCase(userRepository)
    }

    val getAllUsersUseCase by lazy {
        GetAllUsersUseCase(userRepository)
    }

    val getCurrentUserUseCase by lazy {
        GetCurrentUserUseCase(authRepository)
    }


    val logInUseCase by lazy {
        LogInUseCase(authRepository)
    }

    val signUpUseCase by lazy {
        SignUpUseCase(authRepository)
    }

    val getSearchListUseCase by lazy {
        GetSearchListUseCase(googlePlacesApiRepository)
    }

}

class LogInContainer(
    private val logInUseCase: LogInUseCase
) {
    val logInViewModelFactory = LogInViewModelFactory(logInUseCase)
}

class SignUpContainer(
    private val signUpUseCase: SignUpUseCase
) {
    val signUpViewModelFactory = SignUpViewmodelFactory(signUpUseCase)
}

class MainPageContainer(
    private val getUserUseCase: GetUserUseCase,
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
){
    val mainPageViewModelFactory = MainPageViewModelFactory(
        getUserUseCase, getAllUsersUseCase, getCurrentUserUseCase
    )
}

class RoomContainer(
    private val getAllUsersUseCase: GetAllUsersUseCase
) {
    val roomViewModelFactory = RoomViewModelFactory(getAllUsersUseCase)
}

class CalculationContainer(
//    private val consumptionRepository: ConsumptionRepository,
//    val getFinishedConsumptionUseCase: GetFinishedConsumptionUseCase,
//    val getUnfinishedConsumptionUseCase: GetUnfinishedConsumptionUseCase,
    val insertConsumptionUseCase: InsertConsumptionUseCase,
    val deleteConsumptionUseCase: DeleteConsumptionUseCase,
//    val getConsumptionByCategoryUseCase: GetConsumptionByCategoryUseCase,
//    val getConsumptionByIdUseCase: GetConsumptionByIdUseCase,
//    val getConsumptionDataUseCase: GetConsumptionDataUseCase,
//    val getTotalPriceUseCase: GetTotalPriceUseCase,
    val getDataCountUseCase: GetDataCountUseCase
) {
    val sharedViewModelFactory = SharedViewModelFactory(
//        consumptionRepository,
//        getFinishedConsumptionUseCase,
//        getUnfinishedConsumptionUseCase,
        insertConsumptionUseCase,
        deleteConsumptionUseCase,
//        getConsumptionByCategoryUseCase,
//        getConsumptionByIdUseCase,
//        getConsumptionDataUseCase,
//        getTotalPriceUseCase,
        getDataCountUseCase
    )
}

class ConsumptionContainer(
    val getFinishedConsumptionUseCase: GetFinishedConsumptionUseCase,
    val getUnfinishedConsumptionUseCase: GetUnfinishedConsumptionUseCase,
    val insertConsumptionUseCase: InsertConsumptionUseCase,
    val deleteConsumptionUseCase: DeleteConsumptionUseCase,
    val getConsumptionByCategoryUseCase: GetConsumptionByCategoryUseCase,
    val getConsumptionByIdUseCase: GetConsumptionByIdUseCase,
    val getConsumptionDataUseCase: GetConsumptionDataUseCase,
    val getTotalPriceUseCase: GetTotalPriceUseCase,
    val getDataCountUseCase: GetDataCountUseCase,
    val getLiveDataCountUseCase: GetLiveDataCountUseCase,
    val toggleIsFinishedUseCase: ToggleIsFinishedUseCase
){
    val consumptionViewModelFactory = ConsumptionViewModelFactory(
        getFinishedConsumptionUseCase,
        getUnfinishedConsumptionUseCase,
        insertConsumptionUseCase,
        deleteConsumptionUseCase,
        getConsumptionByCategoryUseCase,
        getConsumptionByIdUseCase,
        getConsumptionDataUseCase,
        getTotalPriceUseCase,
        getDataCountUseCase,
        getLiveDataCountUseCase,
        toggleIsFinishedUseCase
    )
}

class SearchPlaceContainer(
    val getSearchListUseCase: GetSearchListUseCase
){
    val placeSearchViewModelFactory = PlaceSearchViewModelFactory(
        getSearchListUseCase
    )
}