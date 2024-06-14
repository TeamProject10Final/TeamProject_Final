package com.example.donotlate.feature.mypromise.presentation.mapper

import com.example.donotlate.core.domain.model.PromiseRoomEntity
import com.example.donotlate.feature.mypromise.presentation.model.PromiseModel

fun PromiseRoomEntity.toPromiseModel() = PromiseModel(
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

fun PromiseModel.toPromiseEntity() = PromiseRoomEntity(
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

fun List<PromiseRoomEntity>.toPromiseModelList(): List<PromiseModel> {
    return this.map { it.toPromiseModel() }
}

fun List<PromiseModel>.toPromiseEntityList(): List<PromiseRoomEntity> {
    return this.map { it.toPromiseEntity() }
}