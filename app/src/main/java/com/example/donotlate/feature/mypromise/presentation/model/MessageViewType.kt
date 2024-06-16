package com.example.donotlate.feature.mypromise.presentation.model

sealed class MessageViewType {
    data class SentMessage(val message: MessageModel) : MessageViewType()
    data class ReceiveMessage(val message: MessageModel) : MessageViewType()
}

fun MessageViewType.getMessageId(): String {
    return when (this) {
        is MessageViewType.SentMessage -> this.message.messageId
        is MessageViewType.ReceiveMessage -> this.message.messageId
    }
}

