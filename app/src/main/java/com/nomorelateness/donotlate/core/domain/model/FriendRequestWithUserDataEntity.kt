package com.nomorelateness.donotlate.core.domain.model

data class FriendRequestWithUserDataEntity(
    val friendRequestEntity: FriendRequestEntity,
    val userDataEntity: UserEntity
)
