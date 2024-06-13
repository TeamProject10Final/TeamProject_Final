package com.example.donotlate.feature.friends.presentation.mapper

import com.example.donotlate.core.data.response.FriendRequestWithUserDataEntity
import com.example.donotlate.core.domain.model.FriendRequestEntity
import com.example.donotlate.core.domain.model.UserEntity
import com.example.donotlate.feature.friends.presentation.model.FriendRequestModel
import com.example.donotlate.feature.friends.presentation.model.FriendRequestWithUserDataModel
import com.example.donotlate.feature.friends.presentation.model.FriendsUserModel


fun List<FriendRequestEntity>.toFriendRequestModelList(): List<FriendRequestModel> {
    return this.map{ it. toModel()}
}

fun FriendRequestEntity.toModel() = FriendRequestModel(
    toId, fromId, status, requestTime, fromUserName
)

fun UserEntity.toModel() = FriendsUserModel(
    name, email, uid, friends, count, continuousCounter, createdAt
)

fun List<UserEntity>.toUserModelList():List<FriendsUserModel> {
    return this.map { it.toModel()}
}

fun List<FriendRequestWithUserDataEntity>.toModelList(): List<FriendRequestWithUserDataModel> {
    return this.map { entity ->
        FriendRequestWithUserDataModel(
            friendRequestModel = entity.friendRequestEntity.toModel(),
            userDataModel = entity.userDataEntity.toModel()
        )
    }
}
