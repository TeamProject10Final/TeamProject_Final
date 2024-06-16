package com.example.donotlate.feature.room.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.donotlate.R
import com.example.donotlate.databinding.ItemRvFriendBinding
import com.example.donotlate.feature.room.presentation.model.RoomUserModel

class RoomFriendAdapter(
    private val onAddFriendClick: () -> Unit,
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

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_ADD_FRIEND else VIEW_TYPE_FRIEND
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ItemRvFriendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return if (viewType == VIEW_TYPE_ADD_FRIEND) {
            AddFriendViewHolder(binding, onAddFriendClick)
        } else {
            RoomFriendAdapterViewHolder(
                binding,
                onItemClick,
                this::toggleSelection,
                selectedPositions
            )
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RoomFriendAdapterViewHolder) {
            val actualPosition = position - 1
            if (actualPosition >= 0) {
                holder.bind(getItem(actualPosition), selectedPositions.contains(position))
            }
        } else if (holder is AddFriendViewHolder) {
            holder.bind()
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
                tvItemFriend.setTextColor(if (isSelected) R.color.blue_violet else R.color.black)
            }
            itemView.setOnClickListener {
                onItemClick(item)
                toggleSelection(adapterPosition)
            }
        }
    }

    class AddFriendViewHolder(
        private val binding: ItemRvFriendBinding,
        private val onAddFriendClick: () -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.tvItemFriend.text = "친구 추가"
            binding.ivItemFriend.load(R.drawable.ic_user) {
                transformations(CircleCropTransformation())
            }
            binding.root.setOnClickListener {
                onAddFriendClick()
            }
        }
    }


    companion object {
        private const val VIEW_TYPE_ADD_FRIEND = 0
        private const val VIEW_TYPE_FRIEND = 1

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
