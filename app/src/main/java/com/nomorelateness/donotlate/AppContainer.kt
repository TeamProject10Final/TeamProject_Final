package com.nomorelateness.donotlate

import SignUpViewmodelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.nomorelateness.donotlate.core.data.repository.FirebaseDataSourceImpl
import com.nomorelateness.donotlate.core.data.repository.PromiseRoomRepositoryImpl
import com.nomorelateness.donotlate.core.data.repository.UserRepositoryImpl
import com.nomorelateness.donotlate.core.data.session.SessionManagerImpl
import com.nomorelateness.donotlate.core.domain.repository.PromiseRoomRepository
import com.nomorelateness.donotlate.core.domain.repository.UserRepository
import com.nomorelateness.donotlate.core.domain.session.SessionManager
import com.nomorelateness.donotlate.core.domain.usecase.AcceptFriendRequestsUseCase
import com.nomorelateness.donotlate.core.domain.usecase.GetCurrentUserDataUseCase
import com.nomorelateness.donotlate.core.domain.usecase.GetFriendRequestsListUseCase
import com.nomorelateness.donotlate.core.domain.usecase.GetFriendRequestsStatusUseCase
import com.nomorelateness.donotlate.core.domain.usecase.GetFriendsListFromFirebaseUseCase
import com.nomorelateness.donotlate.core.domain.usecase.LoadToMyPromiseListUseCase
import com.nomorelateness.donotlate.core.domain.usecase.MakeAFriendRequestUseCase
import com.nomorelateness.donotlate.core.domain.usecase.SearchUserByIdUseCase
import com.nomorelateness.donotlate.core.domain.usecase.promiseusecase.RemoveParticipantsUseCase
import com.nomorelateness.donotlate.feature.auth.data.repository.AuthRepositoryImpl
import com.nomorelateness.donotlate.feature.auth.domain.useCase.CheckUserEmailVerificationUseCase
import com.nomorelateness.donotlate.feature.auth.domain.useCase.DeleteUseCase
import com.nomorelateness.donotlate.feature.auth.domain.useCase.GetCurrentUserWithKakaoUseCase
import com.nomorelateness.donotlate.feature.auth.domain.useCase.LogInUseCase
import com.nomorelateness.donotlate.feature.auth.domain.useCase.SendEmailVerificationUseCase
import com.nomorelateness.donotlate.feature.auth.domain.useCase.SignUpUseCase
import com.nomorelateness.donotlate.feature.auth.domain.useCase.SignUpWithKakaoUseCase
import com.nomorelateness.donotlate.feature.auth.presentation.view.LogInViewModelFactory
import com.nomorelateness.donotlate.feature.consumption.data.repository.ConsumptionRepositoryImpl
import com.nomorelateness.donotlate.feature.consumption.data.repository.ConsumptionRepositoryImpl2
import com.nomorelateness.donotlate.feature.consumption.domain.repository.ConsumptionRepository
import com.nomorelateness.donotlate.feature.consumption.domain.repository.ConsumptionRepository2
import com.nomorelateness.donotlate.feature.consumption.domain.usecase.ClearAllConsumptionsUseCase
import com.nomorelateness.donotlate.feature.consumption.domain.usecase.ConsumptionDataSyncUseCase
import com.nomorelateness.donotlate.feature.consumption.domain.usecase.DeleteConsumptionDataFromFirebaseUseCase
import com.nomorelateness.donotlate.feature.consumption.domain.usecase.DeleteConsumptionUseCase
import com.nomorelateness.donotlate.feature.consumption.domain.usecase.GetConsumptionByCategoryUseCase
import com.nomorelateness.donotlate.feature.consumption.domain.usecase.GetConsumptionByIdUseCase
import com.nomorelateness.donotlate.feature.consumption.domain.usecase.GetConsumptionDataUseCase
import com.nomorelateness.donotlate.feature.consumption.domain.usecase.GetDataCountUseCase
import com.nomorelateness.donotlate.feature.consumption.domain.usecase.GetFinishedConsumptionUseCase
import com.nomorelateness.donotlate.feature.consumption.domain.usecase.GetLiveDataCountUseCase
import com.nomorelateness.donotlate.feature.consumption.domain.usecase.GetTotalPriceUseCase
import com.nomorelateness.donotlate.feature.consumption.domain.usecase.GetUnfinishedConsumptionUseCase
import com.nomorelateness.donotlate.feature.consumption.domain.usecase.InsertConsumptionDataToFirebaseUseCase
import com.nomorelateness.donotlate.feature.consumption.domain.usecase.InsertConsumptionUseCase
import com.nomorelateness.donotlate.feature.consumption.domain.usecase.ToggleIsFinishedUseCase
import com.nomorelateness.donotlate.feature.consumption.domain.usecase.UpdateFinishedFromFirebaseUseCase
import com.nomorelateness.donotlate.feature.consumption.presentation.ConsumptionViewModelFactory
import com.nomorelateness.donotlate.feature.consumption.presentation.SharedViewModelFactory
import com.nomorelateness.donotlate.feature.directionRoute.api.RouteNetworkClient
import com.nomorelateness.donotlate.feature.directionRoute.data.DirectionsRepositoryImpl
import com.nomorelateness.donotlate.feature.directionRoute.domain.DirectionsRepository
import com.nomorelateness.donotlate.feature.directionRoute.domain.usecase.GetDirWithArrTmRpUseCase
import com.nomorelateness.donotlate.feature.directionRoute.domain.usecase.GetDirWithDepTmRpUseCase
import com.nomorelateness.donotlate.feature.directionRoute.domain.usecase.GetDirWithTmRpUseCase
import com.nomorelateness.donotlate.feature.directionRoute.domain.usecase.GetDirectionsUseCase
import com.nomorelateness.donotlate.feature.directionRoute.presentation.DirectionsViewModel1Factory
import com.nomorelateness.donotlate.feature.friends.data.repository.FriendRequestRepositoryImpl
import com.nomorelateness.donotlate.feature.friends.presentation.view.FriendsViewModelFactory
import com.nomorelateness.donotlate.feature.main.presentation.view.MainPageViewModelFactory
import com.nomorelateness.donotlate.feature.mypromise.domain.usecase.MessageReceivingUseCase
import com.nomorelateness.donotlate.feature.mypromise.domain.usecase.MessageSendingUseCase
import com.nomorelateness.donotlate.feature.mypromise.domain.usecase.UpdateArrivalStatusUseCase
import com.nomorelateness.donotlate.feature.mypromise.domain.usecase.UpdateDepartureStatusUseCase
import com.nomorelateness.donotlate.feature.mypromise.presentation.view.MyPromiseListViewModelFactory
import com.nomorelateness.donotlate.feature.mypromise.presentation.view.MyPromiseRoomViewModelFactory
import com.nomorelateness.donotlate.feature.room.domain.usecase.MakeAPromiseRoomUseCase
import com.nomorelateness.donotlate.feature.room.presentation.view.RoomViewModelFactory
import com.nomorelateness.donotlate.feature.searchPlace.api.NetWorkClient
import com.nomorelateness.donotlate.feature.searchPlace.data.repository.GooglePlacesApiRepositoryImpl
import com.nomorelateness.donotlate.feature.searchPlace.domain.repository.GooglePlacesApiRepository
import com.nomorelateness.donotlate.feature.searchPlace.domain.usecase.GetSearchListUseCase
import com.nomorelateness.donotlate.feature.searchPlace.presentation.search.PlaceSearchViewModelFactory
import com.nomorelateness.donotlate.feature.setting.data.repository.SettingRepositoryImpl
import com.nomorelateness.donotlate.feature.setting.domain.usecase.DeleteUserUseCase
import com.nomorelateness.donotlate.feature.setting.domain.usecase.ImageUploadUseCase


class AppContainer {

    val consumptionRepository: ConsumptionRepository by lazy {
        ConsumptionRepositoryImpl(com.nomorelateness.donotlate.DoNotLateApplication.Companion.getInstance())
    }

    val authRepository by lazy {
        AuthRepositoryImpl(
            firebaseAuth, firebaseFireStore,
            com.nomorelateness.donotlate.DoNotLateApplication.Companion.getInstance()
        )
    }

    val googlePlacesApiRepository: GooglePlacesApiRepository by lazy {
        GooglePlacesApiRepositoryImpl(googleApiService)
    }

    var calculationContainer: com.nomorelateness.donotlate.CalculationContainer? = null

    var consumptionContainer: com.nomorelateness.donotlate.ConsumptionContainer? = null

    var logInContainer: com.nomorelateness.donotlate.LogInContainer? = null
    var signUpContainer: com.nomorelateness.donotlate.SignUpContainer? = null
    var friendsContainer: com.nomorelateness.donotlate.FriendsContainer? = null

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

    val firebaseAuth by lazy {
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

    val friendRequestRepository by lazy { FriendRequestRepositoryImpl(firebaseFireStore) }

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

    private val directionsApiService = RouteNetworkClient.directionsApiService

    val directionsRepository: DirectionsRepository by lazy {
        DirectionsRepositoryImpl(directionsApiService)
    }

    val getDirectionsUseCase: GetDirectionsUseCase by lazy {
        GetDirectionsUseCase(directionsRepository)
    }

    val getDirWithTmRpUseCase: GetDirWithTmRpUseCase by lazy {
        GetDirWithTmRpUseCase(directionsRepository)
    }

    val directions1Container: com.nomorelateness.donotlate.Directions1Container by lazy {
        com.nomorelateness.donotlate.Directions1Container(
            getDirectionsUseCase,
            getDirWithDepTmRpUseCase,
            getDirWithTmRpUseCase,
            getDirWithArrTmRpUseCase
        )
    }

    val getDirWithDepTmRpUseCase: GetDirWithDepTmRpUseCase by lazy {
        GetDirWithDepTmRpUseCase(directionsRepository)
    }


    val getDirWithArrTmRpUseCase: GetDirWithArrTmRpUseCase by lazy {
        GetDirWithArrTmRpUseCase(directionsRepository)
    }

    val messageSendingUseCase by lazy {
        MessageSendingUseCase(firebaseDataRepository)
    }

    val messageReceivingUseCase by lazy {
        MessageReceivingUseCase(firebaseDataRepository)
    }

    val getCurrentUserDataUseCase by lazy {
        GetCurrentUserDataUseCase(firebaseDataRepository)
    }

    val sessionManager by lazy {
        SessionManagerImpl()
    }


    // 임시
    private val promiseRoomRepository: PromiseRoomRepository by lazy {
        PromiseRoomRepositoryImpl(firebaseFireStore)
    }

    private val userRepository: UserRepository by lazy {
        UserRepositoryImpl(firebaseFireStore, firebaseAuth)
    }

    private val consumptionRepository2: ConsumptionRepository2 by lazy {
        ConsumptionRepositoryImpl2(firebaseFireStore, firebaseAuth)
    }

    val removeParticipantsUseCase: RemoveParticipantsUseCase by lazy {
        RemoveParticipantsUseCase(promiseRoomRepository)
    }

    val updateArrivalStatusUseCase: UpdateArrivalStatusUseCase by lazy {
        UpdateArrivalStatusUseCase(firebaseDataRepository)
    }

    val updateDepartureStatusUseCase: UpdateDepartureStatusUseCase by lazy {
        UpdateDepartureStatusUseCase(firebaseDataRepository)
    }

    val deleteUserUseCase: DeleteUserUseCase by lazy {
        DeleteUserUseCase(userRepository)
    }

    val sendEmailVerificationUseCase: SendEmailVerificationUseCase by lazy {
        SendEmailVerificationUseCase(authRepository)
    }

    val checkUserEmailVerificationUseCase: CheckUserEmailVerificationUseCase by lazy {
        CheckUserEmailVerificationUseCase(authRepository)
    }

    val deleteUseCase: DeleteUseCase by lazy {
        DeleteUseCase(authRepository)
    }

    val deleteConsumptionFromFirebaseUseCase: DeleteConsumptionDataFromFirebaseUseCase by lazy {
        DeleteConsumptionDataFromFirebaseUseCase(repository2 = consumptionRepository2)
    }

    val insertConsumptionDataToFirebaseUseCase: InsertConsumptionDataToFirebaseUseCase by lazy {
        InsertConsumptionDataToFirebaseUseCase(repository2 = consumptionRepository2)
    }

    val updateFriendsListFromFirebaseUseCase: UpdateFinishedFromFirebaseUseCase by lazy {
        UpdateFinishedFromFirebaseUseCase(repository2 = consumptionRepository2)
    }
    val clearAllConsumptionsUseCase: ClearAllConsumptionsUseCase by lazy {
        ClearAllConsumptionsUseCase(repository = consumptionRepository)
    }
    val consumptionDataSyncUseCase: ConsumptionDataSyncUseCase by lazy {
        ConsumptionDataSyncUseCase(
            repository = consumptionRepository,
            repository2 = consumptionRepository2
        )
    }
    val signUpWithKakaoUseCase: SignUpWithKakaoUseCase by lazy {
        SignUpWithKakaoUseCase(authRepository = authRepository)
    }

    val getCurrentUserWithKakaoUseCase: GetCurrentUserWithKakaoUseCase by lazy {
        GetCurrentUserWithKakaoUseCase(authRepository = authRepository)
    }
}

class LogInContainer(
    private val logInUseCase: LogInUseCase,
    private val checkUserEmailVerificationUseCase: CheckUserEmailVerificationUseCase,
    private val sendVerificationUseCase: SendEmailVerificationUseCase,
    private val sessionManager: SessionManager,
    private val deleteUseCase: DeleteUseCase,
    private val getCurrentUserDataUseCase: GetCurrentUserDataUseCase,
    private val signUpWithKakaoUseCase: SignUpWithKakaoUseCase,
    private val getCurrentUserWithKakaoUseCase: GetCurrentUserWithKakaoUseCase
) {
    val logInViewModelFactory = LogInViewModelFactory(
        logInUseCase = logInUseCase,
        checkUserEmailVerificationUseCase = checkUserEmailVerificationUseCase,
        sendVerificationUseCase = sendVerificationUseCase,
        sessionManager = sessionManager,
        deleteUseCase = deleteUseCase,
        getCurrentUserDataUseCase = getCurrentUserDataUseCase,
        signUpWithKakaoUseCase = signUpWithKakaoUseCase,
        getCurrentUserWithKakaoUseCase,
    )
}

class SignUpContainer(
    private val signUpUseCase: SignUpUseCase,
    private val sessionManager: SessionManager
) {
    val signUpViewModelFactory = SignUpViewmodelFactory(signUpUseCase, sessionManager)
}

class MainPageContainer(
    private val getCurrentUserDataUseCase: GetCurrentUserDataUseCase,
) {
    val mainPageViewModelFactory = MainPageViewModelFactory(
        getCurrentUserDataUseCase,
    )
}

class RoomContainer(
    private val getSearchListUseCase: GetSearchListUseCase,
    private val makeAPromiseRoomUseCase: MakeAPromiseRoomUseCase,
    private val getFriendsListFromFirebaseUseCase: GetFriendsListFromFirebaseUseCase,

    ) {
    val roomViewModelFactory =
        RoomViewModelFactory(
            getSearchListUseCase,
            makeAPromiseRoomUseCase,
            getFriendsListFromFirebaseUseCase,
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
    val getDataCountUseCase: GetDataCountUseCase,
    val insertConsumptionDataToFirebaseUseCase: InsertConsumptionDataToFirebaseUseCase
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
        getDataCountUseCase,
        insertConsumptionDataToFirebaseUseCase
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
    val toggleIsFinishedUseCase: ToggleIsFinishedUseCase,
    val getCurrentUserDataUseCase: GetCurrentUserDataUseCase,
    val deleteConsumptionFromFirebaseUseCase: DeleteConsumptionDataFromFirebaseUseCase,
    val insertConsumptionDataToFirebaseUseCase: InsertConsumptionDataToFirebaseUseCase,
    val updateFriendsListFromFirebaseUseCase: UpdateFinishedFromFirebaseUseCase,
    val consumptionDataSyncUseCase: ConsumptionDataSyncUseCase
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
        toggleIsFinishedUseCase,
        getCurrentUserDataUseCase,
        deleteConsumptionFromFirebaseUseCase,
        insertConsumptionDataToFirebaseUseCase,
        updateFriendsListFromFirebaseUseCase,
        consumptionDataSyncUseCase
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
    val getFriendsListFromFirebaseUseCase: GetFriendsListFromFirebaseUseCase,
    val searchUserByIdUseCase: SearchUserByIdUseCase,
    val makeAFriendRequestUseCase: MakeAFriendRequestUseCase,
    val getFriendRequestsStatusUseCase: GetFriendRequestsStatusUseCase,
    val getFriendRequestsListUseCase: GetFriendRequestsListUseCase,
    val acceptFriendRequestsUseCase: AcceptFriendRequestsUseCase
) {
    val friendsViewModelFactory = FriendsViewModelFactory(
        getFriendsListFromFirebaseUseCase,
        searchUserByIdUseCase,
        makeAFriendRequestUseCase,
        getFriendRequestsStatusUseCase,
        getFriendRequestsListUseCase,
        acceptFriendRequestsUseCase
    )
}

class MyPromiseRoomContainer(
    val messageSendingUseCase: MessageSendingUseCase,
    val messageReceivingUseCase: MessageReceivingUseCase,
    private val getDirectionsUseCase: GetDirectionsUseCase,
    val removeParticipantsUseCase: RemoveParticipantsUseCase,
    val updateArrivalStatusUseCase: UpdateArrivalStatusUseCase,
    val updateDepartureStatusUseCase: UpdateDepartureStatusUseCase,
    val getCurrentUserDataUseCase: GetCurrentUserDataUseCase

) {
    val myPromiseViewModelFactory = MyPromiseRoomViewModelFactory(
        messageSendingUseCase,
        messageReceivingUseCase,
        getDirectionsUseCase,
        removeParticipantsUseCase,
        updateArrivalStatusUseCase,
        updateDepartureStatusUseCase,
        getCurrentUserDataUseCase
    )
}

class Directions1Container(
    private val getDirectionsUseCase: GetDirectionsUseCase,
    private val getDirWithDepTmRpUseCase: GetDirWithDepTmRpUseCase,
    private val getDirWithTmRpUseCase: GetDirWithTmRpUseCase,
    private val getDirWithArrTmRpUseCase: GetDirWithArrTmRpUseCase
) {
    val directionsViewModel1Factory = DirectionsViewModel1Factory(
        getDirectionsUseCase,
        getDirWithDepTmRpUseCase,
        getDirWithTmRpUseCase,
        getDirWithArrTmRpUseCase
    )
}

class MyPromiseListContainer(
    private val loadToMyPromiseListUseCase: LoadToMyPromiseListUseCase
) {
    val myPromiseListViewModel = MyPromiseListViewModelFactory(
        loadToMyPromiseListUseCase
    )
}