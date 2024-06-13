package com.example.donotlate.core.data.response

import com.example.donotlate.core.domain.model.FriendRequestEntity
import com.example.donotlate.core.domain.model.UserEntity

data class FriendRequestWithUserDataEntity(
    val friendRequestEntity: FriendRequestEntity,
    val userDataEntity: UserEntity
)
