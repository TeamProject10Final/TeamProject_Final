package com.example.donotlate.core.data.mapper

import com.example.donotlate.core.data.response.MessageResponse
import com.example.donotlate.core.domain.model.MessageEntity

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