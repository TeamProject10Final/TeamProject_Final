package com.example.donotlate.core.data.mapper

import com.example.donotlate.core.data.response.ChatRoomResponse
import com.example.donotlate.core.data.response.UserResponse
import com.example.donotlate.core.domain.model.ChatRoomEntity
import com.example.donotlate.core.domain.model.UserEntity

fun List<UserResponse>.toEntityList(): List<UserEntity> {
    return this.map{ it.toEntity()}
}

fun List<UserEntity>.toResponseList(): List<UserResponse>{
    return this.map{it.toResponse()}
}

fun UserEntity.toResponse() = UserResponse(
    name,email, uId, friend, count, continuousCounter,createdAt,profileImgUrl)

fun UserResponse.toEntity() = UserEntity(
    name,email,uId,friend,count,continuousCounter,createdAt,profileImgUrl)

fun ChatRoomEntity.toResponse() = ChatRoomResponse(
    roomTitle, destination, date, time, penalty, participants
)

fun ChatRoomResponse.toEntity() = ChatRoomEntity(
    roomTitle, destination, date, time, penalty, participants
)

