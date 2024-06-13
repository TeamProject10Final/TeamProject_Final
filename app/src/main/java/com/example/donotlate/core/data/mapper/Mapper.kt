package com.example.donotlate.core.data.mapper

import com.example.donotlate.core.data.response.ChatRoomResponse
import com.example.donotlate.core.data.response.FriendRequestDTO
import com.example.donotlate.core.data.response.UserResponse
import com.example.donotlate.core.domain.model.ChatRoomEntity
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

fun ChatRoomEntity.toChatRoomResponse() = ChatRoomResponse(
    roomTitle, destination, date, time, penalty, participants
)

fun ChatRoomResponse.toChatRoomEntity() = ChatRoomEntity(
    roomTitle, destination, date, time, penalty, participants
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



