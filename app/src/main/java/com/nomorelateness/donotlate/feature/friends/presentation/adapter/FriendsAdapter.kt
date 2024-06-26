package com.nomorelateness.donotlate.feature.friends.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nomorelateness.donotlate.R
import com.nomorelateness.donotlate.databinding.ItemRvFriendBinding
import com.nomorelateness.donotlate.feature.friends.presentation.model.FriendsUserModel

class FriendsAdapter(
    private val onAddFriendClick: () -> Unit,
    private val onItemClick: (FriendsUserModel) -> Unit
) :
    ListAdapter<FriendsUserModel, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_ADD_FRIEND else VIEW_TYPE_FRIEND
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ADD_FRIEND) {
            val binding =
                ItemRvFriendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            AddFriendViewHolder(binding)
        } else {
            val binding =
                ItemRvFriendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            FriendsAdapterViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AddFriendViewHolder) {
            holder.bind(onAddFriendClick)
        } else if (holder is FriendsAdapterViewHolder) {
            val actualPosition = position - 1
            if (actualPosition >= 0) {
                holder.bind(getItem(actualPosition), onItemClick)
            }
        }
    }

    class FriendsAdapterViewHolder(
        private val binding: ItemRvFriendBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(friend: FriendsUserModel, onItemClick: (FriendsUserModel) -> Unit) {
            binding.tvItemFriend.text = friend.name

            itemView.setOnClickListener { onItemClick }
        }
    }

    class AddFriendViewHolder(private val binding: ItemRvFriendBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ResourceAsColor")
        fun bind(onAddFriendClick: () -> Unit) {
            binding.tvItemFriend.text = "친구 추가"
            binding.ivItemFriend.setImageResource(R.drawable.ic_add_user)
            binding.root.setOnClickListener {
                onAddFriendClick()
            }
        }
    }

    companion object {
        private const val VIEW_TYPE_ADD_FRIEND = 0
        private const val VIEW_TYPE_FRIEND = 1

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FriendsUserModel>() {
            override fun areItemsTheSame(
                oldItem: FriendsUserModel,
                newItem: FriendsUserModel
            ): Boolean {
                return oldItem.uid == newItem.uid
            }

            override fun areContentsTheSame(
                oldItem: FriendsUserModel,
                newItem: FriendsUserModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}

