package com.example.donotlate.feature.main.presentation.mapper

import com.example.donotlate.core.domain.model.PromiseRoomEntity
import com.example.donotlate.core.domain.model.UserEntity
import com.example.donotlate.feature.main.presentation.model.UserModel
import com.example.donotlate.feature.mypromise.presentation.model.PromiseModel

fun UserEntity.toModel() = UserModel(
    name, email, uid, friends, count, continuousCounter, createdAt, profileImgUrl
)

fun PromiseRoomEntity.toModel() = PromiseModel(
    roomId,
    roomTitle,
    roomCreatedAt,
    promiseTime,
    promiseDate,
    destination,
    destinationLat,
    destinationLng,
    penalty,
    participants
)

fun PromiseRoomEntity.toEntity() = PromiseRoomEntity(
    roomId,
    roomTitle,
    roomCreatedAt,
    promiseTime,
    promiseDate,
    destination,
    destinationLat,
    destinationLng,
    penalty,
    participants
)
