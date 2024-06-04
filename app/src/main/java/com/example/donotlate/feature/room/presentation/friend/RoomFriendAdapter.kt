package com.example.donotlate.feature.room.presentation.friend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.donotlate.databinding.ItemRvFriendBinding

class RoomFriendAdapter : ListAdapter<FriendListModel, RoomFriendAdapter.ListHolder>(diffUtil) {

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    var itemClick: ItemClick? = null


    inner class ListHolder(var binding: ItemRvFriendBinding) :
            RecyclerView.ViewHolder(binding.root) {
                fun bind(item: FriendListModel) {
                    binding.apply {
                        ivItemFriend.setImageURI(item.url.toUri())
                        tvItemFriend.text = item.id
                    }
                    itemView.setOnClickListener {
                        itemClick?.onClick(it, adapterPosition)
                    }
                }
            }
    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<FriendListModel>() {
            override fun areItemsTheSame(
                oldItem: FriendListModel,
                newItem: FriendListModel
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: FriendListModel,
                newItem: FriendListModel
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