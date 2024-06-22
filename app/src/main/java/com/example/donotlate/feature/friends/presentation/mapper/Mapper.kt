package com.example.donotlate.feature.friends.presentation.mapper

import com.example.donotlate.core.domain.model.FriendRequestWithUserDataEntity
import com.example.donotlate.core.domain.model.FriendRequestEntity
import com.example.donotlate.core.domain.model.UserEntity
import com.example.donotlate.feature.friends.presentation.model.FriendRequestModel
import com.example.donotlate.feature.friends.presentation.model.FriendRequestWithUserDataModel
import com.example.donotlate.feature.friends.presentation.model.FriendsUserModel


fun List<FriendRequestEntity>.toFriendRequestModelList(): List<FriendRequestModel> {
    return this.map{ it. toModel()}
}

fun FriendRequestEntity.toModel() = FriendRequestModel(
    requestId = requestId,
    toId = toId,
    fromId = fromId,
    status = status,
    requestTime = requestTime,
    fromUserName = fromUserName
)

fun UserEntity.toModel() = FriendsUserModel(
    name = name,
    email = email,
    uid = uid,
    friends = friends,
    count = count,
    continuousCounter = continuousCounter,
    createdAt = createdAt
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
