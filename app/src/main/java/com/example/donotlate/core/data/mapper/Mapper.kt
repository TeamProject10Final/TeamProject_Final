package com.example.donotlate.core.data.mapper

import com.example.donotlate.core.data.response.FriendRequestDTO
import com.example.donotlate.core.data.response.MessageResponse
import com.example.donotlate.core.data.response.PromiseRoomResponse
import com.example.donotlate.core.data.response.UserResponse
import com.example.donotlate.core.domain.model.FriendRequestEntity
import com.example.donotlate.core.domain.model.MessageEntity
import com.example.donotlate.core.domain.model.PromiseRoomEntity
import com.example.donotlate.core.domain.model.UserEntity

fun List<UserResponse>.toUserEntityList(): List<UserEntity> {
    return this.map { it.toUserEntity() }
}

fun List<UserEntity>.toUserResponseList(): List<UserResponse> {
    return this.map { it.toUserResponse() }
}

fun UserEntity.toUserResponse() = UserResponse(
    name = name,
    email = email,
    uid = uid,
    friends = friends,
    count = count,
    continuousCounter = continuousCounter,
    createdAt = createdAt,
    profileImgUrl = profileImgUrl
)

fun UserResponse.toUserEntity() = UserEntity(
    name = name,
    email = email,
    uid = uid,
    friends = friends,
    count = count,
    continuousCounter = continuousCounter,
    createdAt = createdAt,
    profileImgUrl = profileImgUrl
)

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
    participants = participants
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
    participants = participants
)

fun FriendRequestDTO.toEntity() = FriendRequestEntity(
    toId = toId,
    fromId = fromId,
    status = status,
    requestTime = requestTime,
    fromUserName = fromUserName
)

fun FriendRequestEntity.toDTO() = FriendRequestDTO(
    toId = toId,
    fromId = fromId,
    status = status,
    requestTime = requestTime,
    fromUserName = fromUserName
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

fun MessageResponse.toMessageEntity() = MessageEntity(
    messageId = messageId,
    senderId = senderId,
    senderName = senderName,
    sendTimestamp = sendTimestamp,
    contents = contents,
    senderProfileUrl = senderProfileUrl
)

fun MessageEntity.toMessageResponse() = MessageResponse(
    messageId = messageId,
    senderId = senderId,
    senderName = senderName,
    sendTimestamp = sendTimestamp,
    contents = contents,
    senderProfileUrl = senderProfileUrl
)

fun List<MessageEntity>.toMessageResponseList(): List<MessageResponse> {
    return this.map { it.toMessageResponse() }
}

fun List<MessageResponse>.toMessageEntityList(): List<MessageEntity> {
    return this.map { it.toMessageEntity() }
}
