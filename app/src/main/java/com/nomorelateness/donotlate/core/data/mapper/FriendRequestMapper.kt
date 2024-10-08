package com.nomorelateness.donotlate.core.data.mapper

import com.nomorelateness.donotlate.core.data.response.FriendRequestDTO
import com.nomorelateness.donotlate.core.domain.model.FriendRequestEntity


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

fun List<FriendRequestDTO>.toEntityList() : List<FriendRequestEntity>{
    return this.map { it.toEntity()}
}

fun List<FriendRequestEntity>.toResponseList(): List<FriendRequestDTO>{
    return this.map { it.toDTO() }
}
