package com.nomorelateness.donotlate.feature.friends.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FriendRequestWithUserDataModel(
    val friendRequestModel: FriendRequestModel,
    val userDataModel: FriendsUserModel
): Parcelable
