package com.nomorelateness.donotlate.feature.main.presentation.mapper

import com.nomorelateness.donotlate.core.domain.model.PromiseRoomEntity
import com.nomorelateness.donotlate.core.domain.model.UserEntity
import com.nomorelateness.donotlate.feature.main.presentation.model.UserModel
import com.nomorelateness.donotlate.feature.mypromise.presentation.model.PromiseModel

fun UserEntity.toModel() = UserModel(
    name = name,
    email = email,
    uId = uid,
    friend = friends,
    count = count,
    continuousCounter = continuousCounter,
    createdAt = createdAt,
    profileImgUrl = profileImgUrl
)

fun PromiseRoomEntity.toModel() = PromiseModel(
    roomId = roomId,
    roomTitle = roomTitle,
    roomCreatedAt = roomCreatedAt,
    promiseTime = promiseTime,
    promiseDate = promiseDate,
    destination = destination,
    destinationLat = destinationLat,
    destinationLng = destinationLng,
    penalty = penalty,
    participants = participants,
    hasArrived = hasArrived,
    participantsNames = participantsNames,
    hasDeparture = hasDeparture
)

fun PromiseRoomEntity.toEntity() = PromiseRoomEntity(
    roomId = roomId,
    roomTitle = roomTitle,
    roomCreatedAt = roomCreatedAt,
    promiseTime = promiseTime,
    promiseDate = promiseDate,
    destination = destination,
    destinationLat = destinationLat,
    destinationLng = destinationLng,
    penalty = penalty,
    participants = participants,
    hasArrived = hasArrived,
    participantsNames = participantsNames,
    hasDeparture = hasDeparture
)
