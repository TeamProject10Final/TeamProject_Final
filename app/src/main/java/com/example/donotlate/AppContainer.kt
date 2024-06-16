package com.example.donotlate

import com.example.donotlate.core.data.repository.FirebaseDataSourceImpl
import com.example.donotlate.core.domain.usecase.AcceptFriendRequestsUseCase
import com.example.donotlate.core.domain.usecase.GetCurrentUserUseCase
import com.example.donotlate.core.domain.usecase.GetFriendRequestsListUseCase
import com.example.donotlate.core.domain.usecase.GetFriendRequestsStatusUseCase
import com.example.donotlate.core.domain.usecase.GetFriendsListFromFirebaseUseCase
import com.example.donotlate.core.domain.usecase.GetUserDataUseCase
import com.example.donotlate.core.domain.usecase.LoadToCurrentUserDataUseCase
import com.example.donotlate.core.domain.usecase.LoadToMyPromiseListUseCase
import com.example.donotlate.core.domain.usecase.MakeAFriendRequestUseCase
import com.example.donotlate.core.domain.usecase.SearchUserByIdUseCase
import com.example.donotlate.feature.auth.data.repository.AuthRepositoryImpl
import com.example.donotlate.feature.auth.domain.useCase.LogInUseCase
import com.example.donotlate.feature.auth.domain.useCase.SignUpUseCase
import com.example.donotlate.feature.auth.presentation.viewmodel.LogInViewModelFactory
import com.example.donotlate.feature.auth.presentation.viewmodel.SignUpViewmodelFactory
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
import com.example.donotlate.feature.consumption.presentation.SharedViewModelFactory
import com.example.donotlate.feature.directionRoute.api.RouteNetworkClient
import com.example.donotlate.feature.directionRoute.data.DirectionsRepository
import com.example.donotlate.feature.directionRoute.data.DirectionsRepositoryImpl
import com.example.donotlate.feature.directionRoute.domain.usecase.GetDirectionsUseCase
import com.example.donotlate.feature.directionRoute.presentation.DirectionsViewModel1Factory
import com.example.donotlate.feature.friends.data.repository.FriendRequestRepositoryImpl
import com.example.donotlate.feature.friends.presentation.viewmodel.FriendsViewModelFactory
import com.example.donotlate.feature.main.presentation.viewmodel.MainPageViewModelFactory
import com.example.donotlate.feature.mypromise.presentation.viewmodel.MyPromiseViewModelFactory
import com.example.donotlate.feature.room.domain.usecase.GetAllUsersUseCase
import com.example.donotlate.feature.room.domain.usecase.MakeAPromiseRoomUseCase
import com.example.donotlate.feature.room.presentation.viewmodel.RoomViewModelFactory
import com.example.donotlate.feature.searchPlace.api.NetWorkClient
import com.example.donotlate.feature.searchPlace.data.repository.GooglePlacesApiRepositoryImpl
import com.example.donotlate.feature.searchPlace.domain.repository.GooglePlacesApiRepository
import com.example.donotlate.feature.searchPlace.domain.usecase.GetSearchListUseCase
import com.example.donotlate.feature.searchPlace.presentation.search.PlaceSearchViewModelFactory
import com.example.donotlate.feature.setting.data.repository.SettingRepositoryImpl
import com.example.donotlate.feature.setting.domain.usecase.ImageUploadUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


class AppContainer {

    val consumptionRepository: ConsumptionRepository by lazy {
        ConsumptionRepositoryImpl(DoNotLateApplication.getInstance())
    }

    val authRepository by lazy {
        AuthRepositoryImpl(firebaseAuth, firebaseFireStore, DoNotLateApplication.getInstance())
    }

    val googlePlacesApiRepository: GooglePlacesApiRepository by lazy {
        GooglePlacesApiRepositoryImpl(googleApiService)
    }

    var calculationContainer: CalculationContainer? = null

    var consumptionContainer: ConsumptionContainer? = null


    var logInContainer: LogInContainer? = null
    var signUpContainer: SignUpContainer? = null

    var friendsContainer: FriendsContainer? = null


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

    private val firebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }

    private val firebaseDataRepository by lazy {
        FirebaseDataSourceImpl(firebaseFireStore, firebaseAuth)
    }

    private val settingRepository by lazy {
        SettingRepositoryImpl(firebaseAuth, firebaseFireStore, firebaseStorage)
    }

    val friendRequestRepository by lazy {
        FriendRequestRepositoryImpl(
            firebaseFireStore,
            firebaseAuth,
            DoNotLateApplication.getInstance()
        )
    }
//    val getUserUseCase1 by lazy {
//        GetUserUseCase(userRepository)
//    }

    val getUserDataUseCase by lazy {
        GetUserDataUseCase(firebaseDataRepository)
    }

    val getAllUsersUseCase by lazy {
        GetAllUsersUseCase(firebaseDataRepository)
    }

    val getCurrentUserUseCase by lazy {
        GetCurrentUserUseCase(authRepository)
    }

    val getFriendsListFromFirebaseUseCase by lazy {
        GetFriendsListFromFirebaseUseCase(firebaseDataRepository)
    }

    val searchUserByIdUseCase by lazy {
        SearchUserByIdUseCase(firebaseDataRepository)
    }

    val makeAFriendRequestUseCase by lazy {
        MakeAFriendRequestUseCase(firebaseDataRepository)
    }

    val getFriendRequestsStatusUseCase by lazy {
        GetFriendRequestsStatusUseCase(firebaseDataRepository)
    }

    val getFriendRequestListUseCase by lazy {
        GetFriendRequestsListUseCase(firebaseDataRepository)
    }

    val acceptFriendRequestsUseCase by lazy {
        AcceptFriendRequestsUseCase(firebaseDataRepository)
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

    val imageUploadUseCase by lazy {
        ImageUploadUseCase(settingRepository)
    }

    val makeAPromiseRoomUseCase by lazy {
        MakeAPromiseRoomUseCase(firebaseDataRepository)
    }

    val loadToMyPromiseListUseCase by lazy {
        LoadToMyPromiseListUseCase(firebaseDataRepository)
    }

    val loadToCurrentUserDataUseCase by lazy {
        LoadToCurrentUserDataUseCase(firebaseDataRepository)
    }

    private val directionsApiService = RouteNetworkClient.directionsApiService

    val directionsRepository : DirectionsRepository by lazy {
        DirectionsRepositoryImpl(directionsApiService)
    }

    val getDirectionsUseCase: GetDirectionsUseCase by lazy {
        GetDirectionsUseCase(directionsRepository)
    }


    val directions1Container : Directions1Container by lazy {
        Directions1Container(getDirectionsUseCase)
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
    private val getUserDataUseCase: GetUserDataUseCase,
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val imageUploadUseCase: ImageUploadUseCase,
) {
    val mainPageViewModelFactory = MainPageViewModelFactory(
        getUserDataUseCase,
        getAllUsersUseCase,
        getCurrentUserUseCase,
        imageUploadUseCase
    )
}

class RoomContainer(
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val getSearchListUseCase: GetSearchListUseCase,
    private val makeAPromiseRoomUseCase: MakeAPromiseRoomUseCase,
    private val loadToCurrentUserDataUseCase: LoadToCurrentUserDataUseCase

) {
    val roomViewModelFactory =
        RoomViewModelFactory(
            getAllUsersUseCase,
            getSearchListUseCase,
            makeAPromiseRoomUseCase,
            loadToCurrentUserDataUseCase
        )
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
) {
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
) {
    val placeSearchViewModelFactory = PlaceSearchViewModelFactory(
        getSearchListUseCase
    )
}

class FriendsContainer(
    val getCurrentUserUseCase: GetCurrentUserUseCase,
    val getFriendsListFromFirebaseUseCase: GetFriendsListFromFirebaseUseCase,
    val searchUserByIdUseCase: SearchUserByIdUseCase,
    val makeAFriendRequestUseCase: MakeAFriendRequestUseCase,
    val getUserDataUseCase: GetUserDataUseCase,
    val getFriendRequestsStatusUseCase: GetFriendRequestsStatusUseCase,
    val getFriendRequestsListUseCase: GetFriendRequestsListUseCase,
    val acceptFriendRequestsUseCase: AcceptFriendRequestsUseCase
) {
    val friendsViewModelFactory = FriendsViewModelFactory(
        getFriendsListFromFirebaseUseCase,
        getCurrentUserUseCase,
        searchUserByIdUseCase,
        makeAFriendRequestUseCase,
        getUserDataUseCase,
        getFriendRequestsStatusUseCase,
        getFriendRequestsListUseCase,
        acceptFriendRequestsUseCase
    )
}

class MyPromiseContainer(
    val loadToMyPromiseListUseCase: LoadToMyPromiseListUseCase,
) {
    val myPromiseViewModelFactory = MyPromiseViewModelFactory(
        loadToMyPromiseListUseCase
    )
}

class Directions1Container(
    private val getDirectionsUseCase: GetDirectionsUseCase
){
    val directionsViewModel1Factory = DirectionsViewModel1Factory(
        getDirectionsUseCase
    )
}