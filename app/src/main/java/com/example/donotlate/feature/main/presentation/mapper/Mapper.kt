package com.example.donotlate.feature.main.presentation.mapper

import com.example.donotlate.core.domain.model.ChatRoomEntity
import com.example.donotlate.core.domain.model.UserEntity
import com.example.donotlate.feature.main.presentation.model.ChatRoomModel
import com.example.donotlate.feature.main.presentation.model.UserModel

fun UserEntity.toModel() = UserModel(
    name,email,uId,friend,count,continuousCounter,createdAt,profileImgUrl
)

fun ChatRoomEntity.toModel() = ChatRoomModel(
    roomTitle, destination, date, time, penalty, participants
)

fun ChatRoomModel.toEntity() = ChatRoomEntity(
    roomTitle, destination, date, time, penalty, participants
)
