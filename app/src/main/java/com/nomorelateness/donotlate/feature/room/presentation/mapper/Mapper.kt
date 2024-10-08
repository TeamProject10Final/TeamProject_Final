package com.nomorelateness.donotlate.feature.room.presentation.mapper

import com.nomorelateness.donotlate.core.domain.model.PromiseRoomEntity
import com.nomorelateness.donotlate.core.domain.model.UserEntity
import com.nomorelateness.donotlate.feature.room.presentation.model.PromiseRoomModel
import com.nomorelateness.donotlate.feature.room.presentation.model.RoomUserModel

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

fun PromiseRoomModel.toPromiseRoomEntity()= PromiseRoomEntity(
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