package com.example.donotlate.feature.room.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.donotlate.databinding.ItemRvFriendBinding
import com.example.donotlate.feature.room.presentation.model.FriendListModel
import com.example.donotlate.feature.room.presentation.model.UserModel

class RoomFriendAdapter : ListAdapter<UserModel, RoomFriendAdapter.ListHolder>(diffUtil) {

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    var itemClick: ItemClick? = null


    inner class ListHolder(var binding: ItemRvFriendBinding) :
            RecyclerView.ViewHolder(binding.root) {
                fun bind(item: UserModel) {
                    binding.apply {
                        ivItemFriend.setImageURI(item.profileImgUrl.toUri())
                        tvItemFriend.text = item.name
                    }
                    itemView.setOnClickListener {
                        itemClick?.onClick(it, adapterPosition)
                    }
                }
            }
    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<UserModel>() {
            override fun areItemsTheSame(
                oldItem: UserModel,
                newItem: UserModel
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: UserModel,
                newItem: UserModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
        val binding = ItemRvFriendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListHolder(binding)
    }

    override fun onBindViewHolder(holder: ListHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

}