package com.nomorelateness.donotlate.core.data.mapper

import com.nomorelateness.donotlate.core.data.response.UserResponse
import com.nomorelateness.donotlate.core.domain.model.UserEntity

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

