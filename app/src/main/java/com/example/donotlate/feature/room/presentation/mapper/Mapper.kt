package com.example.donotlate.feature.room.presentation.mapper

import com.example.donotlate.core.domain.model.UserEntity
import com.example.donotlate.feature.room.presentation.model.UserModel

fun List<UserEntity>.toModelList(): List<UserModel> {
    return this.map{ it.toModel()}
}

fun UserEntity.toModel() = UserModel(
    name, email, uId, friend, count, continuousCounter, createdAt, profileImgUrl
)