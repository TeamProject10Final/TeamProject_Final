package com.example.donotlate.feature.room.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.donotlate.R
import com.example.donotlate.databinding.ItemRvFriendBinding
import com.example.donotlate.feature.room.presentation.model.RoomUserModel

class RoomFriendAdapter() : ListAdapter<RoomUserModel, RoomFriendAdapter.ListHolder>(diffUtil) {
    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    var itemClick: ItemClick? = null
    private var selectedPositions = mutableSetOf<Int>()


    inner class ListHolder(var binding: ItemRvFriendBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RoomUserModel, isSelected: Boolean) {
            binding.apply {
                ivItemFriend.setImageURI(item.profileImgUrl.toUri())
                tvItemFriend.text = item.name
                itemView.setBackgroundResource(if(isSelected) R.drawable.ic_main_friend else 0)
            }
            itemView.setOnClickListener {
                itemClick?.onClick(it, adapterPosition)
                toggleSelection(adapterPosition)
            }
        }
    }

    private fun toggleSelection(position: Int){
        if(selectedPositions.contains(position)){
            selectedPositions.remove(position)
        }else{
            selectedPositions.add(position)
        }
        notifyItemChanged(position)
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<RoomUserModel>() {
            override fun areItemsTheSame(
                oldItem: RoomUserModel,
                newItem: RoomUserModel
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: RoomUserModel,
                newItem: RoomUserModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
        val binding =
            ItemRvFriendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListHolder(binding)
    }

    override fun onBindViewHolder(holder: ListHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, selectedPositions.contains(position))
    }

}