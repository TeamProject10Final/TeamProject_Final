package com.example.donotlate.feature.friends.data.mapper

import com.example.donotlate.core.domain.model.FriendRequestEntity
import com.example.donotlate.feature.friends.data.model.FriendRequestDTO


fun FriendRequestDTO.toEntity() = FriendRequestEntity(
    toId, fromId, status, requestTime, fromUserName
)

fun FriendRequestEntity.toDTO() = FriendRequestDTO(
    toId, fromId, status, requestTime, fromUserName
)