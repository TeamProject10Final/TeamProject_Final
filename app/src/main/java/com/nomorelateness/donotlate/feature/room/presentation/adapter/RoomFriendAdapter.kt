package com.nomorelateness.donotlate.feature.room.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.donotlate.R
import com.example.donotlate.databinding.ItemRvFriendBinding
import com.example.donotlate.feature.room.presentation.model.RoomUserModel

class RoomFriendAdapter(
    private val onItemClick: (RoomUserModel) -> Unit
) : ListAdapter<RoomUserModel, RecyclerView.ViewHolder>(diffUtil) {

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    var itemClick: ItemClick? = null

    private var selectedPositions = mutableSetOf<Int>()

    private fun toggleSelection(position: Int) {
        if (selectedPositions.contains(position)) {
            selectedPositions.remove(position)
        } else {
            selectedPositions.add(position)
        }
        notifyItemChanged(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ItemRvFriendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RoomFriendAdapterViewHolder(binding, onItemClick, this::toggleSelection, selectedPositions)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is RoomFriendAdapterViewHolder){
                holder.bind(getItem(position), selectedPositions.contains(position))
            }
        }


    class RoomFriendAdapterViewHolder(
        private val binding: ItemRvFriendBinding,
        private val onItemClick: (RoomUserModel) -> Unit,
        private val toggleSelection: (Int) -> Unit,
        private val selectedPosition: Set<Int>

    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RoomUserModel, isSelected: Boolean) {
            binding.apply {
                ivItemFriend.setImageResource(R.drawable.ic_user)
                tvItemFriend.text = item.name
                itemView.setBackgroundResource(if (isSelected) R.drawable.radius_rounded2 else R.color.backGround)
            }
            itemView.setOnClickListener {
                onItemClick(item)
                toggleSelection(adapterPosition)
            }
        }
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
}
