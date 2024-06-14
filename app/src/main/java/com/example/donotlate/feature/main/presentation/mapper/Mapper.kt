package com.example.donotlate.feature.main.presentation.mapper

import com.example.donotlate.core.domain.model.PromiseRoomEntity
import com.example.donotlate.core.domain.model.UserEntity
import com.example.donotlate.feature.main.presentation.model.ChatRoomModel
import com.example.donotlate.feature.main.presentation.model.UserModel

fun UserEntity.toModel() = UserModel(
    name, email, uid, friends, count, continuousCounter, createdAt, profileImgUrl
)

fun PromiseRoomEntity.toModel() = ChatRoomModel(
    roomTitle, destination, date, time, penalty, participants
)

fun ChatRoomModel.toEntity() = PromiseRoomEntity(
    roomTitle, destination, date, time, penalty, participants
)
