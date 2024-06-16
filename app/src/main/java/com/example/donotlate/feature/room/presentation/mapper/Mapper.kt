package com.example.donotlate.feature.room.presentation.mapper

import com.example.donotlate.core.domain.model.UserEntity
import com.example.donotlate.feature.room.presentation.model.RoomUserModel

fun List<UserEntity>.toModelList(): List<RoomUserModel> {
    return this.map { it.toRoomUserModel() }
}

fun UserEntity.toRoomUserModel() = RoomUserModel(
    name = name,
    email = email,
    uId = uid,
    friend = friends,
    count = count,
    continuousCounter = continuousCounter,
    createdAt = createdAt,
    profileImgUrl = profileImgUrl
)