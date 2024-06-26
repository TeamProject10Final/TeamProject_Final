package com.nomorelateness.donotlate.feature.mypromise.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nomorelateness.donotlate.core.util.toFormattedHMString
import com.nomorelateness.donotlate.databinding.ItemReceiveMessageBinding
import com.nomorelateness.donotlate.databinding.ItemSentMessageBinding
import com.nomorelateness.donotlate.feature.mypromise.presentation.model.MessageModel
import com.nomorelateness.donotlate.feature.mypromise.presentation.model.MessageViewType
import com.nomorelateness.donotlate.feature.mypromise.presentation.model.getMessageId

class PromiseMessageAdapter() :
    ListAdapter<MessageViewType, RecyclerView.ViewHolder>(MessageDiffCallback()) {


    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is MessageViewType.SentMessage -> VIEW_TYPE_SENT
            is MessageViewType.ReceiveMessage -> VIEW_TYPE_RECEIVED
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_SENT -> SentMessageViewHolder(
                ItemSentMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )

            VIEW_TYPE_RECEIVED -> ReceivedMessageViewHolder(
                ItemReceiveMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SentMessageViewHolder -> holder.bind((getItem(position) as MessageViewType.SentMessage).message)
            is ReceivedMessageViewHolder -> holder.bind((getItem(position) as MessageViewType.ReceiveMessage).message)
        }
    }

    class SentMessageViewHolder(private val binding: ItemSentMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: MessageModel) {
            binding.tvMessage.text = message.contents
            binding.tvSendTime.text = message.sendTimestamp.toFormattedHMString()

        }
    }

    class ReceivedMessageViewHolder(private val binding: ItemReceiveMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: MessageModel) {
            binding.tvMessage.text = message.contents
            binding.tvSendTime.text = message.sendTimestamp.toFormattedHMString()
            binding.tvSenderId.text = message.senderName
        }
    }

    companion object {
        private const val VIEW_TYPE_SENT = 1
        private const val VIEW_TYPE_RECEIVED = 2
    }
}

class MessageDiffCallback() : DiffUtil.ItemCallback<MessageViewType>() {
    override fun areItemsTheSame(oldItem: MessageViewType, newItem: MessageViewType): Boolean {
        return oldItem.getMessageId() == newItem.getMessageId()
    }

    override fun areContentsTheSame(oldItem: MessageViewType, newItem: MessageViewType): Boolean {
        return oldItem == newItem
    }
}