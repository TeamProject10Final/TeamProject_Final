package com.example.donotlate.core.data.mapper

import com.example.donotlate.core.data.response.PromiseRoomResponse
import com.example.donotlate.core.data.response.FriendRequestDTO
import com.example.donotlate.core.data.response.UserResponse
import com.example.donotlate.core.domain.model.PromiseRoomEntity
import com.example.donotlate.core.domain.model.FriendRequestEntity
import com.example.donotlate.core.domain.model.UserEntity

fun List<UserResponse>.toUserEntityList(): List<UserEntity> {
    return this.map { it.toUserEntity() }
}

fun List<UserEntity>.toUserResponseList(): List<UserResponse> {
    return this.map { it.toUserResponse() }
}

fun UserEntity.toUserResponse() = UserResponse(
    name, email, uid, friends, count, continuousCounter, createdAt, profileImgUrl
)

fun UserResponse.toUserEntity() = UserEntity(
    name, email, uid, friends, count, continuousCounter, createdAt, profileImgUrl
)

fun PromiseRoomEntity.toPromiseRoomResponse() = PromiseRoomResponse(
    roomTitle, roomCreatedAt, promiseTime, promiseDate, destination, destinationLat, destinationLng, penalty, participants
)

fun PromiseRoomResponse.toPromiseEntity() = PromiseRoomEntity(
    roomTitle, roomCreatedAt, promiseTime, promiseDate, destination, destinationLat, destinationLng, penalty, participants
)

fun FriendRequestDTO.toEntity() = FriendRequestEntity(
    toId, fromId, status, requestTime, fromUserName
)

fun FriendRequestEntity.toDTO() = FriendRequestDTO(
    toId, fromId, status, requestTime, fromUserName
)

fun List<FriendRequestDTO>.toEntityList() : List<FriendRequestEntity>{
    return this.map { it.toEntity()}
    }

fun List<FriendRequestEntity>.toResponseList(): List<FriendRequestDTO>{
    return this.map { it.toDTO() }
}

fun List<PromiseRoomEntity>.toPromiseResponseList(): List<PromiseRoomResponse>{
    return this.map{it.toPromiseRoomResponse()}
}

fun List<PromiseRoomResponse>.toPromiseEntityList(): List<PromiseRoomEntity>{
    return this.map{it.toPromiseEntity()}
}


