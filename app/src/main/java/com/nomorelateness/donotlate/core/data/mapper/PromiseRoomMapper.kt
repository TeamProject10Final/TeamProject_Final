package com.nomorelateness.donotlate.core.data.mapper

import com.example.donotlate.core.data.response.PromiseRoomResponse
import com.example.donotlate.core.domain.model.PromiseRoomEntity

fun PromiseRoomEntity.toPromiseRoomResponse() = PromiseRoomResponse(
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

fun PromiseRoomResponse.toPromiseEntity() = PromiseRoomEntity(
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

fun List<PromiseRoomEntity>.toPromiseResponseList(): List<PromiseRoomResponse>{
    return this.map{it.toPromiseRoomResponse()}
}

fun List<PromiseRoomResponse>.toPromiseEntityList(): List<PromiseRoomEntity>{
    return this.map{it.toPromiseEntity()}
}


