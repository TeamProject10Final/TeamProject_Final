package com.example.donotlate.core.domain.model

import com.example.donotlate.core.domain.model.FriendRequestEntity
import com.example.donotlate.core.domain.model.UserEntity

data class FriendRequestWithUserDataEntity(
    val friendRequestEntity: FriendRequestEntity,
    val userDataEntity: UserEntity
)
