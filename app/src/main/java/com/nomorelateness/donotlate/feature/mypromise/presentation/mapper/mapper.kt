package com.nomorelateness.donotlate.feature.mypromise.presentation.mapper

import com.nomorelateness.donotlate.core.domain.model.MessageEntity
import com.nomorelateness.donotlate.core.domain.model.PromiseRoomEntity
import com.nomorelateness.donotlate.core.domain.model.UserEntity
import com.nomorelateness.donotlate.feature.mypromise.presentation.model.MessageModel
import com.nomorelateness.donotlate.feature.mypromise.presentation.model.MessageViewType
import com.nomorelateness.donotlate.feature.mypromise.presentation.model.PromiseModel
import com.nomorelateness.donotlate.feature.mypromise.presentation.model.UserModel

fun PromiseRoomEntity.toPromiseModel() = PromiseModel(
    roomId = roomId,
    roomTitle = roomTitle,
    roomCreatedAt = roomCreatedAt,
    promiseTime = promiseTime,
    promiseDate = promiseDate,
    destination = destination,
    destinationLat = destinationLat,
    destinationLng = destinationLng,
    penalty = penalty,
    participants = participants,
    hasArrived = hasArrived,
    participantsNames = participantsNames,
    hasDeparture = hasDeparture
)

fun PromiseModel.toPromiseEntity() = PromiseRoomEntity(
    roomId = roomId,
    roomTitle = roomTitle,
    roomCreatedAt = roomCreatedAt,
    promiseTime = promiseTime,
    promiseDate = promiseDate,
    destination = destination,
    destinationLat = destinationLat,
    destinationLng = destinationLng,
    penalty = penalty,
    participants = participants,
    hasArrived = hasArrived,
    participantsNames = participantsNames,
    hasDeparture = hasDeparture
)

fun List<PromiseRoomEntity>.toPromiseModelList(): List<PromiseModel> {
    return this.map { it.toPromiseModel() }
}

fun List<PromiseModel>.toPromiseEntityList(): List<PromiseRoomEntity> {
    return this.map { it.toPromiseEntity() }
}

fun MessageEntity.toMessageModel() = MessageModel(
    messageId = messageId,
    senderId = senderId,
    senderName = senderName,
    sendTimestamp = sendTimestamp,
    contents = contents,
    senderProfileUrl = senderProfileUrl
)

fun MessageModel.toMessageEntity() = MessageEntity(
    messageId = messageId,
    senderId = senderId,
    senderName = senderName,
    sendTimestamp = sendTimestamp,
    contents = contents,
    senderProfileUrl = senderProfileUrl
)

fun List<MessageEntity>.toMessageModelList(): List<MessageModel> {
    return this.map { it.toMessageModel() }
}

fun List<MessageModel>.toMessageEntityList(): List<MessageEntity> {
    return this.map { it.toMessageEntity() }
}

fun MessageModel.toViewType(currentUserId: String): MessageViewType {
    return if (this.senderId == currentUserId) {
        MessageViewType.SentMessage(this)
    } else {
        MessageViewType.ReceiveMessage(this)
    }
}

fun UserEntity.toModel() = UserModel(
    name = name,
    email = email,
    uid = uid,
    friends = friends,
    count = count,
    continuousCounter = continuousCounter,
    createdAt = createdAt
)

fun UserModel.toEntity() = UserEntity(
    name = name,
    email = email,
    uid = uid,
    friends = friends,
    count = count,
    continuousCounter = continuousCounter,
    createdAt = createdAt
)

fun List<UserEntity>.toModelList(): List<UserModel> {
    return this.map { it.toModel() }
}

fun List<UserModel>.toEntityList(): List<UserEntity> {
    return this.map { it.toEntity() }
}