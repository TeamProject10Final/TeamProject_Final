package com.example.donotlate.feature.friends.data.mapper

import com.example.donotlate.core.domain.model.FriendRequestEntity
import com.example.donotlate.feature.friends.data.model.FriendRequestDTO


fun FriendRequestDTO.toEntity() = FriendRequestEntity(
    requestId = requestId,
    toId = toId,
    fromId = fromId,
    status = status,
    requestTime = requestTime,
    fromUserName = fromUserName
)

fun FriendRequestEntity.toDTO() = FriendRequestDTO(
    requestId = requestId,
    toId = toId,
    fromId = fromId,
    status = status,
    requestTime = requestTime,
    fromUserName = fromUserName
)